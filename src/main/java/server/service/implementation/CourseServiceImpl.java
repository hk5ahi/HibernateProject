package server.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.dao.CourseDao;
import server.dao.EnrollmentDao;
import server.domain.Course;
import server.domain.Enrollment;
import server.domain.Student;
import server.exception.CourseAlreadyExistsException;
import server.exception.NotFoundException;
import server.exception.StudentAlreadyExistsException;
import server.service.CourseService;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;
    private final EnrollmentDao enrollmentDao;
    @Autowired
    public CourseServiceImpl(CourseDao courseDao, EnrollmentDao enrollmentDao) {
        this.courseDao = courseDao;
        this.enrollmentDao = enrollmentDao;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }

    @Override
    public void addCourse(Course course) {

        Optional<Course> existingCourse = courseDao.getCourseByTitle(course.getTitle());
        if (existingCourse.isPresent()) {
            throw new CourseAlreadyExistsException("Course with" + course.getTitle() + " already exists.");
        } else {
            courseDao.addCourse(course);
        }
    }

    @Override
    public void deleteCourse(String title) {
        if (courseDao.existsByTitle(title)) {

            Optional<Course> optionalCourse=courseDao.getCourseByTitle(title);
            if(optionalCourse.isPresent())
            {
                for (Enrollment enrollment : enrollmentDao.getEnrollmentsByCourseTitle(optionalCourse.get().getTitle())) {
                    enrollmentDao.deleteEnrollment(enrollment.getStudent().getUserName(),enrollment.getCourse().getTitle()); // Custom method to delete an enrollment
                }
                courseDao.deleteByTitle(title);
            }

        }
        else {
            throw new NotFoundException("The Student Does not Exist");
        }
    }
}
