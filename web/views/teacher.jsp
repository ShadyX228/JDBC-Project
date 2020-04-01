<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <title> Преподаватели | Информационная система для работы с базой студентов, групп и преподавателей</title>
        <script src ="../resources/scripts/jquery-3.4.1.min.js"></script>
        <script src ="../resources/scripts/teachers/handlers.js"></script>
        <script src ="../resources/scripts/teachers/requests.js"></script>
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
                    <li><a id="studentAction" href="/student">Студенты</a></li>
                    <li><a id="groupAction" href="/group">Группы</a></li>
                    <li><a class="currentPage" id="/teacher"  href="teacher">Преподаватели</a></li>
                </ul>
            </nav>
            <div id="status"></div>
            <div id="content">
                <div id="teachersTable">
                    <ul class="actions" id="teacherNav">
                        <li><a id="teacherAdd" href="#teacherAdd">Добавить</a></li>
                    </ul>
                    <form id="teacherAddForm" name="teacherAddForm" method="POST">
                        <fieldset>
                            <legend>Добавление преподавателя</legend>
                            <input type="text" class="name" name="name" placeholder="ФИО">
                            <input type="date" class="birth" name="birthday">
                            <select name="gender" class="gender">
                                <option value="MALE">М</option>
                                <option value="FEMALE">Ж</option>
                            </select>
                            <input type="submit" class="submit" value="Go">
                            <button id="teacherAddFormClose">X</button>
                        </fieldset>
                    </form>
                    <form id="teacherUpdateForm" name="teacherUpdateForm" method="POST">
                        <fieldset>
                            <legend>Обновление преподавателя</legend>
                            <input type="hidden" class="id" name="id">
                            <input type="text" class="name" name="name" placeholder="ФИО">
                            <input type="date" class="birth" name="birth">
                            <select name="gender" class="gender">
                                <option value="MALE">М</option>
                                <option value="FEMALE">Ж</option>
                            </select>
                            <input type="submit" class="submit" value="Go">
                            <button id="teacherUpdateFormClose">X</button>
                        </fieldset>
                    </form>

                    <div id="teacherOutput">
                        <table class="outputTable display">
                            <thead>
                            <tr>
                                <th>ФИО</th>
                                <th>День рождения</th>
                                <th>Пол</th>
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

        <div id="teacherInfo">
            Преподаватель: <span id="teacherName"></span> <span id="teacherId"></span>
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
            <div id="info">
                <fieldset>
                    <legend>
                        Группы
                    </legend>
                </fieldset>
                <div id="groups">
                    <table class="display">
                        <thead>
                        <tr>
                            <th>Номер</th>
                            <th>Операции</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
