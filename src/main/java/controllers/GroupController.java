package controllers;

import dbmodules.entity.Group;
import dbmodules.entity.Student;
import dbmodules.entity.Teacher;
import dbmodules.service.GroupService;
import dbmodules.service.StudentService;
import dbmodules.service.TeacherService;
import dbmodules.types.Criteria;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class GroupController {
    @Autowired
    public GroupController(GroupService groupService,
                           TeacherService teacherService) {
        this.groupService = groupService;
        this.teacherService = teacherService;
    }

    @RequestMapping(value="group/selectAllGroups",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String getAllGroups() {
        List<Group> groups = groupService.selectAll();
        List<String> errors = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        if(!groups.isEmpty()) {
            jsonObject.accumulate("groups", groups);
        } else {
            errors.add("#G1: список групп пуст.");
            jsonObject.accumulate("errors", errors);
        }

        return jsonObject.toString();
    }

    @RequestMapping(value="group/selectGroup",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String getGroup(@RequestParam(name = "group") int number) {
        Group group = groupService.select(number);
        JSONObject jsonObject = new JSONObject();
        List<String> errors = new ArrayList<>();
        List<Group> groups = new ArrayList<>();

        if(!Objects.isNull(group)) {
            groups.add(group);
        } else {
            errors.add("#G2: нет группы с выбранным номером.");
            groups.addAll(groupService.selectAll());
        }
        jsonObject.accumulate("groups", groups);
        jsonObject.accumulate("errors", errors);

        System.out.print(errors);

        return jsonObject.toString();
    }

    @RequestMapping(value="group/addGroup",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String addGroup(@RequestParam(name = "group") int number) {
        Group group = groupService.select(number);
        JSONObject jsonObject = new JSONObject();
        List<String> errors = new ArrayList<>();

        if(Objects.isNull(group)) {
            group = new Group(number);
            groupService.add(group);
            jsonObject.put("lastId", group.getId());
        } else {
            errors.add("#G3: группа с таким номером уже существует");
        }
        jsonObject.accumulate("errors", errors);
        return jsonObject.toString();
    }

    @RequestMapping(value="group/deleteGroup",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String deleteGroup(@RequestParam(name = "id") int id) {
        Group group = groupService.selectById(id);
        JSONObject jsonObject = new JSONObject();
        List<String> errors = new ArrayList<>();

        if(!Objects.isNull(group)
                && groupService.getGroupStudents(group).isEmpty()
                && groupService.getGroupTeachers(group).isEmpty()) {
            groupService.delete(group);
        } else {
            errors.add("#G4: невозможно удалить группу. Нет группы с таким номером или в группе есть преподаватели/студенты.");
        }
        jsonObject.accumulate("errors", errors);
        return jsonObject.toString();
    }

    @RequestMapping(value="group/updateGroup",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String updateGroup(@RequestParam(name="id") int id,
                              @RequestParam(name="group") int newNumber) {
        Group group = groupService.selectById(id);
        Group check = groupService.select(newNumber);
        JSONObject jsonObject = new JSONObject();
        List<String> errors = new ArrayList<>();

        if(Objects.isNull(check)) {
            groupService.update(group, newNumber);
        } else {
            errors.add("G5: невозможно обновить группу. Возможно, группа с таким номером уже существует.");
        }
        jsonObject.accumulate("errors", errors);

        return jsonObject.toString();
    }

    @RequestMapping(value="group/getGroupInfo",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String getGroupInfo(@RequestParam(name="id") int id) {
        Group group = groupService.selectById(id);
        JSONObject jsonObject = new JSONObject();
        List<String> errors = new ArrayList<>();
        List<String> students = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();

        if(!Objects.isNull(group)) {
            for(Student student : groupService.getGroupStudents(group)) {
                students.add(student.getName());
            }
            teachers = groupService.getGroupTeachers(group);
        } else {
            errors.add("G6: нет группы с таким номером.");
        }
        jsonObject.accumulate("errors", errors);
        jsonObject.accumulate("students", students);
        jsonObject.accumulate("teachers", teachers);

        return jsonObject.toString();
    }

    @RequestMapping(value="group/deleteTeacherFromGroup",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public void deleteTeacherFromGroup(
            @RequestParam(name="teacherId") int teacherId,
            @RequestParam(name="groupId") int groupId
            ) {
        Teacher teacher = teacherService.selectById(teacherId);
        Group group = groupService.selectById(groupId);
        teacherService.removeTeacherFromGroup(teacher, group);
    }

    @RequestMapping(value="group/getFreeTeachers",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public String getFreeTeachers(
            @RequestParam(name="id") int id
    ) {
        JSONObject jsonObject = new JSONObject();
        List<String> errors = new ArrayList<>();
        List<Teacher> freeTeachers = teacherService.selectFree(id);


        if(freeTeachers.isEmpty()) {
            errors.add("G7: нет свободных преподаватлей.");
        }
        jsonObject.accumulate("teachers", freeTeachers);
        jsonObject.accumulate("errors", errors);

        return jsonObject.toString();
    }

    @RequestMapping(value="group/putTeacherInGroup",
            produces={"text/html; charset=UTF-8"})
    @ResponseBody
    public void putTeacherInGroup(
            @RequestParam(name="teacherId") int teacherId,
            @RequestParam(name="groupId") int groupId
    ) {
        Teacher teacher = teacherService.selectById(teacherId);
        Group group = groupService.selectById(groupId);
        teacherService.putTeacherInGroup(teacher, group);
    }


    private GroupService groupService;
    private TeacherService teacherService;
}