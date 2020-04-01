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
import dbmodules.types.Criteria;
import gson.GroupSerialize;
import gson.TeacherSerialize;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class GroupController {
    private GroupService groupService;
    private TeacherService teacherService;
    @Autowired
    public GroupController(GroupService groupService,
                           TeacherService teacherService) {
        this.groupService = groupService;
        this.teacherService = teacherService;
    }

    @RequestMapping(value="group/selectAllGroups",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getAllGroups(
            @RequestParam(name = "start") int first,
            @RequestParam(name = "length") int last,
            @RequestParam(name = "search[value]") String search) {
        List<Group> groups = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        if(search.isEmpty()) {
            groups = groupService.selectAll(first, last);
        } else {
            Group group = groupService.select(Integer.parseInt(search));
            if(!Objects.isNull(group)) {
                groups.add(group);
            } else {
                errors.add("G1: нет группы с таким номером.");
            }
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Group.class, new GroupSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();



        jsonObject.add("groups", gson.toJsonTree(groups));
        jsonObject.add("errors", gson.toJsonTree(errors));
        jsonObject.addProperty("length", last);
        jsonObject.addProperty("start", first);
        jsonObject.addProperty("recordsTotal", groups.size());
        jsonObject.addProperty("recordsFiltered", groupService.selectAll().size());
        return jsonObject.toString();
    }

    @RequestMapping(value="group/addGroup",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String addGroup(Group group) {
        Group groupCheck = groupService.select(group.getNumber());
        System.out.print(group + " " + groupCheck);
        List<String> errors = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Group.class, new GroupSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        if(Objects.isNull(groupCheck)) {
            groupService.add(group);
        } else {
            errors.add("G3: такая группа уже есть");
        }

        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="group/deleteGroup",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String deleteGroup(Group group) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Group.class, new GroupSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        List<String> errors = new ArrayList<>();

        if(!Objects.isNull(group)
                && groupService.getGroupStudents(group).isEmpty()
                && groupService.getGroupTeachers(group).isEmpty()) {
            groupService.delete(group);
        } else {
            errors.add("#G4: невозможно удалить группу. Нет группы с таким номером или в группе есть преподаватели/студенты.");
        }
        jsonObject.add("errors", gson.toJsonTree(errors));
        return jsonObject.toString();
    }

    @RequestMapping(value="group/updateGroup",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String updateGroup(Group group,
                              @RequestParam(name="newNumber") int newNumber) {
        Group check = groupService.select(newNumber);

        System.out.print(group + " " + newNumber + " " + check);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Group.class, new GroupSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        List<String> errors = new ArrayList<>();

        if(Objects.isNull(check)) {
            groupService.update(group, newNumber);
        } else {
            errors.add("G5: невозможно обновить группу. Возможно, группа с таким номером уже существует.");
        }
        jsonObject.add("errors", gson.toJsonTree(errors));

        return jsonObject.toString();
    }

    @RequestMapping(value="group/getGroupInfoTeachers",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getGroupInfoTeachers(@RequestParam(name="id") Group group) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Group.class, new GroupSerialize())
                .registerTypeAdapter
                        (Teacher.class, new TeacherSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        List<String> errors = new ArrayList<>();
        Set<Teacher> teachers = new HashSet<>();

        if(!Objects.isNull(group)) {
            teachers.addAll(groupService.getGroupTeachers(group));
        } else {
            errors.add("G6: нет группы с таким номером.");
        }
        jsonObject.add("errors", gson.toJsonTree(errors));
        jsonObject.add("teachers", gson.toJsonTree(teachers));

        return jsonObject.toString();
    }


    @RequestMapping(value="group/getGroupInfoStudents",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getGroupInfoStudents(@RequestParam(name="id") Group group) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Group.class, new GroupSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        List<String> errors = new ArrayList<>();
        List<String> students = new ArrayList<>();

        if(!Objects.isNull(group)) {
            for(Student student : groupService.getGroupStudents(group)) {
                students.add(student.getName());
            }
        } else {
            errors.add("G6.1: нет группы с таким номером.");
        }
        jsonObject.add("errors", gson.toJsonTree(errors));
        jsonObject.add("students", gson.toJsonTree(students));

        return jsonObject.toString();
    }

    @RequestMapping(value="group/deleteTeacherFromGroup",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public void deleteTeacherFromGroup(
            @RequestParam(name="teacherId") Teacher teacher,
            @RequestParam(name="groupId") Group group
            ) {
        teacherService.removeTeacherFromGroup(teacher, group);
    }

    @RequestMapping(value="group/getFreeTeachers",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getFreeTeachers(
            @RequestParam(name="id") Group group
    ) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter
                        (Group.class, new GroupSerialize())
                .create();
        JsonObject jsonObject = new JsonObject();

        List<String> errors = new ArrayList<>();
        List<Teacher> freeTeachers = teacherService.selectFree(group);


        if(freeTeachers.isEmpty()) {
            errors.add("G7: нет свободных преподаватлей.");
        }
        jsonObject.add("teachers", gson.toJsonTree(freeTeachers));
        jsonObject.add("errors", gson.toJsonTree(errors));

        return jsonObject.toString();
    }

    @RequestMapping(value="group/putTeacherInGroup",
            produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public void putTeacherInGroup(
            @RequestParam(name="teacherId") Teacher teacher,
            @RequestParam(name="groupId") Group group
    ) {
        teacherService.putTeacherInGroup(teacher, group);
    }
}