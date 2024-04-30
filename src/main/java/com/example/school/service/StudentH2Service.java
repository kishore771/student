package com.example.school.service;

import com.example.school.model.StudentRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;

@Service
public class StudentH2Service implements StudentRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Student> getStudents() {
        List<Student> studentCollection = db.query("select * from STUDENT ", new StudentRowMapper());
        ArrayList<Student> students = new ArrayList<>(studentCollection);

        return students;
    }

    @Override
    public Student getStudentById(int studentId) {
        try {
            Student student = db.queryForObject("select * from STUDENT  where studentId = ?",
                    new StudentRowMapper(), studentId);
            return student;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Student addStudent(Student student) {
        db.update("insert into student (studentName, Gender, Standard) values (?,?,?)", student.getStudentName(),
                student.getGender(), student.getStandard());
        Student savedStudent = db.queryForObject(
                "select from student where studentName = ? and Gender =? and standard = ?",
                student.getStudentName(), student.getGender(), student.getStandard());
        return savedStudent;
    }

    @Override
    public String addMultipleStudents (ArrayList<Student> studentsList) {
    for (Student eachStudent studentsList) {
        db.update("insert into student (studentName, gender, standard) values (?,?,?)", eachStudent.getStudentName(), eachStudent.getGender(), eachStudent.getStandard()); 
    }
    String responseMessage = String.format("Successfully added %d students", studentsList.size());
    return responseMessage;
    }

    @Override
    public Student updateStudent(int studentId, Student student) {
        if (student.getStudentName() != null) {
            db.update("update STUDENT  set studentName = ? where studentId = ?",
                    student.getStudentName(), studentId);
        }
        if (student.getGender() != null) {
            db.update("update STUDENT  set gender = ? where studentId = ?",
                    student.getGender(), studentId);
        }
        if (student.getStandard() != 0) {
            db.update("update STUDENT  set standard = ? where studentId = ?",
                    student.getStandard(), studentId);
        }

        return getStudentById(studentId);
    }

    @Override
    public void deleteStudent(int studentId) {

        db.update("delete from STUDENT  where studentId = ?", studentId);

    }

}
