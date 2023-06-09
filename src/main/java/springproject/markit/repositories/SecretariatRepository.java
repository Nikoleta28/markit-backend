package springproject.markit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springproject.markit.models.Course;
import springproject.markit.models.Secretariat;
import springproject.markit.models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SecretariatRepository extends JpaRepository<Secretariat, Long> {

    Optional<Secretariat> findSecretariatByEmail(String email);


    Optional<Object> findSecretariatByDepartment(String department);


    Secretariat getSecretariatByDepartment(String department);

    Secretariat findSecretariatById(Long secretariatId);
}
