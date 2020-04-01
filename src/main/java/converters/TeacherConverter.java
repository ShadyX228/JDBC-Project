package converters;

import dbmodules.entity.Teacher;
import dbmodules.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.transaction.annotation.Transactional;

public class TeacherConverter implements Converter<String, Teacher> {
    private TeacherService teacherService;

    @Autowired
    public TeacherConverter(TeacherService teacherService){
        this.teacherService = teacherService;
    }

    @Override
    @Transactional
    public Teacher convert(String teacherId) {
        int id = Integer.parseInt(teacherId);
        return teacherService.selectById(id);
    }
}