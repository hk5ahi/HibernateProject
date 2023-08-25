package server.service;

import server.domain.Enrollment;
import server.dto.EnrollmentDTO;

import java.util.List;

public interface EnrollmentService  {

    void addEnrollment(EnrollmentDTO enrollmentDTO);

    List<Enrollment> getEnrollmentsByCriteria(String username,String title);

    void deleteEnrollment(String username,String title);
}
