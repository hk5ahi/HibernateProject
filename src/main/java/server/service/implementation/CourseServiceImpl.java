package server.service.implementation;

import org.springframework.stereotype.Service;
import server.dao.CourseDao;
import server.domain.Course;
import server.service.CourseService;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;

    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }

    @Override
    public void addCourse(Course course) {
        courseDao.addCourse(course);
    }
}
