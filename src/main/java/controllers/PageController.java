package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @RequestMapping(value={"/", "/index"})
    public String homePage() {
        return "index";
    }

    @RequestMapping("/group")
    public String groupPage(){
        return "group";
    }

    @RequestMapping("/student")
    public String studentPage() {
        return "student";
    }

    @RequestMapping("/teacher")
    public String teacherPage() {
        return "teacher";
    }
}