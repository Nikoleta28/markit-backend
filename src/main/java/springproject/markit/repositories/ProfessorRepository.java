package springproject.markit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springproject.markit.models.Professor;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProfessorRepository  extends JpaRepository<Professor, Long> {
    Optional<Professor> findProfessorByEmail(String email);

    List<Professor> findAll();

//    List<Professor> getAllProfessors();
    Optional<Professor> findProfessorById(Long id);

    Professor findProfessorByFullName(String name);

    Professor getProfById(Long id);


    List<Professor> findAllByDepartment(String department);
}
