package springproject.markit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springproject.markit.models.Assignment;
import springproject.markit.models.Course;
import springproject.markit.services.AssignmentService;

import java.util.List;

@RestController
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping("/course/assignments")
    public List<Assignment> getAssignments(){
        return assignmentService.getAssignments();
    }

//    @PostMapping("/course/assignments/newassignment")
//    public String createNewAssignment(@RequestBody Assignment assignment, Course course){
//        assignmentService.createAssignment(assignment);
//        return "Assignment created!";
//    }

//    @DeleteMapping("/course/assignments/{assignmentId}/deleteassignment")
//    public String deleteAssignment(@PathVariable(value = "assignmentId") Long id){
//        assignmentService.deleteAssignment(id);
//        return "Assignment deleted!";
//    }

//    @PutMapping("/course/assignments/{assignmentId}/updateassignment")
//    public String updateAssignment(@PathVariable("assignmentId") Long id,
//                                @RequestParam(required = false) String name,
//                                @RequestParam(required = false) Integer percentage,
//                                @RequestParam(required = false) String date){
//
//        assignmentService.updateAssignment(id,name,percentage,date);
//        return "Assignment updated!";
//    }


    public String addNewAssignment(Assignment assignment, Course course) {
        assignmentService.createAssignment(assignment,course);
        return "Course created!";
    }


}
