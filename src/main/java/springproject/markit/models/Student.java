package springproject.markit.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String department;
    private String university;
    private String email;
    private String password;
    private String recordNum;

    @ManyToMany(cascade = CascadeType.REMOVE)
//    @JsonIgnore
    @JoinTable(
            name = "student_course_list",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courseList = new ArrayList<>();

    @OneToMany(mappedBy = "student")
//    @JsonIgnore
    private List<StudyHours> studyHoursList = new ArrayList<>();

    @OneToMany(mappedBy = "student")
//    @JsonIgnore
    private List<FinalMark> finalMarkList = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<AssignmentMark>  assignmentMarkList = new ArrayList<>();


   //why an empty constructor?
    public Student(){}

    //constructor without id because the db will generate it for me
    public Student(String fullName, String university,String department, String recordNum, String email , String password) {
        this.fullName = fullName;
        this.university = university;
        this.department = department;
        this.recordNum = recordNum;
        this.email= email;
        this.password = password;

    }


    //getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUniversity() {
        return university;
    }
    public void setUniversity(String university) {
        this.university = university;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRecordNum() {
        return recordNum;
    }
    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public List<Course> getCourseList() {
        return courseList;
    }
    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
    public void addCourse(Course course) {
        this.courseList.add(course);
    }

    public void removeCourse(Course course) {
        courseList.remove(course);
    }

    public void removeAllCourses() {
        courseList.clear();
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

