package springproject.markit.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String department;
    private String university;
    private String email;
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "professor")
    @JsonIgnore
    private List<Course> courseList = new ArrayList<>();


    public Professor(){}

    //prof constructor
    public Professor(String fullName, String university ,String department, String email , String password) {
        this.fullName = fullName;
        this.university = university;
        this.department = department;
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



}
