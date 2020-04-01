package converters;

import dbmodules.entity.Student;
import dbmodules.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class StudentConverter implements Converter<String, Student> {
    private StudentService studentService;

    @Autowired
    public StudentConverter(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public Student convert(String studentId) {
        int id = Integer.parseInt(studentId);
        return studentService.selectById(id);
    }
}