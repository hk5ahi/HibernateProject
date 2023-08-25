package server.dao;

import server.domain.Student;


import java.util.List;
import java.util.Optional;

public interface StudentDao {

    List<Student> getStudents();

    void addStudent(Student student);

    void deleteByUsername(String Username);

    boolean existsByUsername(String Username);

    Optional<Student> getStudentByUsername(String username);
}
