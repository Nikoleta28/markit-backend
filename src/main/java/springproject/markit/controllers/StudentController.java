package springproject.markit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springproject.markit.controllers.entities.CourseDTO;
import springproject.markit.controllers.entities.StudentAssignmentMark;
import springproject.markit.controllers.entities.StudentStudyHours;
import springproject.markit.controllers.entities.profAssignment;
import springproject.markit.models.*;
import springproject.markit.repositories.*;
import springproject.markit.services.AssignmentMarkService;
import springproject.markit.services.FinalMarkService;
import springproject.markit.services.StudentService;
import springproject.markit.services.StudyHoursService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/student")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SecretariatRepository secretariatRepository;
    @Autowired
    private FinalMarkRepository finalMarkRepository;
    @Autowired
    private FinalMarkService finalMarkService;
    @Autowired
    private AssignmentMarkRepository assignmentMarkRepository;
    @Autowired
    private AssignmentMarkService assignmentMarkService;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private StudyHoursRepository studyHoursRepository;
    @Autowired
    private StudyHoursService studyHoursService;

    @GetMapping("/allStudents")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @PostMapping("/newStudent")
    public String registerNewStudent(@RequestBody Student student) {
        studentService.createStudent(student);
        return "Student created!";
    }

    @DeleteMapping("/{studentId}/deleteStudent")
    public String deleteStudent(@PathVariable(value = "studentId") Long id) {
        studentService.deleteStudent(id);
        return "Student deleted!";
    }

    @GetMapping("/{studentId}")
    public Optional<Student> getStudentById(@PathVariable(value = "studentId") Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping("/{studentId}/addCourseToStudent")
    public List<Course> addCourseToStudent(@PathVariable Long studentId, @RequestBody Map<String, String> credentials) {

        Long id = Long.valueOf(credentials.get("id"));
        Course course = courseRepository.getCourseById(id);
        Student student = studentRepository.getStudentById(studentId);

        if (student != null) {
            if (course != null) {
                List<Course> courseList = student.getCourseList();
                if (courseList == null) {
                    student.setCourseList(Arrays.asList(course));
                }
                if (courseList.contains(course)) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course already exists exist");
                }
                courseList.add(course); // Add the existing course to the list


//                courseRepository.save(course);

//                for(Assignment a: course.getAssignmentList()){
//                    AssignmentMark assignmentMark = new AssignmentMark(null);
//
//                    student.addAssignmentMark(assignmentMark);
//                    assignmentMark.setStudent(student);
//                    a.addAssignmentMark(assignmentMark);
//                    assignmentMark.setAssignment(a);
//
//                    assignmentMarkRepository.save(assignmentMark);
//                }

                FinalMark finalMark = new FinalMark(null);

                student.addFinalMark(finalMark);
                finalMark.setStudent(student);
                course.addFinalMark(finalMark);
                finalMark.setCourse(course);

                finalMarkRepository.save(finalMark);

                courseRepository.save(course);
                studentRepository.save(student);

                return student.getCourseList();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student doesn't exist");
        }
    }


    @DeleteMapping("/{studentId}/courses/deleteCourse")
    public List<Course> deleteCourse(@PathVariable Long studentId, @RequestBody Map<String, String> credentials) {

        Long id = Long.valueOf(credentials.get("id"));
        Course course = courseRepository.getCourseById(id);
        Student student = studentRepository.getStudentById(studentId);

        if (student != null) {
            if (course != null) {
                List<Course> courses = student.getCourseList();
                if (courses.contains(course)) {

                    List<Assignment> assignments = course.getAssignmentList();
                    if (!assignments.isEmpty()) {
                        for (Assignment assignment : assignments) {
                            AssignmentMark assignmentMark = assignmentMarkRepository.findByStudentIdAndAssignmentId(studentId, assignment.getId());
                            if (assignmentMark != null) {
                                student.removeAssignmentMark(assignmentMark);
                                assignment.removeAssignmentMark(assignmentMark);
                                assignmentMarkRepository.delete(assignmentMark);
                            }
                        }
                    }

                    FinalMark finalMarkToDelete = finalMarkRepository.findByStudentIdAndCourseId(studentId, id);
                    student.removeFinalMark(finalMarkToDelete);
                    course.removeFinalMark(finalMarkToDelete);
                    finalMarkRepository.delete(finalMarkToDelete);

                    // Delete study hours for the course (if any)
                    List<StudyHours> studyHoursList = student.getStudyHoursList();
                    if (!studyHoursList.isEmpty()) {
                        List<StudyHours> studyHoursToDelete = new ArrayList<>();
                        for (StudyHours studyHours : studyHoursList) {
                            if (studyHours.getCourse().equals(course)) {
                                studyHoursToDelete.add(studyHours);
                            }
                        }
                        student.getStudyHoursList().removeAll(studyHoursToDelete);
                        studyHoursRepository.deleteAll(studyHoursToDelete);
                    }

                    student.removeCourse(course);
                }

                studentRepository.save(student);
                List<Course> courseList = student.getCourseList();
                return courseList;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }
    }

    @GetMapping("/{studentId}/course")
    public ResponseEntity<Course> getCourse(@PathVariable Long studentId, @RequestParam Long courseId) {
        Long id = Long.valueOf(courseId);
        Student student = studentRepository.getStudentById(studentId);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        if (student != null) {
            Course course = courseRepository.getCourseById(id);
            return ResponseEntity.ok(course);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }
    }

    @GetMapping("/{studentId}/course/{courseId}/finalMark")
    public FinalMark getFinalMark(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        if (student != null) {
            if (course != null) {
                FinalMark finalMark = finalMarkRepository.findByStudentIdAndCourseId(studentId, courseId);
                return finalMark;

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }
    }


    @PostMapping("/{studentId}/course/{courseId}/addFinalMark")
    public FinalMark addFinalMark(@PathVariable Long studentId, @PathVariable Long courseId, @RequestBody Map<String, String> credentials) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        Float mark = Float.valueOf(Long.valueOf(credentials.get("mark")));

        FinalMark finalMark = finalMarkRepository.findByStudentIdAndCourseId(studentId, courseId);

        if (student != null) {
            if (course != null) {

//                student.addFinalMark(finalMark);
//                finalMark.setStudent(student);
//                course.addFinalMark(finalMark);
//                finalMark.setCourse(course);
//                courseRepository.save(course);
//                studentRepository.save(student);

                FinalMark finalMarkSaved = finalMarkService.updateFinalMark(finalMark.getId(), mark);

                return finalMarkSaved;

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }

    }


    @DeleteMapping("/{studentId}/course/{courseId}/deleteFinalMark")
    public FinalMark deleteFinalMark(@PathVariable Long studentId, @PathVariable Long courseId, @RequestBody Map<String, String> credentials) {


        Float mark = Float.valueOf(credentials.get("mark"));

        FinalMark finalMark = finalMarkRepository.findByStudentIdAndCourseIdAndMark(studentId, courseId, mark);

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        if (student != null) {
            if (course != null) {

                finalMark.setMark(null);
                finalMarkRepository.save(finalMark);
                return finalMark;

//                student.removeFinalMark(finalMark);
//                course.removeFinalMark(finalMark);
//
//                finalMarkRepository.delete(finalMark);
//
//                courseRepository.save(course);
//                studentRepository.save(student);

//                FinalMark finalMarkSaved= finalMarkRepository.findByStudentIdAndCourseId(studentId,courseId);
//                return ResponseEntity.ok("final mark deleted");
//                Map<String, String> response = new HashMap<>();
//                response.put("message", "Final mark deleted");
//                return ResponseEntity.ok(response);
//                FinalMark finalMarkSaved = finalMark.setMark(null);

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }

    }

//    @PostMapping("/{studentId}/assignmentsmarks")
//    public Student addAssignmentMarkToStudent(@PathVariable Long studentId, @RequestBody AssignmentMark assignmentMark) {
//        return studentService.addAssignmentMarkToStudent(studentId, assignmentMark);
//    }

//    @GetMapping("/{studentId}/course/{courseId}/assignmentMarkList")
//    public  List<StudentAssignmentMark> getCourseAssignmentMarkList(@PathVariable Long studentId, @PathVariable Long courseId) {
//
//        Student student = studentRepository.getStudentById(studentId);
//        Course course = courseRepository.getCourseById(courseId);
//
//       List<StudentAssignmentMark> studentAssignmentMarks =  new ArrayList<>();
//
//        if (student != null) {
//            if (course != null) {
////                  for(Assignment a: course.getAssignmentList()) {
////                      //we check if there is an assign mark for the assignment a
////                      AssignmentMark assToAdd = assignmentMarkRepository.findByStudentIdAndAssignmentId(studentId,a.getId());
////
////                      //if there is an assignment mark object
////                      if(assToAdd != null){
////                          studentAssignmentMarks.add(new StudentAssignmentMark(assToAdd.getId(), assToAdd.getMark() , a.getTitle()));
////                      }else{
////                          //if there is not, we create a new one and give it a null mark
////                          AssignmentMark am = new AssignmentMark(null);
////                          am.setStudent(student);
////                          am.etAssignment(a);
////                          studentAssignmentMarks.add(new StudentAssignmentMark(am.getId(),a.getTitle()));
////                      }
////                  }
//
//
//                for(Assignment a: course.getAssignmentList()){
//                    AssignmentMark assToAdd = assignmentMarkRepository.findByStudentIdAndAssignmentId(studentId,a.getId());
//                    if(assToAdd!=null) {
//                        studentAssignmentMarks.add(new StudentAssignmentMark(assToAdd.getId(), assToAdd.getMark(), a.getTitle()));
//                    }
//                    else{
//
//                    }
//
//
//                }
//
//
//
//                  return studentAssignmentMarks;
//
//            } else {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
//            }
//        }
//        else{
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"student doesn't exist");
//        }
//
//    }

    @GetMapping("/{studentId}/course/{courseId}/assignmentMarkList")
    public List<StudentAssignmentMark> getCourseAssignmentMarkList(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);


        if (student != null) {
            if (course != null) {
                List<StudentAssignmentMark> studentAssignmentMarks = new ArrayList<>();

                // Retrieve the assignments for the course
                List<Assignment> assignments = course.getAssignmentList();

                // Retrieve or create assignment marks for each assignment
                for (Assignment assignment : assignments) {
                    AssignmentMark assignmentMark = assignmentMarkRepository.findByStudentIdAndAssignmentId(studentId, assignment.getId());

                    // If assignment mark doesn't exist, create a new one
                    if (assignmentMark == null) {
                        assignmentMark = new AssignmentMark(null);
                        assignmentMark.setAssignment(assignment);
                        assignmentMark.setStudent(student);
                        assignmentMarkRepository.save(assignmentMark);

                        student.addAssignmentMark(assignmentMark);
                        assignment.addAssignmentMark(assignmentMark);

                        assignmentRepository.save(assignment);
                        studentRepository.save(student);
                    }

                    studentAssignmentMarks.add(new StudentAssignmentMark(assignmentMark.getId(), assignmentMark.getMark(), assignment.getTitle()));

                }

                return studentAssignmentMarks;

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }

    }

    @PostMapping("/{studentId}/course/{courseId}/assignment/{editingAssignmentMarkId}/addAssignmentMark")
    public List<StudentAssignmentMark> addAssignmentMarkToStudent(@PathVariable Long studentId, @PathVariable Long courseId, @PathVariable Long editingAssignmentMarkId, @RequestBody Map<String, String> credentials) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        Long id = editingAssignmentMarkId;
        AssignmentMark assMarkToUpdate = assignmentMarkRepository.getAssignmentMarkById(id);

        Float mark = Float.valueOf(credentials.get("mark"));

        List<StudentAssignmentMark> studentAssignmentMarks = new ArrayList<>();

        if (student != null) {
            if (course != null) {
                if (assMarkToUpdate != null) {

                    assignmentMarkService.updateAssignmentMark(id, mark);

                    for (Assignment a : course.getAssignmentList()) {
                        AssignmentMark assToAdd = assignmentMarkRepository.findByStudentIdAndAssignmentId(studentId, a.getId());
                        studentAssignmentMarks.add(new StudentAssignmentMark(assToAdd.getId(), assToAdd.getMark(), a.getTitle()));
                    }

                    return studentAssignmentMarks;
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "assignment mark doesn't exist");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }

    }


//    @PostMapping("/{studentId}/course/{courseId}/assignment/{editingAssignmentMarkId}/addAssignmentMark")
//    public List<StudentAssignmentMark> addAssignmentMarkToStudent(@PathVariable Long studentId,@PathVariable Long courseId,@PathVariable Long editingAssignmentMarkId, @RequestBody Map<String, String> credentials ) {
//
//        Student student = studentRepository.getStudentById(studentId);
//        Course course = courseRepository.getCourseById(courseId);
////        Assignment assignment = assignmentRepository.getAssignmentById(editingAssignmentMarkId);
//
//        Long id = editingAssignmentMarkId;
//        AssignmentMark assMarkToUpdate = assignmentMarkRepository.getAssignmentMarkById(id);
//
//        Float mark = Float.valueOf(credentials.get("mark"));
//
//        List<StudentAssignmentMark> studentAssignmentMarks =  new ArrayList<>();
//
//        if (student != null) {
//            if (course != null) {
////                if(assignment!=null){
////                    AssignmentMark assignmentMark = new AssignmentMark(mark);
////
////                    student.addAssignmentMark(assignmentMark);
////                    assignmentMark.setStudent(student);
////                    assignment.addAssignmentMark(assignmentMark);
////                    assignmentMark.setAssignment(assignment);
////
////                    assignmentMarkRepository.save(assignmentMark);
////
////                    courseRepository.save(course);
////                    studentRepository.save(student);
////
//////                       return assignmentMark;
////
////                    for(Assignment a: course.getAssignmentList()) {
////                        AssignmentMark assToAdd = assignmentMarkRepository.findByStudentIdAndAssignmentId(studentId,a.getId());
////                        if(assToAdd != null){
////                            studentAssignmentMarks.add(new StudentAssignmentMark(assToAdd.getId(), assToAdd.getMark() , a.getTitle()));
////                        }else{
////                            AssignmentMark am = new AssignmentMark(assToAdd.getMark());
////                            studentAssignmentMarks.add(new StudentAssignmentMark(am.getId(),am.getMark(),a.getTitle()));
////                        }
////                    }
////
////                    return studentAssignmentMarks;
////
////                } else {
////                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "assignment doesn't exist");
////                }
//                if (assMarkToUpdate !=null) {
//                    AssignmentMark updatedAssignmentMark = assignmentMarkService.updateAssignmentMark(id,mark);
//
//
//                    for(Assignment a: course.getAssignmentList()){
//                        for(AssignmentMark as: a.getAssignmentMarkList()){
//                            AssignmentMark assToAdd = assignmentMarkRepository.findByStudentIdAndAssignmentId(studentId,a.getId());
//                            studentAssignmentMarks.add(new StudentAssignmentMark(assToAdd.getId(), assToAdd.getMark() , a.getTitle()));
//                        }
//                    }
//
//                    return studentAssignmentMarks;
//
//                }
//                else {
//                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "assignment mark doesn't exist");
//                }
//
//            } else {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
//            }
//        }
//        else{
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"student doesn't exist");
//        }
////        return studentService.addAssignmentMarkToStudent(studentId, assignmentMark);
//    }
//
//


    @PostMapping("/{studentId}/course/{courseId}/assignment/deleteAssignmentMark")
    public List<StudentAssignmentMark> deleteAssignmentMarkFromStudent(@PathVariable Long studentId, @PathVariable Long courseId, @RequestBody Map<String, String> credentials) {

        Float mark = Float.valueOf(credentials.get("mark"));
        Long id = Long.valueOf(credentials.get("id"));
        String assignTitle = credentials.get("assignmentName");

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);
        Assignment assignment = assignmentRepository.findByCourseIdAndTitle(courseId, assignTitle);

        AssignmentMark assToDelete = assignmentMarkRepository.findByIdAndMarkAndStudentIdAndAssignmentId(id, mark, studentId, assignment.getId());

        List<StudentAssignmentMark> studentAssignmentMarks = new ArrayList<>();

        if (student != null) {
            if (course != null) {
                if (assToDelete != null) {

//                    student.removeAssignmentMark(assToDelete);
//                    assignment.removeAssignmentMark(assToDelete);

//
//                    courseRepository.save(course);
//                    studentRepository.save(student);

//                    assignmentMarkRepository.delete(assToDelete);

//                    Map<String, String> response = new HashMap<>();
//                    response.put("message", "Assignment mark deleted");
//                    return ResponseEntity.ok(response);

//                    for(Assignment a: course.getAssignmentList()) {
//                        AssignmentMark assToAdd = assignmentMarkRepository.findByStudentIdAndAssignmentId(studentId,a.getId());
//                        if(assToAdd != null){
//                            studentAssignmentMarks.add(new StudentAssignmentMark(assToAdd.getId(), assToAdd.getMark() , a.getTitle()));
//                        }else{
//                            studentAssignmentMarks.add(new StudentAssignmentMark(a.getId(),a.getTitle()));
//                        }
//                    }


                    assToDelete.setMark(null);
                    assignmentMarkRepository.save(assToDelete);


                    for (Assignment a : course.getAssignmentList()) {

                            AssignmentMark assToAdd = assignmentMarkRepository.findByStudentIdAndAssignmentId(studentId, a.getId());
                            studentAssignmentMarks.add(new StudentAssignmentMark(assToAdd.getId(), assToAdd.getMark(), a.getTitle()));

                    }

                    return studentAssignmentMarks;

                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "assignment doesn't exist");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }
    }

    @PutMapping("/{studentId}/updateStudent")
    public ResponseEntity<Student> updateStudent(@PathVariable("studentId") Long id,
                                                 @RequestBody Map<String, String> credentials) {
        String fullName = credentials.get("fullName");
        String university = credentials.get("university");
        String department = credentials.get("department");

        String email = credentials.get("email");

        String recordNum = credentials.get("recordNum");
        String password = credentials.get("password");


        Student updatedStudent = studentService.updateStudent(id, fullName, recordNum, email, university, department, password);
        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping("/{studentId}/courseList")
    public List<Course> getCoursesByStudentId(@PathVariable Long studentId) {

        Student student = studentRepository.getStudentById(studentId);
        if (student != null) {
            List<Course> courses = student.getCourseList();
            return courses;
        } else {
            return new ArrayList<Course>();
        }
    }


    @GetMapping("/{studentId}/availableCoursesList")
    public List<CourseDTO> getAvailableCourses(@PathVariable Long studentId) {

        Student student = studentRepository.getStudentById(studentId);

        if (student != null) {
            String department = student.getDepartment();
            Secretariat secretariat = secretariatRepository.getSecretariatByDepartment(department);
//            List<Course>  avCourses = secretariat.getCourseList();
//            return avCourses;
            List<CourseDTO> courseDTOS = new ArrayList<>();
            for (Course c : secretariat.getCourseList()) {
                if (!student.getCourseList().contains(c)) {
                    courseDTOS.add(new CourseDTO(c.getId(), c.getName(), c.getYear(), c.getSemester(), c.getProfessor().getFullName()));
                }
            }
            return courseDTOS;
        } else {
            return new ArrayList<CourseDTO>();
        }
    }


    @GetMapping("/{studentId}/courses/assignmentLists")
    public List<Assignment> getAllCoursesAssignmentLists(@PathVariable Long studentId) {

        Student student = studentRepository.getStudentById(studentId);
        List<Assignment> assignments = new ArrayList<>();

        if (student != null) {
            for (Course c : student.getCourseList()) {
                for (Assignment a : c.getAssignmentList()) {
                    assignments.add(a);
                }
            }
            return assignments;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }
    }


    @GetMapping("/{studentId}/course/{courseId}/studyHoursList")
    public List<StudentStudyHours> getStudentsCourseStudyHoursList(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        if (student != null) {
            if (course != null) {
                List<StudyHours> studyHours = studyHoursRepository.findByStudentIdAndCourseId(studentId, courseId);
                if (!studyHours.isEmpty()) {

                    List<StudentStudyHours> stHours = new ArrayList<>();
                    for (StudyHours s : studyHours) {
                        stHours.add(new StudentStudyHours(s.getId(), s.getStart(), s.getHours().toString()));
                    }

                    return stHours;

                } else {
                    return new ArrayList<StudentStudyHours>();
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }
    }


    @PostMapping("/{studentId}/course/{courseId}/studyHours/addStudyHours")
    public List<StudentStudyHours> addStudyHours(@PathVariable Long studentId, @PathVariable Long courseId, @RequestBody Map<String, String> credentials) {
        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String d = credentials.get("date");
        LocalDate date = LocalDate.parse(d, formatter);
        Integer hours = Integer.valueOf(credentials.get("hours"));

        List<StudentStudyHours> stHours = new ArrayList<>();

        if (student != null) {
            if (course != null) {

                StudyHours studyHours = new StudyHours(date, hours);

                student.addStudyHours(studyHours);
                studyHours.setStudent(student);
                course.addStudyHours(studyHours);
                studyHours.setCourse(course);

                studyHoursRepository.save(studyHours);
                courseRepository.save(course);
                studentRepository.save(student);

                for (StudyHours sh : student.getStudyHoursList()) {
                    if (sh.getCourse() == course) {
                        StudentStudyHours studentStudyHours = new StudentStudyHours(sh.getId(),sh.getStart(),sh.getHours().toString());
                        stHours.add(studentStudyHours);
                    }
                }

                return stHours;

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }
    }


    @PostMapping("/{studentId}/course/{courseId}/studyHours/getStudyHours")
    public ResponseEntity<Map<String, Object>> checkIfStudyHoursExist(@PathVariable Long studentId, @PathVariable Long courseId, @RequestBody Map<String, String> credentials) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String d = credentials.get("start");
        LocalDate start = LocalDate.parse(d, formatter);

        StudyHours studyHours = studyHoursRepository.findByStudentIdAndCourseIdAndStart(studentId, courseId, start);

        Map<String, Object> responseBody = new HashMap<>();

        if (studyHours != null) {
            responseBody.put("check", "exists");
            responseBody.put("studyHours", studyHours);
        } else if (studyHours == null) {
            responseBody.put("check", "notHere");
        }
        return ResponseEntity.ok(responseBody);

    }


    @PutMapping("{studentId}/course/{courseId}/studyHours/{studyHoursId}/updateStudyHours")
    public List<StudentStudyHours> updateStudyHours(@PathVariable Long studentId, @PathVariable Long courseId, @PathVariable Long studyHoursId, @RequestBody Map<String, String> credentials) {


        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        Long id = studyHoursId;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String d = credentials.get("start");
        LocalDate start = LocalDate.parse(d, formatter);
        Integer hours = Integer.valueOf(credentials.get("hours"));

        StudyHours studyHours = studyHoursService.updateStudyHours(id, start, hours);

        List<StudentStudyHours> stHours = new ArrayList<>();

        for (StudyHours sh : student.getStudyHoursList()) {
            if (sh.getCourse() == course) {
                StudentStudyHours studentStudyHours = new StudentStudyHours(sh.getId(),sh.getStart(),sh.getHours().toString());
                stHours.add(studentStudyHours);
            }
        }

        return stHours;
//        return ResponseEntity.ok(studyHours);
    }


    @DeleteMapping("{studentId}/course/{courseId}/studyHours/deleteStudyHours")
    public List<StudentStudyHours> deleteStudyHours(@PathVariable Long studentId, @PathVariable Long courseId, @RequestBody Map<String, String> credentials) {

        Long id = Long.valueOf(credentials.get("id"));
        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        StudyHours stHoursToDelete = studyHoursRepository.getStudyHoursByIdAndStudentIdAndCourseId(id, studentId, courseId);

        student.removeStudyHours(stHoursToDelete);
        course.removeStudyHours(stHoursToDelete);
        courseRepository.save(course);
        studentRepository.save(student);

        studyHoursRepository.delete(stHoursToDelete);

        List<StudentStudyHours> stHours = new ArrayList<>();

        for (StudyHours sh : student.getStudyHoursList()) {
            if (sh.getCourse() == course) {
                StudentStudyHours studentStudyHours = new StudentStudyHours(sh.getId(),sh.getStart(),sh.getHours().toString());
                stHours.add(studentStudyHours);
            }
        }

        return stHours;
    }


    @GetMapping("{studentId}/course/{courseId}/ranking")
    public Integer studentRanking(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);


        if (student != null) {
            if (course != null) {

                List<Student> studentsWithCourse = new ArrayList<>();

                //we find the students that have the course
                for (Student s : studentRepository.findAll()) {
                    if (s.getCourseList().contains(course)) {
                        studentsWithCourse.add(s);
                    }
                }

                //calculate the sum of the assignment marks for the assignments in the course
                Map<Student, Float> studentAssignmentMarksSum = new HashMap<>();

                for (Student s : studentsWithCourse) {
                    float assignmentMarksSum = 0;
                    for (AssignmentMark assignmentMark : s.getAssignmentMarkList()) {
                        if(assignmentMark.getAssignment().getCourse().equals(course) && assignmentMark.getMark()!=null){
                            assignmentMarksSum += assignmentMark.getMark() * ((assignmentMark.getAssignment().getPercentage()*1.0)/100.0);
                        }

                    }
                    studentAssignmentMarksSum.put(s, assignmentMarksSum);
                }


                // Sort the students based on the sum of assignment marks in descending order
//                List<Map.Entry<Student, Float>> sortedList = new ArrayList<>(studentAssignmentMarksSum.entrySet());
//                Collections.sort(sortedList, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));


                // Sort the students based on the sum of assignment marks in descending order
               List<Map.Entry<Student, Float>> sortedList = new ArrayList<>(studentAssignmentMarksSum.entrySet());
               sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

//                List<Map.Entry<Student, Float>> sortedList = studentAssignmentMarksSum.entrySet()
//                        .stream()
//                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                        .collect(Collectors.toList());


                int position = -1 ;
                for (int i = 0; i < sortedList.size(); i++) {
                    if (sortedList.get(i).getKey().getId().equals(studentId)) {
                        // Adding 1 to make it readable (1-based index)
                       position = i ;
                        break;
                    }
                }

                position += 1;
               return  position;

//                return sortedList;

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }


    }










}

