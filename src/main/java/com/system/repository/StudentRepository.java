package com.system.repository;

import com.system.domain.Student;
import java.util.List;

public interface StudentRepository {
    void addStudent(Student student) throws Exception;
    List<Student> getAllStudents() throws Exception;
    void updateStudent(Student student) throws Exception;
    void deleteStudent(String studentId) throws Exception;
    List<Student> searchStudents(String query) throws Exception;
}