<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title> Преподаватели | Информационная система для работы с базой студентов, групп и преподавателей</title>
    <script src ="scripts/jquery-3.4.1.min.js"></script>
    <script src ="scripts/functions.js"></script>
    <script src ="scripts/teachers/handlers.js"></script>
    <script src ="scripts/teachers/requests.js"></script>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <meta charset="UTF-8">
</head>
    <body>
        <div id="wrapper">
            <nav>
                <ul>
                    <li><a id="homeAction" href="/home">H</a></li>
                    <li><a id="studentAction" href="student.jsp">Студенты</a></li>
                    <li><a id="groupAction"  href="group.jsp">Группы</a></li>
                    <li><a class="currentPage" id="teacherAction"  href="teacher.jsp">Преподаватели</a></li>
                </ul>
            </nav>
            <div id="status"></div>
            <div id="content">
                <div id="teachersTable">
                    <ul class="actions" id="teacherNav">
                        <li><a id="teacherAdd" href="#teacherAdd">Добавить</a></li>
                        <li><a id="teacherSearch" href="#teacherSearch">Поиск</a></li>
                    </ul>
                    <form id="teacherAddForm" name="teacherAddForm" method="POST">
                        <p>Добавление преподавателя</p>
                        <input type="text" class="name" name="name" placeholder="ФИО">
                        <input type="date" class="birth" name="birth">
                        <select name="gender" class="gender">
                            <option value="MALE">М</option>
                            <option value="FEMALE">Ж</option>
                        </select>
                        <input type="submit" class="submit" value="Go">
                        <button id="teacherAddFormClose">X</button>
                    </form>
                    <form id="teacherUpdateForm" name="teacherUpdateForm" method="POST">
                        <p>Обновление преподавателя</p>
                        <input disabled type="hidden" class="id" name="id">
                        <input type="text" class="name" name="name" placeholder="ФИО">
                        <input type="date" class="birth" name="birth">
                        <select name="gender" class="gender">
                            <option value="MALE">М</option>
                            <option value="FEMALE">Ж</option>
                        </select>
                        <input type="submit" class="submit" value="Go">
                        <button id="teacherUpdateFormClose">X</button>
                    </form>
                    <form id="teacherSearchForm" name="teacherSearchForm" method="POST">
                        <p>Поиск преподавателей</p>
                        <input type="text" class="id" name="id" placeholder="id">
                        <input type="text" class="name" name="name" placeholder="ФИО">
                        <input type="date" class="birth" name="birth">
                        <select name="gender" class="gender">
                            <option>Пол</option>
                            <option value="MALE">М</option>
                            <option value="FEMALE">Ж</option>
                        </select>
                        <input type="submit" class="submit" value="Go">
                        <button id="teacherSearchFormClose">X</button>
                    </form>

                    <div id="teacherOutput">
                        <table class="outputTable display"></table>
                    </div>
                </div>
            </div>
            <footer>
                Подвальчик
            </footer>
        </div>

        <div id="teacherInfo">
            Преподаватели: <span id="teacherName"></span>, <span id="teacherId"></span>
            <a id="closeTeacherInfo" href="#closeTeacherInfo">X</a>
            <fieldset>
                <legend>
                    Назначить преподавателя в группу
                </legend>
                <form id="putTeacherInGroup" name="putTeacherInGroup">
                    <input type="text" class="group" placeholder="Номер группы">
                    <input type="submit" class="submit" value="Go">
                </form>
            </fieldset>
            Группы:
            <div id="groups"></div>
        </div>
    </body>
</html>
