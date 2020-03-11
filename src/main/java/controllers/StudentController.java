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
    public String getAllStudents() {
        List<Student> students = studentService.selectAll();
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Student.class, new StudentSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        if(!students.isEmpty()) {
            jsonObject.add("students", gson.toJsonTree(students));
        } else {
            errors.add("#S1: список студентов пуст.");
        }
        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="student/selectStudentById",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getStudentById(@RequestParam(name="id") int id) {
        Student student = studentService.selectById(id);
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Student.class, new StudentSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        if(!Objects.isNull(student)) {
            List<Student> list = new ArrayList<>();
            list.add(student);
            jsonObject.add("students", gson.toJsonTree(list));
        } else {
            errors.add("#S5: нет такого студента.");
        }

        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="student/addStudent",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String addStudent(@RequestParam Student student) {


        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Student.class, new StudentSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();


            studentService.add(student);
            jsonObject.addProperty("lastId", student.getId());
            jsonObject.addProperty("group", student.getGroup().getNumber());

        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();

    }

    @RequestMapping(value="student/deleteStudent",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String deleteStudent(
            @RequestParam(name="id") int id
            ) {
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Student.class, new StudentSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        Student student = studentService.selectById(id);
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
            @RequestParam(name="id") int id,
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
        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="student/selectStudent",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String selectStudent(
            @RequestParam(name="name", required = false) String name,
            @RequestParam(name="birth", required = false) String birth,
            @RequestParam(name="gender", required = false) String gender,
            @RequestParam(name="group", required = false) Integer group
    ) {
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Student.class, new StudentSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        Set<Student> students = new HashSet<>();

      if(name.isEmpty()
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

        jsonObject.add("students", gson.toJsonTree(students));
        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }
}