package springproject.markit.controllers.entities;

public class StudentAssignmentMarkWithPlacement {

    private Long id;

    private Float mark;

    private String assignmentName;
    private int placement;

    public StudentAssignmentMarkWithPlacement(Long id, Float mark, String assignmentName, int placement) {
        this.id = id;
        this.mark = mark;
        this.assignmentName = assignmentName;
        this.placement = placement;
    }

    public StudentAssignmentMarkWithPlacement(Long id , String assignmentName) {
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

    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }
}
