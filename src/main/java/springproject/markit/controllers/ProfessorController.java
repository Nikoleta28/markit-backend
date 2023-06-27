package springproject.markit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springproject.markit.controllers.entities.CourseDTO;
import springproject.markit.controllers.entities.profAssignment;
import springproject.markit.controllers.entities.profCourseName;
import springproject.markit.models.*;
import springproject.markit.repositories.*;
import springproject.markit.services.AssignmentService;
import springproject.markit.services.ProfessorService;
import springproject.markit.services.SyllabusService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequestMapping("/professor")
@RestController
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    AssignmentMarkRepository assignmentMarkRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AssignmentController assignmentController;

    @Autowired
    private SyllabusRepository syllabusRepository;

    @Autowired
    private SyllabusService syllabusService;

    @Autowired
    private  SyllabusController syllabusController;


    @GetMapping("/allProfessors")
    public List<Professor> getProfessors(){
        return professorService.getProfessors();
    }

    @PostMapping("/newProfessor")
    public String registerNewProfessor(@RequestBody Professor professor){
        professorService.createProfessor(professor);
        return "Professor created!";
    }

    @DeleteMapping("/{professorId}")
    public String deleteProfessor(@PathVariable(value = "professorId") Long id){
        professorService.deleteProfessor(id);
        return "Professor deleted!";
    }

    @GetMapping("/{professorId}")
    public Optional<Professor> getProfessorById(@PathVariable (value = "professorId") Long id ) {
        return professorService.getProfessorById(id);
    }

    @PostMapping("/{professorId}/courses")
    public Professor addCourseToProfessor(@PathVariable Long professorId, @RequestBody Course course) {
        return professorService.addCourseToProfessor(professorId, course);
    }


//    @PutMapping("/professor/{professorId}/updateProfessor")
//    public String updateProfessor(@PathVariable("professorId") Long id,
//                                @RequestParam(required = false) String fullName,
//                                @RequestParam(required = false) String university,
//                                  @RequestParam(required = false) String department,
//                                @RequestParam(required = false) String email,
//                                @RequestParam(required = false) String password){
//
//        professorService.updateProfessor(id,fullName,university,department,email,password);
//        return "Professor updated!";
//    }


    @PutMapping("/{professorId}/updateProfessor")
    public ResponseEntity<Professor> updateProfessor(@PathVariable("professorId") Long id,
                                                         @RequestBody Map<String, String> credentials){
        String fullName = credentials.get("fullName");
//        String university = credentials.get("university");
//        String department = credentials.get("department");;
        String email = credentials.get("email");;
        String password = credentials.get("password");;

        Professor updatedProfessor = professorService.updateProfessor(id,fullName,email,password);
        return ResponseEntity.ok(updatedProfessor);
    }



    @GetMapping("/{professorId}/courseList")
    public List<profCourseName> getCoursesByProfessorId(@PathVariable Long professorId) {
        Optional<Professor> professor = professorService.getProfessorById(professorId);
        if (professor.isPresent()) {
            List<profCourseName> profCourseNames= new ArrayList<>();
            for (Course c: professor.get().getCourseList()){
                profCourseNames.add(new profCourseName(c.getId(),c.getName()));
            }
            return profCourseNames;

        } else {
            return new ArrayList<profCourseName>();
        }
    }

    @GetMapping("/{professorId}/course")
    public  ResponseEntity<Course> getCourse(@PathVariable Long professorId, @RequestParam Long courseId) {
        Long id = Long.valueOf(courseId);
        Professor professor = professorRepository.getProfById(professorId);

        if (professor == null) {
            return ResponseEntity.notFound().build();
        }
        if(professor != null){
            Course  course = courseRepository.getCourseById(id);
            return ResponseEntity.ok(course);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesnt exist");
        }

    }


    @GetMapping("/{professorId}/course/{courseId}/assignmentList")
    public  List<profAssignment> getCourseAssignmentList(@PathVariable Long professorId, @PathVariable Long courseId) {
        Long id = Long.valueOf(courseId);

        Course course = courseRepository.getCourseById(id);
        Professor professor = professorRepository.getProfById(professorId);


        if(professor != null){
            List<profAssignment> profAssignments= new ArrayList<>();
            for (Assignment a: course.getAssignmentList()){
                profAssignments.add(new profAssignment(a.getId(),a.getTitle(), a.getPercentage(), a.getEnd()));
            }
            return profAssignments;

        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesnt exist");
        }

    }



    @PutMapping("/{professorId}/course/{courseId}/assignment/updateAssignment")
    public List<profAssignment> updateAssignment(@PathVariable Long professorId,@PathVariable Long courseId,
                                        @RequestBody Map<String, String> credentials){

        Long id = Long.valueOf(credentials.get("id"));
        String title = credentials.get("title");
        Integer percentage = Integer.valueOf(credentials.get("percentage"));;
        LocalDate end = LocalDate.parse(credentials.get("end"));

        Course course = courseRepository.getCourseById(courseId);
        Professor professor = professorRepository.getProfById(professorId);

        Assignment assignment = assignmentRepository.getAssignmentById(id);

        if (professor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesn't exist");
        }

        if(professor != null){
           if(course != null)
           {
               if(assignment != null) {
                   Assignment updatedAssignment = assignmentService.updateAssignment(id, title, percentage, end);
                   updatedAssignment.setCourse(course);
                   courseRepository.save(course);
                   professorRepository.save(professor);

                   List<profAssignment> profAssignments = new ArrayList<>();
                   for (Assignment a : course.getAssignmentList()) {
                       profAssignments.add(new profAssignment(a.getId(), a.getTitle(), a.getPercentage(), a.getEnd()));
                   }
                   return profAssignments;
               }else{
                   throw new ResponseStatusException(HttpStatus.NOT_FOUND,"assignment doesn't exist");
               }
           }else{
               throw new ResponseStatusException(HttpStatus.NOT_FOUND,"course doesn't exist");
           }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesn't exist");
        }

    }


    @PostMapping("/{professorId}/course/{courseId}/assignments/addAssignment")
    public List<profAssignment> addAssignment(@PathVariable Long professorId, @PathVariable Long courseId,@RequestBody Assignment assignment) {

        Course course = courseRepository.getCourseById(courseId);
        Professor professor = professorRepository.getProfById(professorId);


        if(professor != null){
            if(course != null)
            {
                assignmentController.addNewAssignment(assignment, course);

                List<profAssignment> profAssignments = new ArrayList<>();
                for (Assignment a : course.getAssignmentList()) {
                    profAssignments.add(new profAssignment(a.getId(), a.getTitle(), a.getPercentage(), a.getEnd()));
                }
                return profAssignments;

            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"course doesn't exist");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesn't exist");
        }
    }


    @DeleteMapping("/{professorId}/course/{courseId}/assignments/deleteAssignment")
    public List<profAssignment> deleteAssignment(@PathVariable Long professorId, @PathVariable Long courseId, @RequestBody Map<String, String> credentials ){

        Long id = Long.valueOf(credentials.get("id"));

        Assignment assignmentToDelete = assignmentRepository.getAssignmentById(id);

        Course course = courseRepository.getCourseById(courseId);
        Professor professor = professorRepository.getProfById(professorId);

        if(professor != null){
            if(course != null) {
                if(assignmentToDelete != null) {



                    List<Assignment> assignments =  course.getAssignmentList();
                    if(assignments.contains(assignmentToDelete)) {

                        List<AssignmentMark> allAssignmentMarkList= assignmentMarkRepository.findAll();
                        for (AssignmentMark am: allAssignmentMarkList){
                            if(am.getAssignment().equals(assignmentToDelete)){
                                assignmentMarkRepository.delete(am);
                            }
                        }

                        course.removeAssignment(assignmentToDelete);

                    }




                    assignmentRepository.delete(assignmentToDelete);

                    List<profAssignment> profAssignments = new ArrayList<>();
                    for (Assignment a : course.getAssignmentList()) {
                        profAssignments.add(new profAssignment(a.getId(), a.getTitle(), a.getPercentage(), a.getEnd()));
                    }
                    return profAssignments;

                }else{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"assignment doesn't exist");
                }
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"course doesn't exist");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesn't exist");
        }


    }



    @GetMapping("/{professorId}/courses/assignmentLists")
    public  List<profAssignment> getAllCoursesAssignmentLists(@PathVariable Long professorId) {

        Professor professor = professorRepository.getProfById(professorId);

        List<profAssignment> profAssignments= new ArrayList<>();

        if(professor != null){
          for(Course c : professor.getCourseList() )
          {
              for (Assignment a: c.getAssignmentList()){
                  profAssignments.add(new profAssignment(a.getId(),a.getTitle(), a.getPercentage(), a.getEnd()));
              }
          }
            return profAssignments;

        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesnt exist");
        }

    }





//-------------------------------------------------------------------------

    @GetMapping("/{professorId}/course/{courseId}/syllabusList")
    public  List<Syllabus> getCourseSyllabusList(@PathVariable Long professorId, @PathVariable Long courseId) {
        Long id = Long.valueOf(courseId);

        Course course = courseRepository.getCourseById(id);
        Professor professor = professorRepository.getProfById(professorId);

        if(professor != null){
          if(course != null) {
              List<Syllabus> syllabusList = course.getSyllabusList();
              return syllabusList;
          }else{
              throw new ResponseStatusException(HttpStatus.NOT_FOUND,"course doesnt exist");
          }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesnt exist");
        }

    }



    @PutMapping("/{professorId}/course/{courseId}/syllabus/updateSyllabus")
    public List<Syllabus> updateSyllabus(@PathVariable Long professorId,@PathVariable Long courseId,
                                                 @RequestBody Map<String, String> credentials){

        Long id = Long.valueOf(credentials.get("id"));
        String name = credentials.get("name");
        Integer difficulty = Integer.valueOf(credentials.get("difficulty"));;


        Course course = courseRepository.getCourseById(courseId);
        Professor professor = professorRepository.getProfById(professorId);

        Syllabus syllabus = syllabusRepository.getSyllabusById(id);

        if (professor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesn't exist");
        }

        if(professor != null){
            if(course != null)
            {
                if(syllabus != null) {
                    Syllabus updatedSyllabus = syllabusService.updateSyllabus(id, name , difficulty);
                    updatedSyllabus.setCourse(course);
                    courseRepository.save(course);
                    professorRepository.save(professor);

                    List<Syllabus> syllabusList = course.getSyllabusList();
                    return syllabusList;
                }else{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"assignment doesn't exist");
                }
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"course doesn't exist");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesn't exist");
        }

    }


    @PostMapping("/{professorId}/course/{courseId}/syllabuses/addSyllabus")
    public List<Syllabus> addSyllabus(@PathVariable Long professorId, @PathVariable Long courseId,@RequestBody Syllabus syllabus) {

        Course course = courseRepository.getCourseById(courseId);
        Professor professor = professorRepository.getProfById(professorId);


        if(professor != null){
            if(course != null)
            {
                syllabusController.addNewSyllabus(syllabus, course);

                List<Syllabus> syllabusList = course.getSyllabusList();
                return syllabusList;

            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"course doesn't exist");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesn't exist");
        }
    }


    @DeleteMapping("/{professorId}/course/{courseId}/syllabuses/deleteSyllabus")
    public List<Syllabus> deleteSyllabus(@PathVariable Long professorId, @PathVariable Long courseId, @RequestBody Map<String, String> credentials ){

        Long id = Long.valueOf(credentials.get("id"));

        Syllabus syllabusToDelete = syllabusRepository.getSyllabusById(id);

        Course course = courseRepository.getCourseById(courseId);
        Professor professor = professorRepository.getProfById(professorId);

        if(professor != null){
            if(course != null) {
                if(syllabusToDelete != null) {

                    List<Syllabus> syllabuses =  course.getSyllabusList();
                    if(syllabuses.contains(syllabusToDelete)) {
                        course.removeSyllabus(syllabusToDelete);

                    }

                    syllabusRepository.delete(syllabusToDelete);

                    List<Syllabus> syllabusList = course.getSyllabusList();
                    return syllabusList;

                }else{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,"assignment doesn't exist");
                }
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"course doesn't exist");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesn't exist");
        }


    }




}
