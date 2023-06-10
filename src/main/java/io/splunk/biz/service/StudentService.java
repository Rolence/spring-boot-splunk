package io.splunk.biz.service;

import io.splunk.biz.mapper.Mapper;
import io.splunk.biz.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    Logger logger=  LoggerFactory.getLogger(StudentService.class);

    private List<Student> studentList = Arrays.asList(new Student(1, "abc", 23, "Female", "Science"),
            new Student(2, "bcd", 24, "Male", "Maths"),
            new Student(3, "wer",25,"Female","English"),
            new Student(4, "der",26,"Male","History"),
            new Student(5, "fer",24,"Male","Finance"),
            new Student(6, "get",20,"Female","Computer"));

    public List<Student> getStudents() {
        logger.info("StudentService:getStudents execution started..");
        List<Student> list = null;
        list = studentList;
        logger.info("StudentService:getStudents response  {} ", Mapper.mapToJsonString(studentList));
        logger.info("StudentService:getStudents execution ended..");
        return list;
    }

    public Student getStudentById(int id) {
        logger.info("StudentService:getStudentById execution started..");
        Student order = studentList.stream()
                .filter(ord -> ord.getId() == id)
                .findAny().orElseThrow(() -> new RuntimeException("Student not found with id : " + id));
        logger.info("StudentService:getStudentById execution ended..");
        return order;
    }
}
