package server.dao;

import server.domain.Student;
import server.dto.StudentDTO;

import java.util.List;

public interface StudentDao {

    List<Student> getStudents();

    void addStudent(Student student);
}
