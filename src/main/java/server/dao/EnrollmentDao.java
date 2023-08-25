package server.dao;

import server.domain.Course;
import server.domain.Enrollment;
import server.domain.Student;
import server.dto.EnrollmentDTO;

import java.util.List;
import java.util.Optional;

public interface EnrollmentDao {


    void addEnrollment(EnrollmentDTO enrollmentDTO);
    List<Enrollment> getEnrollmentsByCourseTitle(String courseTitle);

    List<Enrollment> getEnrollmentsByStudentUserName(String username);

    List<Enrollment> getEnrollmentsByCriteria(String username,String title);

    void deleteEnrollment(String userName, String title);

    Optional<Enrollment> getEnrollmentByStudentAndCourse(Student student, Course course);
}
