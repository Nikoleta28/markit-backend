package springproject.markit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springproject.markit.models.Course;
import springproject.markit.models.StudyHours;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudyHoursRepository extends JpaRepository<StudyHours, Long> {
    Optional<StudyHours> findById(Long id);

    List<StudyHours> findByStudentIdAndCourseId(Long studentId, Long courseId);


    StudyHours findByStudentIdAndCourseIdAndStart(Long studentId, Long courseId, LocalDate start);

    StudyHours getStudyHoursByIdAndStudentIdAndCourseId(Long id, Long studentId, Long courseId);
}
