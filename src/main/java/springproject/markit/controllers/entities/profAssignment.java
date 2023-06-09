package springproject.markit.controllers.entities;

import java.time.LocalDate;

public class profAssignment {

        private Long id;

        private String title;
        private Integer percentage;
        //  private Float mark;
        private LocalDate end;
        public profAssignment(){}

        public profAssignment(Long id,String title,Integer percentage,LocalDate end) {
            this.id = id;
            this.title = title;
            this.percentage = percentage;
            this.end = end;
        }

        //getters and setters
        public Long getId() { return id;}

        public void setId(Long id) { this.id = id; }

        public String getTitle() { return title; }

        public void setTitle(String title) { this.title = title; }

        public Integer getPercentage() { return percentage; }

        public void setPercentage(Integer percentage) { this.percentage = percentage; }
    
        public LocalDate getEnd() { return end; }

        public void setEnd(LocalDate end) { this.end = end; }


}
