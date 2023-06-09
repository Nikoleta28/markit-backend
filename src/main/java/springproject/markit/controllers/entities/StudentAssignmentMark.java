package springproject.markit.controllers.entities;

import springproject.markit.models.Assignment;

public class StudentAssignmentMark {

    private Long id;

    private Float mark;

    private String assignmentName;

    public StudentAssignmentMark(Long id, Float mark, String assignmentName) {
        this.id = id;
        this.mark = mark;
        this.assignmentName = assignmentName;
    }

    public StudentAssignmentMark(Long id ,String assignmentName) {
        this.id = id;
        this.assignmentName = assignmentName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMark() {
        return mark;
    }

    public void setMark(Float mark) {
        this.mark = mark;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }
}
