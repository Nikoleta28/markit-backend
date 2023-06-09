package springproject.markit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class StudyHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate start;

    private Integer hours;

    @ManyToOne
    @JoinColumn(name="courseId")
    @JsonIgnore
    private Course course;

    @ManyToOne
    @JoinColumn(name="studentId")
    @JsonIgnore
    private Student student;

//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name="syllabus")
//    private Syllabus syllabus;

   public StudyHours(){}

    public StudyHours(LocalDate start,Integer hours){
        this.start = start;
       this.hours = hours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStart() { return start; }

    public void setStart(LocalDate start) { this.start = start; }

    public Integer getHours() {return hours;}

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

//    public Syllabus getSyllabus() {
//        return syllabus;
//    }
//    public void setSyllabus(Syllabus Syllabus) {
//        this.syllabus = syllabus;
//    }

}
