package springproject.markit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springproject.markit.models.Student;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findStudentByEmail(String email);

    Optional<Student> findStudentByRecordNum(String recordNum);

    Optional<Student> findStudentById(Long id);


    Student getStudentById(Long studentId);
}
