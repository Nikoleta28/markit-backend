package springproject.markit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springproject.markit.models.StudyHours;
import springproject.markit.services.StudyHoursService;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class StudyHoursController {

    @Autowired
    private StudyHoursService studyHoursService;

    @GetMapping("/studyhours/{studyhoursId}")
    public Optional<StudyHours> getStudyHoursById(@PathVariable Long id) {
        return studyHoursService.getStudyHoursById(id);
    }

    @PostMapping("/studyhours/newstudyhour")
    public StudyHours addNewStudyHours(@RequestBody StudyHours studyHours){
        studyHoursService.createStudyHours(studyHours);
        return studyHours;
    }

    @DeleteMapping("/studyhours/{studyhoursId}/deletestudyhour")
    public String deleteStudyHours(@PathVariable(value = "studyhoursId") Long id){
        studyHoursService.deleteStudyHours(id);
        return "Study hours deleted!";
    }

    @PutMapping("/studyhours/{studyhoursId}/updatestudyhours")
    public String updateStudyHours(@PathVariable("studyhoursId") Long id,
                                   @RequestParam(required = false)LocalDate end,
                                   @RequestParam(required = false) Integer hours){

        studyHoursService.updateStudyHours(id,end,hours);
        return "Study hours updated!";
    }

}
