package server.service.implementation;

import org.springframework.stereotype.Service;
import server.dao.EnrollmentDao;
import server.dao.StudentDao;
import server.domain.Enrollment;
import server.domain.Student;
import server.exception.NotFoundException;
import server.service.StudentService;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;
    private final EnrollmentDao enrollmentDao;

    public StudentServiceImpl(StudentDao studentDao, EnrollmentDao enrollmentDao) {
        this.studentDao = studentDao;
        this.enrollmentDao = enrollmentDao;
    }

    @Override
    public List<Student> getStudents() {
        return studentDao.getStudents();
    }

    @Override
    public void addStudent(Student student) {
        studentDao.addStudent(student);

    }

    @Override
    public void deleteStudent(String Username) {
        if (studentDao.existsByUsername(Username)) {
            Optional<Student> optionalStudent=studentDao.getStudentByUsername(Username);
            if(optionalStudent.isPresent())
            {
                for (Enrollment enrollment : enrollmentDao.getEnrollmentsByStudentUserName(optionalStudent.get().getUserName())) {
                    enrollmentDao.deleteEnrollment(enrollment.getStudent().getUserName(),enrollment.getCourse().getTitle()); // Custom method to delete an enrollment
                }
                studentDao.deleteByUsername(Username);
            }
        }
        else {
            throw new NotFoundException("The Student Does not Exist");
        }
    }
}
