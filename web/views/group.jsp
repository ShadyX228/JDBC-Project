<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <title> Группы | Информационная система для работы с базой студентов, групп и преподавателей</title>
        <script src ="../resources/scripts/jquery-3.4.1.min.js"></script>
        <script src ="../resources/scripts/groups/handlers.js"></script>
        <script src ="../resources/scripts/groups/requests.js"></script>
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
                    <li><a id="studentAction" href="student">Студенты</a></li>
                    <li><a class="currentPage" id="groupAction" href="group">Группы</a></li>
                    <li><a id="teacherAction"  href="teacher">Преподаватели</a></li>
                </ul>
            </nav>
            <div id="status"></div>
            <div id="content">
                <div id="groupsTable">
                    <ul class="actions" id="groupNav">
                        <li><a id="groupAdd" href="#groupAdd">Добавить</a></li>
                    </ul>
                    <form id="groupAddForm" name="groupAddForm" method="POST">
                        <fieldset>
                            <legend>Добавление группы</legend>
                            <input type="text" class="group" name="group" placeholder="Номер группы">
                            <input type="submit" class="submit" value="Go">
                            <button id="groupAddFormClose">X</button>
                        </fieldset>
                    </form>
                    <form id="groupUpdateForm" name="groupUpdateForm" method="POST">
                        <fieldset>
                            <legend>Обновление группы</legend>
                            <input type="hidden" class="id" name="id">
                            <input class="group" type="text" name="newNumber" placeholder="Группа">
                            <input type="submit" class="submit" value="Go">
                            <button id="groupUpdateFormClose">X</button>
                        </fieldset>
                    </form>
                    <div id="groupOutput">
                        <table class="outputTable display">
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
            <footer>
                Подвальчик
            </footer>
        </div>

        <div id="groupInfo">
            Группа: <span id="groupNumber"></span>
            <a id="closeGroupInfo" href="#closeGroupInfo">X</a>
            <div id="info">
                Преподаватели группы:
                <div id="groupTeachersTable">
                    <table class="display">
                        <thead>
                            <tr>
                                <th>ФИО</th>
                                <th>Операции</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div><br>
                Свободные преподаватели:
                <div id="freeTeachersTable">
                    <table class="display">
                        <thead>
                        <tr>
                            <th>ФИО</th>
                            <th>Операции</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                Студенты:
                <span id="groupStudentsTable"></span>
            </div>
        </div>
    </body>
</html>
