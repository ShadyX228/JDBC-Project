package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.entity.Teacher;
import dbmodules.service.GroupService;
import dbmodules.service.StudentService;
import dbmodules.types.Gender;
import gson.StudentSerialize;
import gson.TeacherSerialize;
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
    private GroupService groupService;
    private StudentService studentService;
    @Autowired
    public StudentController(GroupService groupService,
                           StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

    @RequestMapping(value="student/selectAllStudents",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getAllStudents(
            @RequestParam(name = "start") int first,
            @RequestParam(name = "length") int last,
            @RequestParam(name = "search[value]") String search) {
        List<Student> students;

        if(search.isEmpty()) {
            students = studentService.selectAll(first, last);
        } else {
            students = studentService.select(first, last, search);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Student.class, new StudentSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        System.out.print(students);

        jsonObject.add("students", gson.toJsonTree(students));
        jsonObject.addProperty("length", last);
        jsonObject.addProperty("start", first);
        jsonObject.addProperty("recordsTotal", students.size());
        jsonObject.addProperty("recordsFiltered", studentService.selectAll().size());
        return jsonObject.toString();
    }

    @RequestMapping(value="student/addStudent",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String addStudent(Student student) {
        List<String> errors = new ArrayList<>();


        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Student.class, new StudentSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        System.out.println(!student.getName().isEmpty()
                + " " + !Objects.isNull(student.getBirth())
                + " " + !Objects.isNull(student.getGender())
                + " " + !Objects.isNull(student.getGroup()) + " " + student.getGroup()
                + " " + !Objects.isNull(student.getBirth()));

        if(!student.getName().isEmpty()
                && !Objects.isNull(student.getBirth())
                && !Objects.isNull(student.getGender())
                && !Objects.isNull(student.getGroup())
                && !Objects.isNull(student.getBirth())
                ) {
            studentService.add(student);
        } else {
            errors.add("#T2: переданы некорректные параметры.");
        }


        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();

    }

    @RequestMapping(value="student/deleteStudent",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String deleteStudent(
            @RequestParam(name="id") Student student
    ) {
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Student.class, new StudentSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        if(!Objects.isNull(student)) {
            studentService.delete(student);
        } else {
            errors.add("#S3: некорректный id студента.");
        }
        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="student/updateStudent",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String updateStudent(
            @RequestParam(name="id") Student student,
            @RequestParam(name="name") String name,
            @RequestParam(name="birth") String birth,
            @RequestParam(name="gender") String gender,
            @RequestParam(name="group") int group
    ) {
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Student.class, new StudentSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

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
        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }
}