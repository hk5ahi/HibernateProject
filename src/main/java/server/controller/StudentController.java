package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.domain.Student;
import server.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private  final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("")
    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students=studentService.getStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping("")
    public ResponseEntity<String> add(@RequestBody Student student) {
        studentService.addStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("")
    public void delete(@RequestParam String Username) {
        studentService.deleteStudent(Username);

    }

}
