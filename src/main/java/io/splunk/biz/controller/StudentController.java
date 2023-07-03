package io.splunk.biz.controller;

import io.splunk.biz.mapper.Mapper;
import io.splunk.biz.model.Student;
import io.splunk.biz.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class StudentController {
    Logger logger =  LoggerFactory.getLogger(StudentController.class);
//    Logger logger = org.apache.logging.log4j.core.LoggerContext.getLogger(String.valueOf(StudentController.class));

    @Autowired
    private StudentService service;


    @GetMapping
    public List<Student> getStudentDetails() {
        List<Student> students = service.getStudents();
        logger.info("StudentController:getStudentDetails response from service {}", Mapper.mapToJsonString(students));
        return students;
    }

    @GetMapping("/{id}")
    public Student getStudentDetailsById(@PathVariable int id) {
        logger.info("StudentController:getStudentDetailsById fetch order by id {}", id);
        Student data = service.getStudentById(id);
        logger.info("StudentController:getStudentDetailsById fetch Student response {}", Mapper.mapToJsonString(data));
        return data;
    }

    @GetMapping(value = "/test/{id}")
    public ResponseEntity<?> getTestDetailsById(@PathVariable int id) {
        logger.info("StudentController:getStudentDetailsById fetch order by id {}", id);
        Student data = service.getStudentById(id);
        logger.info("StudentController:getStudentDetailsById fetch Student response {}", Mapper.mapToJsonString(data));
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/sync/{id}")
    public ResponseEntity<?> getSyncDetailsById(@PathVariable int id) {
        logger.info("StudentController:getStudentDetailsById fetch order by id {}", id);
        Student data = service.getStudentById(id);
        data.setId(data.getId()+id);
        logger.info("StudentController:getStudentDetailsById fetch Student response {}", Mapper.mapToJsonString(data));
        return ResponseEntity.ok(data);
    }

//    public static void main(String[] args) {
//        SpringApplication.run(SplunkDemoApplication.class, args);
//    }
}
