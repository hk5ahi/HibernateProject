package server.domain;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Getter
    @ManyToOne
    @JoinColumn(name = "student_id",unique = true)
    private Student student;

    @Getter
    @ManyToOne
    @JoinColumn(name = "course_id",unique = true)
    private Course course;


    public Enrollment() {

    }

}
