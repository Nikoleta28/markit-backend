package springproject.markit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springproject.markit.controllers.entities.CourseDTO;
import springproject.markit.models.*;
import springproject.markit.repositories.*;
import springproject.markit.services.CourseService;
import springproject.markit.services.ProfessorService;
import springproject.markit.services.SecretariatService;
import springproject.markit.services.StudentService;

import java.util.*;

@RequestMapping("/secretariat")
@RestController
public class SecretariatController {

    @Autowired
    private SecretariatService secretariatService;

    @Autowired
    private SecretariatRepository secretariatRepository;

    @Autowired
    private CourseController courseController;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private FinalMarkRepository finalMarkRepository;

    @Autowired
    private AssignmentMarkRepository assignmentMarkRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SyllabusRepository syllabusRepository;

    @Autowired
    private StudyHoursRepository studyHoursRepository;

    @GetMapping("/allSecretariats")
    public List<Secretariat> getSecretariat(){
        return secretariatService.getSecretariats();
    }

    @PostMapping("/newSecretariat")
    public String registerNewSecretariat(@RequestBody Secretariat secretariat){
        secretariatService.createSecretariat(secretariat);
        return "Secretariat created!";
    }

    @DeleteMapping("/{secretariatId}")
    public String deleteSecretariat(@PathVariable(value = "secretariatId") Long id){
        secretariatService.deleteSecretariat(id);
        return "Secretariat deleted!";
    }


    @GetMapping("/{secretariatId}")
    public Optional<Secretariat> getSecretariatById(@PathVariable (value = "secretariatId") Long id ) {
        return secretariatService.getSecretariatById(id);
    }

    @GetMapping("/universities")
    public ArrayList<String> getSecrUniversities() {
        ArrayList<String> universities = new ArrayList<>();

        for (Secretariat s : secretariatRepository.findAll()) {
            universities.add(s.getUniversity());
        }

        return universities;
    }

    @GetMapping("/departments")
    public ArrayList<String> getSecrDepartments() {
        ArrayList<String> departments = new ArrayList<>();

        for (Secretariat s : secretariatRepository.findAll()) {
            departments.add(s.getDepartment());
        }
        return departments;
    }


    @GetMapping("/{secretariatId}/courseList")
    public List<CourseDTO> getCoursesBySecretariatId(@PathVariable Long secretariatId) {
        Optional<Secretariat> secretariat = secretariatService.getSecretariatById(secretariatId);
        if (secretariat.isPresent()) {
            List<CourseDTO> courseDTOS= new ArrayList<>();
            for (Course c: secretariat.get().getCourseList()){
                courseDTOS.add(new CourseDTO(c.getId(),c.getName(), c.getYear(), c.getSemester(), c.getProfessor().getFullName()));
            }
            return courseDTOS;
        } else {
            return new ArrayList<CourseDTO>();
        }
    }


    @GetMapping("/{secretariatId}/professorsList")
    public List<String> getDeptProfessors(@PathVariable Long secretariatId) {
       Secretariat secretariat = secretariatRepository.findSecretariatById(secretariatId);
        if (secretariat!= null) {
            List<Professor> allProfs= professorRepository.findAllByDepartment(secretariat.getDepartment());
            List<String> deptProfs= new ArrayList<>();
            for (Professor p: allProfs){
                deptProfs.add(p.getFullName());
            }
            return deptProfs;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"secretariat doesn't exist");
        }
    }


    @PutMapping("/{secretariatId}/updateSecretariat")
    public ResponseEntity<Secretariat> updateSecretariat(@PathVariable("secretariatId") Long id,
                                                         @RequestBody Map<String, String> credentials){

         String university = credentials.get("university");
         String department = credentials.get("department");;
         String email = credentials.get("email");;
         String password = credentials.get("password");;

        Secretariat updatedSecretariat = secretariatService.updateSecretariat(id,university,department,email,password);
        return ResponseEntity.ok(updatedSecretariat);
    }

    @PostMapping("/{secretariatId}/courses/addCourse")
    public List<CourseDTO> addCourse(@PathVariable Long secretariatId, @RequestBody Course course, @RequestParam String profName) {


        Secretariat secretariat = secretariatRepository.findById(secretariatId).orElse(null);

        if (secretariat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"secretariat doesnt exist");
        }

        courseController.addNewCourse(course, profName);

        secretariat.getCourseList().add(course);
        secretariatRepository.save(secretariat);

        List<CourseDTO> courseDTOS= new ArrayList<>();
        for (Course c: secretariat.getCourseList()){
            courseDTOS.add(new CourseDTO(c.getId(),c.getName(), c.getYear(), c.getSemester(), c.getProfessor().getFullName()));
        }
        return courseDTOS;
    }


    @DeleteMapping("/{secretariatId}/courses/deleteCourse")
    public List<CourseDTO> deleteCourse(@PathVariable Long secretariatId,  @RequestBody Map<String, String> credentials ){

        Long courseId = Long.valueOf(credentials.get("id"));
        Secretariat secretariat = secretariatRepository.findById(secretariatId).orElse(null);

        if (secretariat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"secretariat doesn't exist");
        }

        Course courseToDelete =  courseRepository.getCourseById(courseId);

        if (courseToDelete!=null) {


            for (Student s: studentService.getStudents()){
              List<Course> studentCourseList =  s.getCourseList();
                if(studentCourseList.contains(courseToDelete)){

//                 List<FinalMark> fm = courseToDelete.getFinalMarkList();
//                 if(!fm.isEmpty()) {
//                     for (FinalMark f : fm) {
//                         if (s.getFinalMarkList().contains(f)) {
//                             s.getFinalMarkList().remove(f);
//                             fm.remove(f);
//                             finalMarkRepository.delete(f);
//                         }
//                     }
//                 }

                    List<Assignment> assignments = courseToDelete.getAssignmentList();
                    for(Assignment as : assignments){
                        if(!assignments.isEmpty()) {
                            List<AssignmentMark> assignmentMarks = as.getAssignmentMarkList();
                            if (!assignmentMarks.isEmpty()) {
                                for (AssignmentMark am : assignmentMarks) {
                                    if (s.getAssignmentMarkList().contains(am)) {
                                        s.getAssignmentMarkList().remove(am);
                                        as.removeAssignmentMark(am);
                                        assignmentMarkRepository.delete(am);
                                    }

                                }
                            }
                        }
                        courseToDelete.getAssignmentList().remove(as);
                        assignmentRepository.delete(as);
                    }


                   FinalMark studentFinalMark = finalMarkRepository.findByStudentIdAndCourseId(s.getId(),courseToDelete.getId());
                   if(studentFinalMark!=null){
                       s.getFinalMarkList().remove(studentFinalMark);
                       finalMarkRepository.delete(studentFinalMark);
                   }

                   List<StudyHours> stHours = s.getStudyHoursList();
                   if(!stHours.isEmpty()){
                       for(StudyHours sh: stHours ){
                           if(sh.getCourse() == courseToDelete){
                               stHours.remove(sh);
                               courseToDelete.removeStudyHours(sh);
                               studyHoursRepository.delete(sh);
                           }
                       }
                   }



                    s.removeCourse(courseToDelete);
                }
            }


            for (Professor p: professorService.getProfessors()){
                List<Course> profCourseList =  p.getCourseList();
                if(profCourseList.contains(courseToDelete)){

                    List<Assignment> assignments = courseToDelete.getAssignmentList();
                    if(!assignments.isEmpty()) {
                      for(Assignment as : assignments){
                            courseToDelete.removeAssignment(as);
                            assignmentRepository.delete(as);
                        }
                    }

                    List<Syllabus> sylls = courseToDelete.getSyllabusList();
                    if(!sylls.isEmpty()) {
                       for(Syllabus syl : sylls){
                            courseToDelete.removeSyllabus(syl);
                            syllabusRepository.delete(syl);
                        }
                    }


                    p.removeCourse(courseToDelete);
                }
            }

            secretariat.getCourseList().remove(courseToDelete);
            courseRepository.delete(courseToDelete);
        }

        secretariatRepository.save(secretariat);

        List<CourseDTO> courseDTOS= new ArrayList<>();
        for (Course c: secretariat.getCourseList()){
            courseDTOS.add(new CourseDTO(c.getId(),c.getName(), c.getYear(), c.getSemester(), c.getProfessor().getFullName()));
        }

        return courseDTOS;

//        return ResponseEntity.ok().body("Course deleted successfully");
    }

    @PutMapping("/{secretariatId}/courses/updateCourse")
    public List<CourseDTO> updateCourse(@PathVariable Long secretariatId,
                                               @RequestBody Map<String, String> credentials){

        Long id = Long.valueOf(credentials.get("id"));
        String name = credentials.get("name");
        Integer year = Integer.valueOf(credentials.get("year"));;
        Integer semester = Integer.valueOf(credentials.get("semester"));;
        String profName = credentials.get("professor");

        Secretariat secretariat = secretariatRepository.findById(secretariatId).orElse(null);

        if (secretariat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"secretariat doesn't exist");
        }

//        Course updatedCourse = new Course();

        Professor professor = professorRepository.findProfessorByFullName(profName);

        if(professor != null){
          Course  updatedCourse = courseService.updateCourse(id,name,year,semester);
            updatedCourse.setProfessor(professor);
            secretariatRepository.save(secretariat);

//            return ResponseEntity.ok(updatedCourse);
            List<CourseDTO> courseDTOS= new ArrayList<>();
            for (Course c: secretariat.getCourseList()){
                courseDTOS.add(new CourseDTO(c.getId(),c.getName(), c.getYear(), c.getSemester(), c.getProfessor().getFullName()));
            }

            return courseDTOS;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesn't exist");
        }



    }







}
