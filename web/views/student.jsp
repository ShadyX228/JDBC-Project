<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <title>Студенты | Информационная система для работы с базой студентов, групп и преподавателей</title>
        <script src ="../resources/scripts/jquery-3.4.1.min.js"></script>
        <script src ="../resources/scripts/students/handlers.js"></script>
        <script src ="../resources/scripts/students/requests.js"></script>
        <script src ="../resources/scripts/datatables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="../resources/css/style.css">
        <link rel="stylesheet" type="text/css" href="../resources/css/datatables.min.css">
        <meta charset="UTF-8">
    </head>
    <body>
        <div id="wrapper">
            <nav>
                <ul>
                    <li><a id="homeAction" href="/">H</a></li>
                    <li><a class="currentPage" id="studentAction" href="student">Студенты</a></li>
                    <li><a id="groupAction" href="group">Группы</a></li>
                    <li><a id="teacherAction" href="teacher">Преподаватели</a></li>
                </ul>
            </nav>
            <div id="status"></div>
            <div id="content">
                <div id="studentsTable">
                    <ul class="actions" id="studentNav">
                        <li><a id="studentAdd" href="#studentAddForm">Добавить</a></li>
                    </ul>
                    <form id="studentAddForm" name="studentAddForm" method="POST">
                        <fieldset>
                            <legend>Добавление студента</legend>
                            <input type="text" class="name" name="name" placeholder="ФИО">
                            <input type="date" class="birth" name="birthday">
                            <select name="gender" class="gender">
                                <option value="MALE">М</option>
                                <option value="FEMALE">Ж</option>
                            </select>
                            <input class="groupNum" type="text" name="groupNum" placeholder="Группа">
                            <input class="group" type="hidden" name="group" placeholder="ID группы">
                            <input type="submit" class="submit" value="Go">
                            <button id="studentAddFormClose">X</button>
                        </fieldset>
                    </form>
                    <form id="studentUpdateForm" name="studentUpdateForm" method="POST">
                        <fieldset>
                            <legend>Обновление студента</legend>
                            <input type="hidden" class="id" name="id">
                            <input type="text" class="name" name="name" placeholder="ФИО">
                            <input type="date" class="birth" name="birth">
                            <select name="gender" class="gender">
                                <option value="MALE">М</option>
                                <option value="FEMALE">Ж</option>
                            </select>
                            <input class="group" type="text" name="group" placeholder="Группа">
                            <input type="submit" class="submit" value="Go">
                            <button id="studentUpdateFormClose">X</button>
                        </fieldset>
                    </form>

                    <div id="studentOutput">
                        <table class="outputTable display">
                            <thead>
                            <tr>
                                <th>ФИО</th>
                                <th>День рождения</th>
                                <th>Пол</th>
                                <th>Группа</th>
                                <th>Операции</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <footer>
                Подвальчик
            </footer>
        </div>
    </body>
</html>
