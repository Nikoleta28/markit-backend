package springproject.markit.controllers.entities;

import java.time.LocalDate;

public class StudentStudyHours {
    private  Long id;
    private String title;

    private LocalDate start;

    public StudentStudyHours(Long id, LocalDate start, String title) {
        this.id = id;
        this.start = start;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }
}
