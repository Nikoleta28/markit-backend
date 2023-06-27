package springproject.markit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springproject.markit.models.Course;


import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findCourseByName(String name);

    Optional<Course> findCourseById(Long id);

    Course getCourseById(Long id);

    void deleteById(Long id);

     void deleteByName(String name);


    Course getCourseByName(String name);


}
