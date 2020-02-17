<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Студенты | Информационная система для работы с базой студентов, групп и преподавателей</title>
    <script src ="scripts/jquery-3.4.1.min.js"></script>
    <script src ="scripts/functions.js"></script>
    <script src ="scripts/students/handlers.js"></script>
    <script src ="scripts/students/requests.js"></script>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <meta charset="UTF-8">
</head>
    <body>
        <div id="wrapper">
            <nav>
                <ul>
                    <li><a id="homeAction" href="/home">H</a></li>
                    <li><a class="currentPage" id="studentAction" href="student.jsp">Студенты</a></li>
                    <li><a id="groupAction"  href="group.jsp">Группы</a></li>
                    <li><a id="teacherAction"  href="teacher.jsp">Преподаватели</a></li>
                </ul>
            </nav>
            <div id="status"></div>
            <div id="content">
                <div id="studentsTable">
                    <ul class="actions" id="studentNav">
                        <li><a id="studentAdd" href="#studentAddForm">Добавить</a></li>
                        <li><a id="studentSearch" href="#studentSearchForm">Поиск</a></li>
                    </ul>
                    <form id="studentAddForm" name="studentAddForm" method="POST">
                        <p>Добавление студента</p>
                        <input type="text" class="name" name="name" placeholder="ФИО">
                        <input type="date" class="birth" name="birth">
                        <select name="gender" class="gender">
                            <option value="MALE">М</option>
                            <option value="FEMALE">Ж</option>
                        </select>
                        <input class="group" type="text" name="group" placeholder="Группа">
                        <input type="submit" class="submit" value="Go">
                        <button id="studentAddFormClose">X</button>
                    </form>
                    <form id="studentUpdateForm" name="studentUpdateForm" method="POST">
                        <p>Обновление студента</p>
                        <input disabled type="hidden" class="id" name="id">
                        <input type="text" class="name" name="name" placeholder="ФИО">
                        <input type="date" class="birth" name="birth">
                        <select name="gender" class="gender">
                            <option value="MALE">М</option>
                            <option value="FEMALE">Ж</option>
                        </select>
                        <input class="group" type="text" name="group" placeholder="Группа">
                        <input type="submit" class="submit" value="Go">
                        <button id="studentUpdateFormClose">X</button>
                    </form>
                    <form id="studentSearchForm" name="studentSearchForm" method="POST">
                        <p>Поиск студентов</p>
                        <input type="text" class="id" name="id" placeholder="id">
                        <input type="text" class="name" name="name" placeholder="ФИО">
                        <input type="date" class="birth" name="birth">
                        <select name="gender" class="gender">
                            <option>Пол</option>
                            <option value="MALE">М</option>
                            <option value="FEMALE">Ж</option>
                        </select>
                        <input class="group" type="text" name="group" placeholder="Группа">
                        <input type="submit" class="submit" value="Go">
                        <button id="studentSearchFormClose">X</button>
                    </form>

                    <div id="studentOutput">
                        <table class="outputTable display"></table>
                    </div>
                </div>
            </div>
            <footer>
                Подвальчик
            </footer>
        </div>

    </body>
</html>
