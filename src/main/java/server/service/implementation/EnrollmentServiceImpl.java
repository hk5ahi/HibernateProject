package server.service.implementation;

import org.springframework.stereotype.Service;
import server.dao.CourseDao;
import server.dao.EnrollmentDao;
import server.dao.StudentDao;
import server.domain.Course;
import server.domain.Enrollment;
import server.domain.Student;
import server.dto.EnrollmentDTO;
import server.exception.EnrollmentAlreadyExistsException;
import server.exception.NotFoundException;
import server.service.EnrollmentService;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentDao enrollmentDao;
    private final StudentDao studentDao;

    private final CourseDao courseDao;

    public EnrollmentServiceImpl(EnrollmentDao enrollmentDao, StudentDao studentDao, CourseDao courseDao) {
        this.enrollmentDao = enrollmentDao;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    @Override
    public void addEnrollment(EnrollmentDTO enrollmentDTO) {
        Optional<Student> optionalStudent = studentDao.getStudentByUsername(enrollmentDTO.getUsername());
        Optional<Course> optionalCourse = courseDao.getCourseByTitle(enrollmentDTO.getTitle());

        if (!optionalStudent.isPresent()) {
            throw new NotFoundException("Student does not exist");
        }

        if (!optionalCourse.isPresent()) {
            throw new NotFoundException("Course does not exist");
        }

        Student student = optionalStudent.get();
        Course course = optionalCourse.get();

        Optional<Enrollment> optionalEnrollment = enrollmentDao.getEnrollmentByStudentAndCourse(student, course);
        if (optionalEnrollment.isPresent()) {
            Enrollment existingEnrollment = optionalEnrollment.get();
            throw new EnrollmentAlreadyExistsException("Enrollment already exists with Student username "
                    + existingEnrollment.getStudent().getUserName() + " and Course title "
                    + existingEnrollment.getCourse().getTitle());
        } else {
            enrollmentDao.addEnrollment(enrollmentDTO);
        }
    }

    @Override
    public List<Enrollment> getEnrollmentsByCriteria(String username, String title) {
        return enrollmentDao.getEnrollmentsByCriteria(username,title);
    }

    @Override
    public void deleteEnrollment(String username, String title) {
        enrollmentDao.deleteEnrollment(username,title);
    }
}
