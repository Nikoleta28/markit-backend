package springproject.markit.controllers.entities;

public class profCourseName {
    private Long id;
    private String name;

    public profCourseName(Long id,String name) {
        this.id = id;
        this.name = name;

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
}
