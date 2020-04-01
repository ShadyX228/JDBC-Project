<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <title>Информационная система для работы с базой студентов, групп и преподавателей</title>
        <link rel="stylesheet" type="text/css" href="../resources/css/style.css">
        <meta charset="UTF-8">
    </head>
    <body>
        <div id="wrapper">
            <nav>
                <ul>
                    <li><a class="currentPage" id="homeAction" href="/">H</a></li>
                    <li><a id="studentAction" href="student">Студенты</a></li>
                    <li><a id="groupAction"  href="group">Группы</a></li>
                    <li><a id="teacherAction"  href="teacher">Преподаватели</a></li>
                </ul>
            </nav>
            <div id="content" style="padding: 10px; border-bottom: 1px solid black;">Выберите один из пунктов сверху, чтобы начать работу</div>
            <footer>
                Подвальчик
            </footer>
        </div>
    </body>
</html>
