package springproject.markit.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//we create a table named courseTable
@Entity
public class Course {

    //GeneratedValue annotation is to configure the way of increment of the specified column(field).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //one course can have many assignments
    @OneToMany(cascade = CascadeType.ALL)
    private List<Assignment> assignmentList = new ArrayList<>();

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<Syllabus> syllabusList = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<FinalMark> finalMarkList = new ArrayList<>();

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<StudyHours> studyHoursList = new ArrayList<>();


    private String name;

    private Integer year;
    private Integer semester;


//    //one course can have many students
//    //using a @OneToMany mapping,  mappedBy parameter: the given column is owned by another entity(course):
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
//    @JsonIgnore
//    private List<Student> students = new ArrayList<>();


    //one course can have many professors
    //courses joined in a column by the professor?
    @ManyToOne
    @JoinColumn(name="professorId")
    @JsonIgnore
    private Professor professor;

    public Course() {}

    //why do we only use the name in the constructor?
    public Course(String name,Integer year,Integer semester) {
        this.name = name;
        this.year = year;
        this.semester = semester;
    }


    //getters and setters

    public Professor getProfessor() {
        return professor;
    }
    public void setProfessor(Professor professor) {
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

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<Assignment> courseList) {
        this.assignmentList = assignmentList;
    }

    public void addAssignment(Assignment assignment) {
        this.assignmentList.add(assignment);

    }

    public void removeAssignment(Assignment assignment) {
        this.assignmentList.remove(assignment);
    }



    public List<Syllabus> getSyllabusList() {
        return syllabusList;
    }

    public void setSyllabusList(List<Syllabus> courseList) {
        this.syllabusList = syllabusList;
    }

    public void addSyllabus(Syllabus syllabus) {
        this.syllabusList.add(syllabus);

    }

    public void removeSyllabus(Syllabus syllabus) {
        this.syllabusList.remove(syllabus);
    }

    public List<StudyHours> getStudyHoursList() {return studyHoursList;}
    public void setStudyHoursList(List<StudyHours> studyHoursList) {
        this.studyHoursList = studyHoursList;
    }
    public void addStudyHours(StudyHours studyHours) {
        this.studyHoursList.add(studyHours);
    }
    public void removeStudyHours(StudyHours studyHours) {
        this.studyHoursList.remove(studyHours);
    }
    public List<FinalMark> getFinalMarkList() {
        return finalMarkList;
    }
    public void setFinalMarkList(List<FinalMark> finalMarkList) {
        this.finalMarkList = finalMarkList;
    }
    public void addFinalMark(FinalMark finalMark) {
        this.finalMarkList.add(finalMark);
    }


    public void removeFinalMark(FinalMark finalMark) {
        this.finalMarkList.remove(finalMark);
    }
}
