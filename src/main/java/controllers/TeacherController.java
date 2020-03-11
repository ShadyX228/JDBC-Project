package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.entity.Teacher;
import dbmodules.service.GroupService;
import dbmodules.service.StudentService;
import dbmodules.service.TeacherService;
import dbmodules.types.Gender;
import gson.GroupSerialize;
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
public class TeacherController {
    private TeacherService teacherService;
    private GroupService groupService;
    @Autowired
    public TeacherController(TeacherService teacherService,
                             GroupService groupService) {
        this.teacherService = teacherService;
        this.groupService = groupService;
    }

    @RequestMapping(value="teacher/selectAllTeachers",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getAllTeachers() {
        List<Teacher> teachers = teacherService.selectAll();
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        if(!teachers.isEmpty()) {
            jsonObject.add("teachers", gson.toJsonTree(teachers));
        } else {
            errors.add("#T1: список преподавателей пуст.");
            jsonObject.add("errors", gson.toJsonTree(errors));
        }

        return jsonObject.toString();
    }

    @RequestMapping(value="teacher/selectTeacherById",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getTeacherById(@RequestParam(name="id") int id) {
        Teacher teacher = teacherService.selectById(id);
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        if(!Objects.isNull(teacher)) {
            List<Teacher> list = new ArrayList<>();
            list.add(teacher);
            jsonObject.add("teachers", gson.toJsonTree(list));
        } else {
            errors.add("#T9: нет такого преподавателя.");
            jsonObject.add("errors", gson.toJsonTree(errors));
        }

        return jsonObject.toString();
    }

    @RequestMapping(value="teacher/addTeacher",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String addTeacher (
            @RequestParam(name="name") String name,
            @RequestParam(name="birth") String birth,
            @RequestParam(name="gender") String gender
    ) {
        LocalDate birthParsed = checkBirth(birth);
        Gender genderParsed = checkGender(gender);

        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        if(!Objects.isNull(name)
                && !Objects.isNull(birthParsed)
                && !Objects.isNull(genderParsed)
                ) {
            Teacher teacher = new Teacher(name,
                    birthParsed.getYear(),
                    birthParsed.getMonthValue(),
                    birthParsed.getDayOfMonth(),
                    genderParsed);
            teacherService.add(teacher);
            jsonObject.addProperty("lastId", teacher.getId());
        } else {
            errors.add("#T2: переданы некорректные параметры.");
        }
        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="teacher/deleteTeacher",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String deleteTeacher (
            @RequestParam(name="id") int id
    ) {
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .registerTypeAdapter(Group.class, new GroupSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        Teacher teacher = teacherService.selectById(id);
        System.out.print(id + " " + teacher);
        if(!Objects.isNull(teacher) && teacher.getGroups().isEmpty()) {
            teacherService.delete(teacher);
        } else {
            errors.add("#T3: некорректный id преподавателя или у него есть группы, в которых он преподает.");
        }
        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="teacher/updateTeacher",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String updateTeacher(
            @RequestParam(name="id") int id,
            @RequestParam(name="name") String name,
            @RequestParam(name="birth") String birth,
            @RequestParam(name="gender") String gender
    ) {
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        Teacher teacher = teacherService.selectById(id);

        LocalDate birthParsed = checkBirth(birth);
        Gender genderParsed = checkGender(gender);

        if(!Objects.isNull(teacher)
                && (!Objects.isNull(name)
                && !Objects.isNull(birthParsed)
                && !Objects.isNull(genderParsed))) {
            teacherService.update(teacher, name, birthParsed,
                    genderParsed);
        } else {
            errors.add("#T4: некорректный id преподавателя или переданные параметры.");
        }
        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="teacher/selectTeacher",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String selectTeacher(
            @RequestParam(name="name", required = false) String name,
            @RequestParam(name="birth", required = false) String birth,
            @RequestParam(name="gender", required = false) String gender
    ) {
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        Set<Teacher> teachers = new HashSet<>();

        LocalDate birthParsed = null;
        Gender genderParsed = null;

        if (!Objects.isNull(birth)) {
            birthParsed = checkBirth(birth);
        }
        if (!Objects.isNull(gender)) {
            genderParsed = checkGender(gender);
        }
        teachers.addAll(teacherService.select(name, birthParsed, genderParsed));


        jsonObject.add("teachers", gson.toJsonTree(teachers));
        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="teacher/getTeacherInfo",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getTeacherInfo(
            @RequestParam(name="id") Integer id
    ) {
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .registerTypeAdapter
                        (Group.class, new GroupSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();


        Map<Integer, Integer> groups = new HashMap<>();

        Teacher teacher = teacherService.selectById(id);
        if(!Objects.isNull(teacher)) {
            for(Group group : teacher.getGroups()) {
                groups.put(group.getId(), group.getNumber());
            }
        } else {
            errors.add("T5: некорректный id.");
        }

        jsonObject.add("groups", gson.toJsonTree(groups));
        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="teacher/putTeacherInGroup",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String putTeacherInGroup(
            @RequestParam(name="teacherId") Integer teacherId,
            @RequestParam(name="groupNumber") Integer number) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        List<String> errors = new ArrayList<>();

        Group group = groupService.select(number);
        Teacher  teacher = teacherService.selectById(teacherId);
        if(!Objects.isNull(group)) {
            List<Group> teacherGroups = teacher.getGroups();

            Boolean check = true;

            for(Group currentGroup : teacherGroups) {
                if(currentGroup.equals(group)) {
                    errors.add("T7: преподаватель уже преподает в этой группе.");
                    check = false;
                    break;
                }
            }

            if(check) {
                teacherService.putTeacherInGroup(teacher, group);
                jsonObject.addProperty("groupId", group.getId());
                jsonObject.addProperty("groupNumber", number);
            }
        } else {
            errors.add("T6: некорректные параметры.");
        }

        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="teacher/removeTeacherFromGroup",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String removeTeacherFromGroup(
            @RequestParam(name="teacherId") Integer teacherId,
            @RequestParam(name="groupId") Integer id) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        List<String> errors = new ArrayList<>();

        Group group = groupService.selectById(id);
        Teacher  teacher = teacherService.selectById(teacherId);
        if(!Objects.isNull(group)) {
            teacherService.removeTeacherFromGroup(teacher, group);
        } else {
            errors.add("T8: некорректные параметры.");
        }

        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }
}