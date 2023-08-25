package server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.domain.Course;
import server.service.CourseService;

import java.util.List;

@Controller
@RequestMapping("courses")
public class CourseController {

    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PostMapping("")
    public ResponseEntity<String> add(@RequestBody Course course) {
        courseService.addCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("")
    public void delete(@RequestParam String title) {
        courseService.deleteCourse(title);

    }
}
