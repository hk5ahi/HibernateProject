package server.service.implementation;

import org.springframework.stereotype.Service;
import server.dao.CourseDao;
import server.dao.EnrollmentDao;
import server.dao.StudentDao;
import server.domain.Enrollment;
import server.dto.EnrollmentDTO;
import server.exception.NotFoundException;
import server.service.EnrollmentService;

import java.util.List;

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
    public void addEnrollment(EnrollmentDTO enrollmentDTO)
    {
        if(!(studentDao.existsByUsername(enrollmentDTO.getUsername())))
        {
            throw new NotFoundException("Student Does not Exist");
        } else if (!(courseDao.existsByTitle(enrollmentDTO.getTitle()))) {
            throw new NotFoundException("Course Does not Exist");
        }
        else {
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
