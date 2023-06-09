package springproject.markit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springproject.markit.models.*;

import springproject.markit.repositories.CourseRepository;
import springproject.markit.repositories.ProfessorRepository;
import springproject.markit.services.CourseService;
import springproject.markit.services.ProfessorService;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private   ProfessorRepository professorRepository;

    @GetMapping("/courses")
    public List<Course> getCourses(){
        return courseService.getCourses();
    }


    @GetMapping("/{courseId}")
    public Optional<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping("/courses")
    public String addNewCourse(@RequestBody Course course, @RequestParam String profName){
        courseService.createCourse(course,profName);
        return "Course created!";
    }

//    @DeleteMapping("/courses")
//    public String deleteACourse( @RequestBody String name){
//        courseService.deleteCourseByName(name);
//        return "Course deleted!";
//    }

//    @PutMapping("/courses/{courseId}")
//    public String updateCourse(@PathVariable("courseId") Long id,
//                                @RequestParam(required = false) String name){
//
//        courseService.updateCourse(id,name);
//        return "Course updated!";
//    }


    @GetMapping("/course/{courseId}/courseSyllabusList")
    public List<Syllabus> getCourseSyllabuses(@PathVariable (value = "courseId") Long id ) {

        Course course = courseRepository.getCourseById(id);
        List<Syllabus> syllabuses;

        if (course != null) {
            syllabuses = course.getSyllabusList();
        }
        else{
            syllabuses  = new ArrayList<>();
        }

        return syllabuses;
    }

    @GetMapping("/courses/{courseId}")
    public Optional<Course> findCourseById(@PathVariable (value = "courseId") Long id ) {
        return courseService.getCourseById(id);
    }

//    @PostMapping("/{courseId}/assignments")
//    public Course addAssignmentToCourse(@PathVariable Long courseId, @RequestBody Assignment assignment) {
//        return courseService.addAssignmentToCourse(courseId, assignment);
//    }
//
//    @DeleteMapping("/{courseId}/assignments/{assignmentId}")
//    public Course removeAssignmentFromCourse(@PathVariable Long courseId, @PathVariable Long assignmentId) {
//        return courseService.removeAssignmentFromCourse(courseId, assignmentId);
//    }

}
