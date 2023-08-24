package server.service.implementation;

import org.springframework.stereotype.Service;
import server.dao.StudentDao;
import server.domain.Student;
import server.service.StudentService;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public List<Student> getStudents() {
        return studentDao.getStudents();
    }

    @Override
    public void addStudent(Student student) {
        studentDao.addStudent(student);

    }
}
