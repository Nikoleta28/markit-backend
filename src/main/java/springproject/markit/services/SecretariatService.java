package springproject.markit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import springproject.markit.models.Secretariat;
import springproject.markit.models.Student;
import springproject.markit.repositories.SecretariatRepository;
import springproject.markit.repositories.StudentRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SecretariatService {

    @Autowired
    private SecretariatRepository secretariatRepository;

    public List<Secretariat> getSecretariats(){
        return secretariatRepository.findAll();
    }

    public void createSecretariat(Secretariat secretariat){
        Optional<Secretariat> secretariatOptional = secretariatRepository.findSecretariatByEmail(secretariat.getEmail());
        if(secretariatOptional.isPresent()){
            throw new IllegalStateException("this email is taken");
        }
        secretariatRepository.save(secretariat);
    }

    public void deleteSecretariat(Long id) {
        secretariatRepository.deleteById(id);
    }

    @Transactional
    public Secretariat updateSecretariat(Long id,
                                  String university,
                                  String department,
                                  String email,
                                  String password) {

        //check if the secretariat with the id exists
        Secretariat secretariat = secretariatRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Secretariat with id "+ id +" doesn't exist!"
        ));

        if(university !=null && university.length()> 0 && !Objects.equals(secretariat.getUniversity(),university)){
            secretariat.setUniversity(university);
        }


        if(department != null && !department.isBlank() && !department.equals(secretariat.getDepartment())){
            if(secretariatRepository.findSecretariatByDepartment(department).isPresent()){
                throw new IllegalStateException("This "+ department +" is already taken");
            }
            secretariat.setDepartment(department);
        }

        if(university !=null && university.length()> 0 && !Objects.equals(secretariat.getUniversity(),university)){
            secretariat.setUniversity(university);
        }

        //two secrs can't have the same email
        if(email != null && !email.isBlank() && !email.equals(secretariat.getEmail())){
            if(secretariatRepository.findSecretariatByEmail(email).isPresent()){
                throw new IllegalStateException("Email "+ email +" is already taken");
            }
            secretariat.setEmail(email);
        }

        //password needs to have 8 or more elements
        if(password != null && password.length()>=8 && !Objects.equals(secretariat.getPassword(),password)){
            secretariat.setPassword(password);
        }

        Secretariat updatedSecretariat = secretariatRepository.save(secretariat);

        return updatedSecretariat;
    }

    public Optional<Secretariat> getSecretariatById(Long id) {
        return secretariatRepository.findById(id);
    }


}
