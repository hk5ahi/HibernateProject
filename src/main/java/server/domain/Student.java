package server.domain;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setAge(int age) {
        this.age = age;
    }

    @Getter
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Getter
    @Column(name = "firstname", length = 10)
    private String firstName;

    @Getter
    @Column(name = "lastname", length = 5)
    private String lastName;

    @Getter
    @Column(name = "email", length = 50, columnDefinition = "VARCHAR(50)")
    private String email;

    @Getter
    @Column(name = "age")
    private int age;



    public Student() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
