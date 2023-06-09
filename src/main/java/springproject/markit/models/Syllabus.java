package springproject.markit.models;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Syllabus {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer difficulty;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;


//    @OneToMany(mappedBy = "syllabus")
//    private List<StudyHours> studyHoursList = new ArrayList<>();

    public Syllabus() {
    }

    public Syllabus( String name, Integer difficulty) {

        this.name = name;
        this.difficulty = difficulty;
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

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

//    public List<StudyHours> getStudyHoursList() {return studyHoursList;}
//    public void setStudyHoursList(List<StudyHours> studyHoursList) {
//        this.studyHoursList = studyHoursList;
//    }
//    public void addStudyHours(StudyHours studyHours) {
//        this.studyHoursList.add(studyHours);
//    }

}
