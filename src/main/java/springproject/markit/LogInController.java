package springproject.markit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springproject.markit.models.Professor;
import springproject.markit.models.Secretariat;
import springproject.markit.models.Student;
import springproject.markit.repositories.ProfessorRepository;
import springproject.markit.repositories.SecretariatRepository;
import springproject.markit.repositories.StudentRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@RequestMapping("/api")
@RestController
public class LogInController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private SecretariatRepository secretariatRepository;


    @PostMapping(path ="/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {

        String email = credentials.get("email");
        String password = credentials.get("password");

        // First, try to authenticate against the student table
        Optional<Student> studentOpt = studentRepository.findStudentByEmail(email);
        if (studentOpt.isPresent()) {
            if (studentOpt.get().getPassword().equals(password)) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("type", "student");
                responseBody.put("studentId", studentOpt.get().getId());
                return ResponseEntity.ok(responseBody);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid password"));
        }

        // If not found in the student table, try the professor table
        Optional<Professor> professorOpt = professorRepository.findProfessorByEmail(email);
        if (professorOpt.isPresent()) {
            if (professorOpt.get().getPassword().equals(password)) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("type", "professor");
                responseBody.put("professorId", professorOpt.get().getId());
                return ResponseEntity.ok(responseBody);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid password"));
        }

        // If not found in the professor table, try the secretariat table
        Optional<Secretariat> secretariatOpt = secretariatRepository.findSecretariatByEmail(email);
        if (secretariatOpt.isPresent()) {
            if (secretariatOpt.get().getPassword().equals(password)) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("type", "secretariat");
                responseBody.put("secretariatId", secretariatOpt.get().getId());
                return ResponseEntity.ok(responseBody);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid password"));
        }


        // If not found in any table, authentication failed
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }


}
