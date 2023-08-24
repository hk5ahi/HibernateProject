package server.service;


import server.domain.Student;
import server.dto.StudentDTO;

import java.util.List;

public interface StudentService {

    List<Student> getStudents();

    void addStudent(Student student);
}
