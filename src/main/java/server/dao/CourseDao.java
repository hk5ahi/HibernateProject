package server.dao;

import server.domain.Course;

import java.util.List;

public interface CourseDao {

    List<Course> getAllCourses();

    void addCourse(Course course);
}
