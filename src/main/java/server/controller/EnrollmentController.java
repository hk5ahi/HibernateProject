package server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import server.domain.Enrollment;
import server.dto.EnrollmentDTO;
import server.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        enrollmentService.addEnrollment(enrollmentDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<Enrollment> getEnrollmentsByCriteria(@RequestParam(required = false) String Username,@RequestParam(required = false) String title)
    {
        return enrollmentService.getEnrollmentsByCriteria(Username,title);

    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("")
    public void delete(EnrollmentDTO enrollmentDTO) {

        enrollmentService.deleteEnrollment(enrollmentDTO.getUsername(), enrollmentDTO.getTitle());
    }
}
