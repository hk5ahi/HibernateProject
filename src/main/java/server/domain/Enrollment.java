package server.domain;

import javax.persistence.*;



@Entity
@Table(name = "enrollments")
public class Enrollment {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id",unique = true)
    private Student student;
    @ManyToOne
    @JoinColumn(name = "course_id",unique = true)
    private Course course;


    public Enrollment() {

    }
}
