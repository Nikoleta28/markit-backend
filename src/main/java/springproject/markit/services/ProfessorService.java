package springproject.markit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springproject.markit.models.Course;
import springproject.markit.models.Professor;
import springproject.markit.repositories.ProfessorRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;


    public List<Professor> getProfessors(){
        return professorRepository.findAll();
    }

    public void createProfessor(Professor professor){
        Optional<Professor> professorOptional = professorRepository.findProfessorByEmail(professor.getEmail());
        if(professorOptional.isPresent()){
            throw new IllegalStateException("this email is taken");
        }
        professorRepository.save(professor);
    }

    public void deleteProfessor(Long id) {
        professorRepository.deleteById(id);
    }

    @Transactional
    public Professor updateProfessor(Long id,
                                     String fullName,
                                     String university,
                                     String department,
                                     String email,
                                     String password) {

        Professor professor = professorRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Professor with id " + id + " doesn't exist!"
        ));

        //if null, if length>0 if the name provided is not the same as the current one
        if (fullName != null && fullName.length() > 0 && !Objects.equals(professor.getFullName(), fullName)) {
            professor.setFullName(fullName);
        }

        if (department != null && fullName.length() > 0 && !Objects.equals(professor.getDepartment(), department)) {
            professor.setDepartment(department);
        }

        if (university != null && fullName.length() > 0 && !Objects.equals(professor.getUniversity(), university)) {
            professor.setUniversity(university);
        }

        //two students can't have the same email
        if (email != null && !email.isBlank() && !email.equals(professor.getEmail())) {
            if (professorRepository.findProfessorByEmail(email).isPresent()) {
                throw new IllegalStateException("Email " + email + " is already taken");
            }
            professor.setEmail(email);
        }

        //password needs to have 8 or more elements
        if (password != null && password.length() >= 8 && !Objects.equals(professor.getPassword(), password)) {
            professor.setPassword(password);
        }

        professorRepository.save(professor);
        return professor;
    }

    public Optional<Professor> getProfessorById(Long id) {return professorRepository.findById(id);}

    public Professor addCourseToProfessor(Long professorId, Course course) {
        Professor professor = professorRepository.findById(professorId).orElseThrow(
                () -> new RuntimeException("Professor not found"));
        List<Course> courses = professor.getCourseList();
        courses.add(course);
        professor.setCourseList(courses);
        return professorRepository.save(professor);
    }


}
