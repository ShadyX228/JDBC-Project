package servlets.Main;

import dbmodules.dao.GroupDAO;
import dbmodules.dao.StudentDAO;
import dbmodules.dao.TeacherDAO;

public class MainServlet {
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    private GroupDAO groupDAO;

    public MainServlet() {
        studentDAO = new StudentDAO();
        teacherDAO = new TeacherDAO();
        groupDAO = new GroupDAO();
    }

    public StudentDAO getStudentDAO() {
        return studentDAO;
    }
    public TeacherDAO getTeacherDAO() {
        return teacherDAO;
    }
    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

}
