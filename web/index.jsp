<%@ page import="dbmodules.dao.StudentDAO" %>
<%@ page import="dbmodules.tables.Teacher" %>
<%@ page import="static dbmodules.types.Criteria.ALL" %>
<%@ page import="dbmodules.tables.Student" %><%--
  Created by IntelliJ IDEA.
  User: MagomedovIM
  Date: 21.11.2019
  Time: 13:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
      <title>$Title$</title>
      <script type="text/javascript" src ="scripts/jquery-3.4.1.min.js"></script>
      <script type="text/javascript" src ="scripts/index.js"></script>
      <link rel="stylesheet" type="text/css" href="css/style.css">
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
                        <input type="date" class="birth" name="birth" placeholder="День рождения">
                        <select name="gender" class="gender">
                            <option value="MALE">М</option>
                            <option value="FEMALE">Ж</option>
                        </select>
                        <input class="group" type="text" name="group" placeholder="Группа">
                        <input type="submit" class="submit" type="button" value="Go">
                        <button id="studentAddFormClose">X</button>
                    </form>
                    <form id="studentUpdateForm" name="studentUpdateForm" method="POST">
                        <p>Обновление студента</p>
                        <input disabled type="hidden" class="id" name="id" placeholder="id">
                        <input type="text" class="name" name="name" placeholder="ФИО">
                        <input type="date" class="birth" name="birth" placeholder="День рождения">
                        <select name="gender" class="gender">
                            <option value="MALE">М</option>
                            <option value="FEMALE">Ж</option>
                        </select>
                        <input class="group" type="text" name="group" placeholder="Группа">
                        <input type="submit" class="submit" type="button" value="Go">
                        <button id="studentUpdateFormClose">X</button>
                    </form>
                    <form id="studentSearchForm" name="studentSearchForm" method="POST">
                        <p>Поиск студентов</p>
                        <input type="text" class="id" name="id" placeholder="id">
                        <input type="text" class="name" name="name" placeholder="ФИО">
                        <input type="date" class="birth" name="birth" placeholder="День рождения">
                        <select name="gender" class="gender">
                            <option>Пол</option>
                            <option value="MALE">М</option>
                            <option value="FEMALE">Ж</option>
                        </select>
                        <input class="group" type="text" name="group" placeholder="Группа">
                        <input type="submit" class="submit" type="button" value="Go">
                        <button id="studentSearchFormClose">X</button>
                    </form>

                    <div id="studentOutput">
                        <table class="outputTable"></table>
                    </div>
                </div>
                <div id="groupsTable">
                    <ul class="actions" id="groupNav">
                        <li><a id="groupAdd" href="#groupAdd">Добавить</a></li>
                        <li><a id="groupSearch" href="#groupSearch">Выбрать</a></li>
                    </ul>
                    <form id="groupAddForm" name="groupAddForm" method="POST">
                        <p>Добавление группы</p>
                        <input type="text" class="group" name="group" placeholder="Номер группы">
                        <input type="submit" class="submit" type="button" value="Go">
                        <button id="groupAddFormClose">X</button>
                    </form>
                    <form id="groupUpdateForm" name="studentUpdateForm" method="POST">
                        <p>Обновление группы</p>
                        <input disabled type="hidden" class="id" name="id" placeholder="id">
                        <input class="group" type="text" name="group" placeholder="Группа">
                        <input type="submit" class="submit" type="button" value="Go">
                        <button id="groupUpdateFormClose">X</button>
                    </form>
                    <form id="groupSearchForm" name="studentSearchForm" method="GET">
                        <p>Обновление группы</p>
                        <input class="group" type="text" name="group" placeholder="Группа">
                        <input type="submit" class="submit" type="button" value="Go">
                        <button id="groupSearchFormClose">X</button>
                    </form>

                    <div id="groupOutput">
                        <table class="outputTable">
                        </table>
                    </div>
                </div>
                <div id="teachersTable">
                  Преподы
                </div>
              </div>
        </div>
        <div id="groupInfo">
            Группа: <span id="groupNumber"></span>
            <a id="closeGroupInfo" href="#closeGroupInfo">X</a>
            <div id="info"></div>
        </div>
    </body>
</html>
