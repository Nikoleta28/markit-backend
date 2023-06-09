package springproject.markit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import springproject.markit.controllers.entities.CourseDTO;
import springproject.markit.models.Assignment;
import springproject.markit.models.Course;
import springproject.markit.models.Professor;
import springproject.markit.models.Secretariat;
import springproject.markit.repositories.AssignmentRepository;
import springproject.markit.repositories.CourseRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Assignment> getAssignments() { return assignmentRepository.findAll();}

//    public void createAssignment(Assignment assignment) {
//        Optional<Assignment> assignmentOptional = assignmentRepository.findAssignmentById(assignment.getId());
//        if(assignmentOptional.isPresent()){
//            throw new IllegalStateException("this assignment already exists");
//        }
//        assignmentRepository.save(assignment);
//    }

    public void deleteAssignment(Long id) { assignmentRepository.deleteById(id);}

    public Assignment updateAssignment(Long id, String title, Integer percentage, LocalDate end) {

        Assignment assignment = assignmentRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Assignment with id "+ id +" doesn't exist!"
        ));

        if(title !=null && title.length()> 0 ){
            assignment.setTitle(title);
        }

        if( percentage instanceof Integer){
            assignment.setPercentage(percentage);
        }

        if(end !=null && !Objects.equals(assignment.getEnd(),end)){
            assignment.setEnd(end);
        }

        assignmentRepository.save(assignment);

        return assignment;
    }


   public void createAssignment(Assignment assignment, Course course) {

       Optional<Assignment> assignmentOptional =  assignmentRepository.findAssignmentByTitle(assignment.getTitle());

       if(course == null) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND,"course doesn't exist");
       }
       else{
           if(assignmentOptional.isPresent()) {
               throw new IllegalStateException("Title taken");
           }else{

               course.addAssignment(assignment);
               assignment.setCourse(course);

               assignmentRepository.save(assignment);
               courseRepository.save(course);


           }
       }

   }


}
