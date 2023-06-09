package springproject.markit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springproject.markit.models.FinalMark;
import springproject.markit.services.FinalMarkService;

import java.util.List;

@RestController
public class FinalMarkController {

    @Autowired
    private FinalMarkService finalMarkService;

    @GetMapping("/finalmarks")
    public List<FinalMark> getFinalMarks(){
        return finalMarkService.getFinalMarksList();
    }

//    @PostMapping("/finalmarks/{studentId}/{courseId}/newfinalmark")
//    public String createNewFinalMark(@PathVariable String studentId,
//                                          @PathVariable String courseId,
//                                          @RequestBody FinalMark finalMark){
//        finalMarkService.createFinalMark(studentId,courseId,finalMark);
//        return "Final mark created!";
//    }

    @DeleteMapping("/finalmarks/{finalmarkId}/deletefinalmark")
    public String deleteFinalMark(@PathVariable(value = "finalmarkId") Long id){
        finalMarkService.deleteFinalMark(id);
        return "Final mark deleted!";
    }

    @PutMapping("/finalmarks/{finalmarkId}/updatefinalmark")
    public String updateFinalMark(@PathVariable("finalmarkId") Long id,
                                       @RequestParam(required = false) Float mark){

        finalMarkService.updateFinalMark(id,mark);
        return "Final mark updated!";
    }

}
