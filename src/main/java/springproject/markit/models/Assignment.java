package springproject.markit.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //name or numOfAs?
    private String title;
    private Integer percentage;
//  private Float mark;
    private LocalDate end;


    @OneToMany(mappedBy = "assignment")
    private List<AssignmentMark> assignmentMarkList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;

    //constructor
    public Assignment(){}

    public Assignment(String title,Integer percentage,LocalDate end) {
        this.title = title;
        this.percentage = percentage;
//        this.mark = mark;
        this.end = end;
    }

    //getters and setters
    public Long getId() { return id;}

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Integer getPercentage() { return percentage; }

    public void setPercentage(Integer percentage) { this.percentage = percentage; }

//    public Float getMark() { return mark; }
//    public void setMark(Float mark) { this.mark = mark; }

    public LocalDate getEnd() { return end; }

    public void setEnd(LocalDate end) { this.end = end; }

    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }


    public List<AssignmentMark> getAssignmentMarkList() {
        return assignmentMarkList;
    }

    public void setAssignmentMarkList(List<AssignmentMark> assignmentMarkList) {
        this.assignmentMarkList = assignmentMarkList;
    }

    public void addAssignmentMark(AssignmentMark assignmentMark) {
        this.assignmentMarkList.add(assignmentMark);

    }


    public void removeAssignmentMark(AssignmentMark assignmentMark) {
        this.assignmentMarkList.remove(assignmentMark);
    }
}
