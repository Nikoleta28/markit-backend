package springproject.markit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springproject.markit.models.FinalMark;

import java.util.List;

@Repository
public interface FinalMarkRepository extends JpaRepository<FinalMark, Long> {
    List<FinalMark> findAll();

    FinalMark findByStudentIdAndCourseId(Long studentId, Long courseId);

    FinalMark findByStudentIdAndCourseIdAndMark(Long studentId, Long courseId, Float mark);
}
