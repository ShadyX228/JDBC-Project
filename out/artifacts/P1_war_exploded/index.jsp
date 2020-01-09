<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <title>Информационная система для работы с базой студентов, групп и преподавателей</title>
        <script src ="scripts/jquery-3.4.1.min.js"></script>
        <script src ="scripts/functions.js"></script>
        <script src ="scripts/handlers.js"></script>
        <script src ="scripts/index.js"></script>
        <!--<script src ="scripts/datatables.js"></script>-->
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <%--<link rel="stylesheet" type="text/css" href="css/datatables.css">--%>
        <meta charset="UTF-8">
    </head>
    <body>
    <div id="wrapper">
        <nav>
            <ul>
                <li><a id="studentAction" href="#student">Студенты</a></li>
                <li><a id="groupAction"  href="#group">Группы</a></li>
                <li><a id="teacherAction"  href="#teacher">Преподаватели</a></li>
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
                        <table class="outputTable"></table>
                    </div>
                </div>
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
                        <table class="outputTable">
                        </table>
                    </div>
                </div>
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
                        <table class="outputTable"></table>
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
