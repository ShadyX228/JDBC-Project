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
    public String getAllTeachers(
            @RequestParam(name = "start") int first,
            @RequestParam(name = "length") int last,
            @RequestParam(name = "search[value]") String search
    ) {
        List<Teacher> teachers;

        if(search.isEmpty()) {
            teachers = teacherService.selectAll(first, last);
        } else {
            teachers = teacherService.select(first, last, search);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();


        jsonObject.add("teachers", gson.toJsonTree(teachers));
        jsonObject.addProperty("length", last);
        jsonObject.addProperty("start", first);
        jsonObject.addProperty("recordsTotal", teachers.size());
        jsonObject.addProperty("recordsFiltered", teacherService.selectAll().size());
        return jsonObject.toString();
    }


    @RequestMapping(value="teacher/addTeacher",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String addTeacher (Teacher teacher) {
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        if(!teacher.getName().isEmpty()
                && !Objects.isNull(teacher.getBirth())
                && !Objects.isNull(teacher.getGender())
                ) {
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
            @RequestParam(name="id") Teacher teacher
    ) {
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .registerTypeAdapter(Group.class, new GroupSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        System.out.print(teacher.getId() + " " + teacher);
        if(teacher.getGroups().isEmpty()) {
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
            @RequestParam(name="id") Teacher teacher,
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


        Set<Group> groups = new HashSet<>();

        Teacher teacher = teacherService.selectById(id);
        if(!Objects.isNull(teacher)) {
            groups.addAll(teacher.getGroups());
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