package server.dao;

import server.domain.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao {

    List<Course> getAllCourses();
    boolean existsByTitle(String title);
    void deleteByTitle(String title);
    void addCourse(Course course);

    Optional<Course> getCourseByTitle(String title);
}
