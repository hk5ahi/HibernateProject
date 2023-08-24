package server.domain;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title",length = 10,nullable = false,unique = true)
    private String title;
    @Column(name = "code",nullable = false)
    private String code;
    @Column(name = "description",length = 25,columnDefinition = "TEXT")
    private String description;
    @Column(name ="duration",columnDefinition = "double")
    private double duration;

    public Course() {

    }
}
