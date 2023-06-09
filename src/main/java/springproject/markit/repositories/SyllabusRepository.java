package springproject.markit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springproject.markit.models.Assignment;
import springproject.markit.models.Course;
import springproject.markit.models.Syllabus;

import java.util.List;
import java.util.Optional;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {

    Optional<Syllabus> findSyllabusByName(String name);

    Optional<Syllabus> findSyllabusById(Long id);

    Syllabus getSyllabusById(Long id);

//    Syllabus findSyllabusByCourseIdAndName(Long courseId, String name);

    Syllabus getSyllabusByCourseIdAndName(Long courseId, String name);

    Syllabus getSyllabusByIdAndCourseId(Long id , Long courseId);

    Syllabus getSyllabusByIdAndCourse(Long courseId, Course course);
}
