package server.service;

import server.domain.Course;

import java.util.List;

public interface CourseService {

    List<Course> getAllCourses();

    void addCourse(Course course);
}
