package springproject.markit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springproject.markit.models.AssignmentMark;

import java.util.List;

@Repository
public interface AssignmentMarkRepository  extends JpaRepository<AssignmentMark, Long> {
    List<AssignmentMark> findAll();

    AssignmentMark findByStudentIdAndAssignmentId(Long studentId, Long id);

    AssignmentMark findByIdAndMarkAndStudentIdAndAssignmentId(Long id, Float mark, Long studentId, Long id1);

    AssignmentMark getAssignmentMarkById(Long id);

//    AssignmentMark findByStudentIdAndCourseId(Long studentId, Long Id);
}
