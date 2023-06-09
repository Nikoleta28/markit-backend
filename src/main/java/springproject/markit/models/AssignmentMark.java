package springproject.markit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class AssignmentMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float mark;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assignment")
    @JsonIgnore
    private Assignment assignment;

    public AssignmentMark() {}

    public AssignmentMark(Float mark) {
        this.mark = mark;
    }

    public Long getId() { return id;}
    public void setId(Long id) { this.id = id; }

    public Float getMark() { return mark; }
    public void setMark(Float mark) { this.mark = mark; }

    public Student getStudent() {
        return student;
    }
    public void setStudent( Student student) {
        this.student = student;
    }

    public Assignment getAssignment() {
        return assignment;
    }
    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

}
