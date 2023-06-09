package springproject.markit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springproject.markit.models.AssignmentMark;
import springproject.markit.services.AssignmentMarkService;

import java.util.List;

@RestController
public class AssignmentMarkController {

    @Autowired
    private AssignmentMarkService assignmentMarkService;

//    @GetMapping("/assignmentmarks")
//    public List<AssignmentMark> getAssignmentMarks(){
//        return assignmentMarkService.getAssignmentMarksList();
//    }

    @PostMapping("/assignmentmarks/newassignmentmark")
    public String createNewAssignmentMark(@RequestBody AssignmentMark assignmentMark){
        assignmentMarkService.createAssignmentMark(assignmentMark);
        return "Assignment mark created!";
    }

    @DeleteMapping("/assignmentmarks/{assignmentmarkId}/deleteassignmentmark")
    public String deleteAssignmentMark(@PathVariable(value = "assignmentmarkId") Long id){
        assignmentMarkService.deleteAssignmentMark(id);
        return "Assignment mark deleted!";
    }

    @PutMapping("/assignmentmarks/{assignmentmarkId}")
    public String updateAssignmentMark(@PathVariable("assignmentmarkId") Long id,
                                @RequestParam(required = false) Float mark){

        assignmentMarkService.updateAssignmentMark(id,mark);
        return "Assignment mark updated!";
    }


}
