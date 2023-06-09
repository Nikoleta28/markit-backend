package springproject.markit.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Secretariat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String department;
    private String university;
    private String email;
    private String password;

    //one secretariat can have many courses
    //The mappedBy property is what we use to tell Hibernate which variable
     //we are using to represent the parent class in our child class.
    @OneToMany
    @JsonIgnore
    private List<Course> courseList = new ArrayList<>();

    public Secretariat(){}

    //constructor
    public Secretariat( String university,String department,String email,String password) {
        this.university = university;
        this.department = department;
        this.email = email;
        this.password = password;
    }

   //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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



}
