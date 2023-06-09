package springproject.markit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springproject.markit.models.Assignment;
import springproject.markit.models.Course;
import springproject.markit.models.Syllabus;
import springproject.markit.services.SyllabusService;

import java.util.List;

@RestController
public class SyllabusController {

    @Autowired
    private SyllabusService syllabusService;

    @GetMapping("/course/syllabuses")
    public List<Syllabus> getSyllabuses(){
        return syllabusService.getSyllabuses();
    }


    public String addNewSyllabus(Syllabus syllabus, Course course) {
        syllabusService.createSyllabus(syllabus,course);
        return "Course created!";
    }


}
