<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title> Группы | Информационная система для работы с базой студентов, групп и преподавателей</title>
    <script src ="scripts/jquery-3.4.1.min.js"></script>
    <script src ="scripts/functions.js"></script>
    <script src ="scripts/groups/handlers.js"></script>
    <script src ="scripts/groups/requests.js"></script>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <meta charset="UTF-8">
</head>
<body>
<div id="wrapper">
    <nav>
        <ul>
            <li><a id="homeAction" href="/home">H</a></li>
            <li><a id="studentAction" href="student.jsp">Студенты</a></li>
            <li><a class="currentPage" id="groupAction"  href="group.jsp">Группы</a></li>
            <li><a id="teacherAction"  href="teacher.jsp">Преподаватели</a></li>
        </ul>
    </nav>
    <div id="status"></div>
    <div id="content">
        <div id="groupsTable">
            <ul class="actions" id="groupNav">
                <li><a id="groupAdd" href="#groupAdd">Добавить</a></li>
                <li><a id="groupSearch" href="#groupSearch">Поиск</a></li>
            </ul>
            <form id="groupAddForm" name="groupAddForm" method="POST">
                <p>Добавление группы</p>
                <input type="text" class="group" name="group" placeholder="Номер группы">
                <input type="submit" class="submit" value="Go">
                <button id="groupAddFormClose">X</button>
            </form>
            <form id="groupUpdateForm" name="studentUpdateForm" method="POST">
                <p>Обновление группы</p>
                <input disabled type="hidden" class="id" name="id">
                <input class="group" type="text" name="group" placeholder="Группа">
                <input type="submit" class="submit" value="Go">
                <button id="groupUpdateFormClose">X</button>
            </form>
            <form id="groupSearchForm" name="studentSearchForm" method="GET">
                <p>Поиск групп</p>
                <input class="group" type="text" name="group" placeholder="Группа">
                <button id="groupSearchFormClose">X</button>
            </form>

            <div id="groupOutput">
                <table class="outputTable display"></table>
            </div>
        </div>
    </div>
    <footer>
        Подвальчик
    </footer>
</div>

<div id="groupInfo">
    Группа: <span id="groupNumber"></span>
    <a id="closeGroupInfo" href="#closeGroupInfo">X</a>
    <div id="info">
        Преподаватели:
        <div id="groupTeachersTable"></div><br>
        Студенты:
        <span id="groupStudentsTable"></span>
    </div>
</div>
</body>
</html>
