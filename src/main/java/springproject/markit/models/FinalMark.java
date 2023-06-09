package springproject.markit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class FinalMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float mark;
    @ManyToOne
    @JoinColumn(name="courseId")
    @JsonIgnore
    private Course course;

    @ManyToOne
    @JoinColumn(name="studentId")
    @JsonIgnore
    private Student student;

    public FinalMark(){}

    public FinalMark(Float mark){
        this.mark = mark;
    }

    public Long getId() { return id;}
    public void setId(Long id) { this.id = id; }

    public Float getMark() { return mark; }
    public void setMark(Float mark) { this.mark = mark; }

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
}
