package springproject.markit.controllers.entities;

import springproject.markit.models.Professor;

public class CourseDTO {

    private Long id;
    private String name;
    private Integer year;
    private Integer semester;
    private String professor;

    public CourseDTO(Long id,String name, Integer year, Integer semester, String professor) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.semester = semester;
        this.professor = professor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getSemester() {
        return semester;
    }

    public String getProfessor() {
        return professor;
    }
}
