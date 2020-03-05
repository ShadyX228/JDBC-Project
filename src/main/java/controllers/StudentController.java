package controllers;

import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.service.GroupService;
import dbmodules.service.StudentService;
import dbmodules.types.Gender;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.*;

import static webdebugger.WebInputDebugger.checkBirth;
import static webdebugger.WebInputDebugger.checkGender;

@Controller
public class StudentController {
    @Autowired
    public StudentController(GroupService groupService,
                           StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

    @RequestMapping(value="student/selectAllStudents",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String getAllStudents() {
        List<Student> students = studentService.selectAll();
        List<String> errors = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        if(!students.isEmpty()) {
            jsonObject.accumulate("students", students);
        } else {
            errors.add("#S1: список студентов пуст.");
            jsonObject.accumulate("errors", errors);
        }

        return jsonObject.toString();
    }

    @RequestMapping(value="student/addStudent",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String addStudent(
            @RequestParam(name="name") String name,
            @RequestParam(name="birth") String birth,
            @RequestParam(name="gender") String gender,
            @RequestParam(name="group") int group
            ) {
        LocalDate birthParsed = checkBirth(birth);
        Gender genderParsed = checkGender(gender);
        Group groupParsed = groupService.select(group);

        List<String> errors = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        if(!Objects.isNull(name)
                && !Objects.isNull(birthParsed)
                && !Objects.isNull(genderParsed)
                && !Objects.isNull(groupParsed)
                ) {
            Student student = new Student(name,
                    birthParsed.getYear(),
                    birthParsed.getMonthValue(),
                    birthParsed.getDayOfMonth(),
                    genderParsed,
                    groupParsed);
            studentService.add(student);
            jsonObject.put("lastId", student.getId());
            jsonObject.put("group", student.getGroup().getNumber());
        } else {
            errors.add("#S2: переданы некорректные параметры.");
        }
        jsonObject.accumulate("errors", errors);
        return jsonObject.toString();

    }

    @RequestMapping(value="student/deleteStudent",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String deleteStudent(
            @RequestParam(name="id") int id
            ) {
        List<String> errors = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        Student student = studentService.selectById(id);
        if(!Objects.isNull(student)) {
            studentService.delete(student);
        } else {
            errors.add("#S3: некорректный id студента.");
        }
        jsonObject.accumulate("errors", errors);
        return jsonObject.toString();
    }

    @RequestMapping(value="student/updateStudent",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String updateStudent(
            @RequestParam(name="id") int id,
            @RequestParam(name="name") String name,
            @RequestParam(name="birth") String birth,
            @RequestParam(name="gender") String gender,
            @RequestParam(name="group") int group
    ) {
        List<String> errors = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        Student student = studentService.selectById(id);

        LocalDate birthParsed = checkBirth(birth);
        Gender genderParsed = checkGender(gender);
        Group groupParsed = groupService.select(group);

        if(!Objects.isNull(student)
                && (!Objects.isNull(name)
                    && !Objects.isNull(birthParsed)
                    && !Objects.isNull(genderParsed)
                    && !Objects.isNull(groupParsed))) {
            studentService.update(student, name, birthParsed,
                    genderParsed, groupParsed);
        } else {
            errors.add("#S4: некорректный id студента или переданные параметры.");
        }
        jsonObject.accumulate("errors", errors);
        return jsonObject.toString();
    }

    @RequestMapping(value="student/selectStudent",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String selectStudent(
            @RequestParam(name="id", required = false) Integer id,
            @RequestParam(name="name", required = false) String name,
            @RequestParam(name="birth", required = false) String birth,
            @RequestParam(name="gender", required = false) String gender,
            @RequestParam(name="group", required = false) Integer group
    ) {
        List<String> errors = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        Set<Student> students = new HashSet<>();

        if(!Objects.isNull(id)) {
            students.add(studentService.selectById(id));
        } else if(name.isEmpty()
                && birth.isEmpty()
                && gender.isEmpty()
                && Objects.isNull(group)) {
            students.addAll(studentService.selectAll());
        }
        else {
            LocalDate birthParsed = null;
            Gender genderParsed = null;
            Group groupParsed = null;


            if (!Objects.isNull(birth)) {
                birthParsed = checkBirth(birth);
            }
            if (!Objects.isNull(gender)) {
                genderParsed = checkGender(gender);
            }
            if (!Objects.isNull(group)) {
                groupParsed = groupService.select(group);
            }

            students.addAll(studentService.select(name, birthParsed, genderParsed, groupParsed));
        }

        jsonObject.accumulate("students", students);
        jsonObject.accumulate("errors", errors);
        return jsonObject.toString();
    }

    private GroupService groupService;
    private StudentService studentService;
}