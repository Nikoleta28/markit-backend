package springproject.markit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springproject.markit.models.AssignmentMark;
import springproject.markit.models.Course;
import springproject.markit.models.Student;
import springproject.markit.repositories.StudentRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    //we check if the email exists,if not save the student to db
    public void createStudent(Student student){
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if(studentOptional.isPresent()){
            throw new IllegalStateException("this email is taken");
        }

        Optional<Student> studentOptional2 = studentRepository.findStudentByRecordNum(student.getRecordNum());
        if(studentOptional2.isPresent()){
            throw new IllegalStateException("this recordNum is taken");
        }

        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }


    @Transactional
    public Student updateStudent(Long id,
                                 String fullName,
                                 String recordNum,
                                 String email,
//                                 String department,
//                                 String university,
                                 String password
                                 ) {

        //check if the student with the id exists
        Student student = studentRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Student with id "+ id +" doesn't exist!"
        ));

        //if null, if length>0 if the name provided is not the same as the current one
        if(fullName !=null && fullName.length()> 0 && !Objects.equals(student.getFullName(),fullName)){
              student.setFullName(fullName);
        }

//        if(department !=null && fullName.length()> 0 && !Objects.equals(student.getDepartment(),department)){
//            student.setDepartment(department);
//        }
//
//        if(university !=null && fullName.length()> 0 && !Objects.equals(student.getUniversity(),university)){
//            student.setUniversity(university);
//        }

        //two students can't have the same email
        if(email != null && !email.isBlank() && !email.equals(student.getEmail())){
            if(studentRepository.findStudentByEmail(email).isPresent()){
                throw new IllegalStateException("Email "+ email +" is already taken");
            }
            student.setEmail(email);
        }

        //password needs to have 8 or more elements
        if(password != null && password.length()>=8 && !Objects.equals(student.getPassword(),password)){
            student.setPassword(password);
        }

        //two students can't have the same record number
        if(recordNum != null && !recordNum.isBlank() && !recordNum.equals(student.getRecordNum())){
            if(studentRepository.findStudentByRecordNum(recordNum).isPresent()){
                throw new IllegalStateException("Record number "+ recordNum +" is already taken");
            }
            student.setRecordNum(recordNum);
        }

        return student;
    }


    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByEmail(String email)
    {
        return studentRepository.findStudentByEmail(email);
    }

//    public Student addCourseToStudent(Long studentId, Course course) {
//        Student student = studentRepository.findById(studentId).orElseThrow(
//                () -> new RuntimeException("Student not found"));
//        List<Course> courses = student.getCourseList();
//        courses.add(course);
//        student.setCourseList(courses);
//        return studentRepository.save(student);
//    }

    public Student addAssignmentMarkToStudent(Long studentId, AssignmentMark assignmentMark) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new RuntimeException("Student not found"));
        List<AssignmentMark> assignmentMarks = student.getAssignmentMarkList();
        assignmentMarks.add(assignmentMark);
        student.setAssignmentMarkList(assignmentMarks);
        return studentRepository.save(student);
    }


//    //get the courseList based on the student id
//    public List<Course> getCoursesByStudentId(Long id) {
//        Student student = studentRepository.findById(id).orElseThrow(
//                () -> new IllegalStateException(
//                        "Student with id:"+ id +" doesn't exist"
//                ));
//        return student.getCourseList();
//    }
}
