package springproject.markit.services;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springproject.markit.models.Assignment;
import springproject.markit.models.Course;
import springproject.markit.models.Professor;
import springproject.markit.models.Secretariat;
import springproject.markit.repositories.CourseRepository;
import springproject.markit.repositories.ProfessorRepository;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService{

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    ProfessorRepository professorRepository;

    public List<Course> getCourses(){
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public void createCourse(Course course, String profName){
        Optional<Course> courseOptional = courseRepository.findCourseByName(course.getName());
        if(courseOptional.isPresent()) {
            throw new IllegalStateException("name taken");
        }
        Optional<Professor> professorOptional = Optional.ofNullable(professorRepository.findProfessorByFullName(profName));
        if(!professorOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"professor doesnt exist");
        }
        course.setProfessor(professorOptional.get());
        courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public void deleteCourseByName(String name){courseRepository.deleteByName(name);}

    @Transactional
    public Course updateCourse(Long id, String name, Integer year, Integer semester) {


        //check if the student with the id exists
        Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Course with id "+ id +" doesn't exist!"
        ));

        if(name != null && !name.isBlank() && !name.equals(course.getName())){
            if(courseRepository.findCourseByName(name).isPresent()){
                throw new IllegalStateException("The name "+ name +" is already taken");
            }
            course.setName(name);
        }

        if( year instanceof Integer){course.setYear(year);}

        if(semester instanceof Integer){course.setSemester(semester);}

//        Course updatedCourse =
                courseRepository.save(course);

        return course;

    }

//
//    public Course addAssignmentToCourse(Long courseId, Assignment assignment) {
//        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
//        List<Assignment> assignments = course.getAssignmentList();
//        assignments.add(assignment);
//        assignment.setCourse(course);
//        course.setAssignmentList(assignments);
//        return courseRepository.save(course);
//    }
//
//    public Course removeAssignmentFromCourse(Long courseId, Long assignmentId) {
//        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
//        List<Assignment> assignments = course.getAssignmentList();
//        Assignment assignment = assignments.stream().filter(a -> a.getId().equals(assignmentId)).findFirst().orElseThrow(() -> new RuntimeException("Assignment not found"));
//        assignments.remove(assignment);
//        course.setAssignmentList(assignments);
//        return courseRepository.save(course);
//    }


}

