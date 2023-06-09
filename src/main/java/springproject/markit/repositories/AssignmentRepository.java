package springproject.markit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springproject.markit.models.Assignment;
import springproject.markit.models.Secretariat;
import springproject.markit.models.Student;

import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findAssignmentById(Long id);

    Assignment getAssignmentById(Long id);

    Optional<Assignment> findAssignmentByTitle(String title);

    Assignment getAssignmentByTitle(String title);





    Assignment findByCourseIdAndTitle(Long courseId, String assignTitle);
}
