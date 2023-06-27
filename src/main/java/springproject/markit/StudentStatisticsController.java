package springproject.markit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springproject.markit.controllers.entities.StudentAssignmentMark;
import springproject.markit.controllers.entities.StudentAssignmentMarkWithPlacement;
import springproject.markit.controllers.entities.StudentStudyHours;
import springproject.markit.controllers.entities.profCourseName;
import springproject.markit.models.*;
import springproject.markit.repositories.*;
import springproject.markit.services.AssignmentMarkService;
import springproject.markit.services.FinalMarkService;
import springproject.markit.services.StudentService;
import springproject.markit.services.StudyHoursService;

import java.util.*;

@RequestMapping("/statistics")
@RestController
public class StudentStatisticsController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;
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


    @GetMapping("/student/{studentId}/getCourseByName/{name}")
    public ResponseEntity<Course> getCourse(@PathVariable Long studentId, @PathVariable String name) {

//      String name = String.valueOf(courseName);
        Student student = studentRepository.getStudentById(studentId);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        if (student != null) {
            Course course = courseRepository.getCourseByName(name);
            return ResponseEntity.ok(course);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }
    }


    @GetMapping("/student/{studentId}/course/{courseId}/studentsRegisteredToTheCourse")
    public Integer getStudentsWithTheCourse(@PathVariable Long studentId, @PathVariable Long courseId){

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);
        int studentSum =0;

        //we find the students that have the course
        for (Student s : studentRepository.findAll()) {
            if (s.getCourseList().contains(course)) {
                studentSum += 1;
            }
        }
        return studentSum;
    }


    @GetMapping("/student/{studentId}/course/{courseId}/finalMarkRanking")
    public Integer studentRankingFinalMark(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        if (student != null) {
            if (course != null) {

                if(finalMarkRepository.findByStudentIdAndCourseId(studentId,courseId).getMark()!=null)
                {
                    List<Student> studentsWithCourse = new ArrayList<>();

                    //we find the students that have the course
                    for (Student s : studentRepository.findAll()) {
                        if (s.getCourseList().contains(course)) {
                            studentsWithCourse.add(s);
                        }
                    }

                    //calculate the sum of the assignment marks for the assignments in the course
                    Map<Student, Float> studentFinalMarks = new HashMap<>();

                    for (Student s : studentsWithCourse) {
                        FinalMark fm = finalMarkRepository.findByStudentIdAndCourseId(s.getId(), courseId);

                        if (fm.getMark() != null) {
                            Float finalMark = fm.getMark();
                            studentFinalMarks.put(s, finalMark);
                        }
                    }

                   // Sort the students based on the sum of assignment marks in descending order
                        List<Map.Entry<Student, Float>> sortedList = new ArrayList<>(studentFinalMarks.entrySet());
                        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));


//                        int position = -1;
//                        for (int i = 0; i < sortedList.size(); i++) {
//                            if (sortedList.get(i).getKey().getId().equals(studentId)) {
//                                // Adding 1 to make it readable (1-based index)
//                                position = i;
//                                break;
//                            }
//                        }
//                        position += 1;

                    int position = -1;
                    float targetMark = -1.0f; // Target mark for the given studentId
                    for (int i = 0; i < sortedList.size(); i++) {
                        if (sortedList.get(i).getKey().getId().equals(studentId)) {
                            targetMark = sortedList.get(i).getValue();
                            break;
                        }
                    }

                    if (targetMark != -1.0f) {
                        for (int i = 0; i < sortedList.size(); i++) {
                            if (sortedList.get(i).getValue() == targetMark) {
                                // Adding 1 to make it readable (1-based index)
                                position = i;
                                break;
                            }
                        }
                        position += 1;
                    }


                        return position;
                }else {
                    return -1;
                }

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }


    }



    @GetMapping("/student/{studentId}/course/{courseId}/studyHoursDiagram")
    public List<StudyHours> getAllStudyHours(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);


        if (student != null) {
            if (course != null) {

                List<StudyHours> studyHours = studyHoursRepository.findByStudentIdAndCourseId(studentId, courseId);

               if(studyHours!=null){
                   return studyHours;
               }
               else {
                   List<StudyHours> stHours = new ArrayList<>();
                   return stHours;
               }

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }
    }


    @GetMapping("/student/{studentId}/course/{courseId}/AssignmentMarksSum")
    public Float studentAssignmentMarksSum(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        if (student != null) {
            if (course != null) {
                    float assignmentMarksSum = 0;
                    for (AssignmentMark assignmentMark : student.getAssignmentMarkList()) {
                        if (assignmentMark.getAssignment().getCourse().equals(course) && assignmentMark.getMark() != null) {
                            assignmentMarksSum += assignmentMark.getMark() * ((assignmentMark.getAssignment().getPercentage() * 1.0) / 100.0);
                        }
                    }
                    if(assignmentMarksSum!=0){
                        return assignmentMarksSum;
                    }
                    else{
                        assignmentMarksSum = -1;
                        return assignmentMarksSum;
                    }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }
    }


    @GetMapping("/student/{studentId}/course/{courseId}/StudyHoursSum")
    public Integer studentStudyHoursSum(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        if (student != null) {
            if (course != null) {
                int studyHoursSum = 0;
                for (StudyHours studyHours : student.getStudyHoursList()) {
                    if (studyHours.getCourse().equals(course) && studyHours.getHours()!=null) {
                        studyHoursSum += studyHours.getHours();
                    }
                }
                return studyHoursSum;

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }
    }


    @GetMapping("/student/{studentId}/course/{courseId}/StudyHoursSumRanking")
    public Integer studentStudyHoursSumRanking(@PathVariable Long studentId, @PathVariable Long courseId) {

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

                List<StudyHours> studyHours = student.getStudyHoursList();

                //if the course has assignments
                if(!studyHours.isEmpty()) {

                    Map<Student, Integer> studentStudyHoursSum = new HashMap<>();

                        for (Student s : studentsWithCourse) {
                            int studentSum = 0;
                            for (StudyHours sh : s.getStudyHoursList()) {
                                if (sh.getCourse().equals(course) && sh.getHours()!= null && sh.getHours()>0) {
                                    studentSum += sh.getHours();
                                }

                            }
                            studentStudyHoursSum.put(s, studentSum);
                        }


                        // Sort the students based on the sum of assignment marks in descending order
                        List<Map.Entry<Student, Integer>> sortedList = new ArrayList<>(studentStudyHoursSum.entrySet());
                        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));


//                        int position = -1;
//                        for (int i = 0; i < sortedList.size(); i++) {
//                            if (sortedList.get(i).getKey().getId().equals(studentId)) {
//                                // Adding 1 to make it readable (1-based index)
//                                position = i;
//                                break;
//                            }
//                        }
//                        position += 1;

                    int position = -1;
                    int targetSum = -1; // Target mark for the given studentId
                    for (int i = 0; i < sortedList.size(); i++) {
                        if (sortedList.get(i).getKey().getId().equals(studentId)) {
                            targetSum = sortedList.get(i).getValue();
                            break;
                        }
                    }

                    if (targetSum != -1) {
                        for (int i = 0; i < sortedList.size(); i++) {
                            if (sortedList.get(i).getValue() == targetSum) {
                                // Adding 1 to make it readable (1-based index)
                                position = i;
                                break;
                            }
                        }
                        position += 1;
                    }



                        return position;


                } else {
                    return 0;
                }

//                return sortedList;

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }

    }



    @GetMapping("/student/{studentId}/course/{courseId}/StudyHoursAverage")
    public Integer studentStudyHoursAverage(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        List<Student> studentsWithCourse = new ArrayList<>();

        //we find the students that have the course
        for (Student s : studentRepository.findAll()) {
            if (s.getCourseList().contains(course)) {
                studentsWithCourse.add(s);
            }
        }

        int studentSum = 0;
        int studyHoursSum = 0;

        for(Student st: studentsWithCourse){
            List<FinalMark> finalMarks = st.getFinalMarkList();
           for(FinalMark f: finalMarks){
               if(f.getCourse().equals(course) && f.getMark()!=null && f.getMark()>=5){
                   List<StudyHours> sh = st.getStudyHoursList();
                   if(!sh.isEmpty()){
                       studentSum+=1;
                       for(StudyHours studyHours:sh){
                           if(studyHours.getHours()!=null && studyHours.getCourse().equals(course)){
                               studyHoursSum+= studyHours.getHours();
                           }
                       }
                   }
               }
           }
        }

        Integer averageStudyHours=0;

        if(studentSum!=0){
           averageStudyHours  = studyHoursSum/studentSum;
        }

        return averageStudyHours;
    }




    @GetMapping("/student/{studentId}/course/{courseId}/StudyHoursMAX")
    public Integer studentStudyHoursMAX(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        List<Student> studentsWithCourse = new ArrayList<>();

        // Find the students that have the course
        for (Student s : studentRepository.findAll()) {
            if (s.getCourseList().contains(course)) {
                studentsWithCourse.add(s);
            }
        }

        // Calculate the sum of study hours for each student
        Map<Student, Integer> studentStudyHoursSum = new HashMap<>();

        for (Student s : studentsWithCourse) {
            int studyHoursSum = 0;
            List<StudyHours> sh = s.getStudyHoursList();
            if (!sh.isEmpty()) {
                for (StudyHours studyHours : sh) {
                    if (studyHours.getCourse().equals(course) && studyHours.getHours() != null) {
                        studyHoursSum += studyHours.getHours();
                    }
                }
            }
            studentStudyHoursSum.put(s, studyHoursSum);
        }

        // Sort the students based on the sum of study hours in descending order
        List<Map.Entry<Student, Integer>> sortedList = new ArrayList<>(studentStudyHoursSum.entrySet());
        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        int studyHoursMAX = -1;

        if (!sortedList.isEmpty()) {
            Map.Entry<Student, Integer> firstEntry = sortedList.get(0);
            Student firstStudent = firstEntry.getKey();
            studyHoursMAX = firstEntry.getValue();
        }

        return studyHoursMAX;
    }


    @GetMapping("/student/{studentId}/course/{courseId}/StudyHoursMIN")
    public Integer studentStudyHoursMIN(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);

        List<Student> studentsWithCourse = new ArrayList<>();

        //we find the students that have the course
        for (Student s : studentRepository.findAll()) {
            if (s.getCourseList().contains(course)) {
                studentsWithCourse.add(s);
            }
        }

        //calculate the sum of the assignment marks for the assignments in the course
        Map<Student, Integer> studentStudyHoursSum = new HashMap<>();



        for (Student s : studentsWithCourse) {
            int studyHoursSum = 0;
            List<StudyHours> sh = s.getStudyHoursList();
            if (!sh.isEmpty()) {
                for (StudyHours studyHours : sh) {
                    if (studyHours.getCourse().equals(course) && studyHours.getHours() != null) {
                        studyHoursSum += studyHours.getHours();
                    }
                }
            }
            studentStudyHoursSum.put(s, studyHoursSum);
        }



        // Sort the students based on the sum of study hours in ascending order
        List<Map.Entry<Student, Integer>> sortedList = new ArrayList<>(studentStudyHoursSum.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());

        int studyHoursMIN = -1;
        int lastIndex = sortedList.size() - 1;



        if (lastIndex >= 0) {
            for (Map.Entry<Student, Integer> entry : sortedList) {
                int value = entry.getValue();
                if (value != 0) {
                    studyHoursMIN = value;
                    break;
                }
            }
        }

            return studyHoursMIN;
    }




    @GetMapping("/student/{studentId}/course/{courseId}/ranking")
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

                List<Assignment> assignments = course.getAssignmentList();

                //if the course has assignments
                if(!assignments.isEmpty()) {
                    List<AssignmentMark> studentAssignmentMarks = new ArrayList<>();

                    for (Assignment a : assignments) {
                        AssignmentMark assToAdd = assignmentMarkRepository.findByStudentIdAndAssignmentId(studentId, a.getId());
                        if (assToAdd.getMark() != null) {
                            studentAssignmentMarks.add(assToAdd);
                        }
                    }

                    if (!studentAssignmentMarks.isEmpty()) {
                        //calculate the sum of the assignment marks for the assignments in the course
                        Map<Student, Float> studentAssignmentMarksSum = new HashMap<>();

                        for (Student s : studentsWithCourse) {
                            float assignmentMarksSum = 0;
                            for (AssignmentMark assignmentMark : s.getAssignmentMarkList()) {
                                if (assignmentMark.getAssignment().getCourse().equals(course) && assignmentMark.getMark() != null) {
                                    assignmentMarksSum += assignmentMark.getMark() * ((assignmentMark.getAssignment().getPercentage() * 1.0) / 100.0);
                                }

                            }
                            studentAssignmentMarksSum.put(s, assignmentMarksSum);
                        }


                        // Sort the students based on the sum of assignment marks in descending order
                        List<Map.Entry<Student, Float>> sortedList = new ArrayList<>(studentAssignmentMarksSum.entrySet());
                        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));


                        int position = -1;
                        for (int i = 0; i < sortedList.size(); i++) {
                            if (sortedList.get(i).getKey().getId().equals(studentId)) {
                                // Adding 1 to make it readable (1-based index)
                                position = i;
                                break;
                            }
                        }
                        position += 1;
                        return position;

                    } else {
                        return -1;
                    }
                } else {
                    return 0;
                }

//                return sortedList;

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }


    }


    @GetMapping("/student/{studentId}/course/{courseId}/assignmentMarkList")
    public List<StudentAssignmentMarkWithPlacement> getCourseAssignmentMarkList(@PathVariable Long studentId, @PathVariable Long courseId) {

        Student student = studentRepository.getStudentById(studentId);
        Course course = courseRepository.getCourseById(courseId);



        if (student != null) {
            if (course != null) {
                List<StudentAssignmentMarkWithPlacement> studentAssignmentMarks = new ArrayList<>();

                // Retrieve the assignments for the course
                List<Assignment> assignments = course.getAssignmentList();

                //we find the students that have the course
                List<Student> studentsWithCourse = new ArrayList<>();

                for (Student s : studentRepository.findAll()) {
                    if (s.getCourseList().contains(course)) {
                        studentsWithCourse.add(s);
                    }
                }

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




                    Map<Student, Float> studentsWithAssMark = new HashMap<>();

                    for (Student s : studentsWithCourse) {
                        for (AssignmentMark am : s.getAssignmentMarkList()) {
                            if (am.getAssignment().getCourse().equals(course) && am.getMark() != null) {
                                studentsWithAssMark.put(s, am.getMark());
                            }
                        }
                    }

                    List<Map.Entry<Student, Float>> sortedList = new ArrayList<>(studentsWithAssMark.entrySet());
                    sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

//                    float targetNumber = assignmentMark.getMark();
                    int placement = 0;

                    if(assignmentMark.getMark()!=null) {
                        for (int i = 0; i < sortedList.size(); i++) {
                            if (sortedList.get(i).getValue() == assignmentMark.getMark()) {
                                placement = i;
                                break;
                            }
                        }

                        placement += 1;
                    }

                    studentAssignmentMarks.add(new StudentAssignmentMarkWithPlacement(assignmentMark.getId(), assignmentMark.getMark(), assignment.getTitle(), placement));

                }

                return studentAssignmentMarks;

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course doesn't exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student doesn't exist");
        }

    }








}
