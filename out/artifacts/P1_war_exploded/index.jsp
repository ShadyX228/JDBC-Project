<%@ page import="utilfactories.JPAUtil" %><%--
  Created by IntelliJ IDEA.
  User: MagomedovIM
  Date: 21.11.2019
  Time: 13:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <script type="text/javascript" src ="scripts/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src ="scripts/index.js"></script>
    <link rel="stylesheet" type="text/css" href="css/style.css">
  </head>
  <body>
    <div  id="wrapper">
      <header>Шапка</header>

      <!--Выбор таблицы-->
      <div id="forms">
        <form id="table">
          <select>
            <option value="0">Выберите таблицу</option>
            <option value="1">Студент</option>
            <option value="2">Преподаватель</option>
            <option value="3">Группа</option>
          </select>
        </form>
        <form id="studentAction">
          <select>
            <option value="0">Выберите действие</option>
            <option value="1">Добавить</option>
            <option value="2">Выбрать</option>
            <option value="3">Обновить</option>
            <option value="4">Удалить</option>
          </select>
        </form>
        <form id="teacherAction">
          <select>
            <option value="0">Выберите действие</option>
            <option value="1">Добавить</option>
            <option value="2">Выбрать</option>
            <option value="3">Обновить</option>
            <option value="4">Удалить</option>
            <option value="5">Назначить группу</option>
            <option value="6">Выбрать все группы</option>
            <option value="7">Удалить группу</option>
          </select>
        </form>
        <form id="groupAction">
          <select>
            <option value="0">Выберите действие</option>
            <option value="1">Добавить</option>
            <option value="2">Выбрать</option>
            <option value="3">Удалить</option>
          </select>
        </form>
      <!--/Выбор таблицы-->

      <!--Действия со студентами-->
        <form id="studentAdd" name="studentAdd" method="POST">
          <input type="text" class="name" name="name" placeholder="ФИО"><br>
          <input type="text" class="birth" name="birth" placeholder="День рождения"><br>

          <fieldset class="gender" form="studentAdd">
            <legend>Пол</legend>
            <label for="studentM">
              <input  type="radio" id="studentM" name="gender" value="MALE">М
            </label>
            <label for="studentF">
              <input type="radio" id="studentF" name="gender" value="FEMALE">Ж<br>
            </label>
          </fieldset>


          <input class="group" type="text" name="group" placeholder="Группа"><br>
          <button class="submit" type="button">Go</button>
        </form>
        <form id="studentSelect" name="studentSelect" method="GET">
          <select name="criteria">
            <option value="default">Выберите критерий выборки</option>
            <option value="id">ID</option>
            <option value="name">Имя</option>
            <option value="birth">День рождения</option>
            <option value="gender">Пол</option>
            <option value="group">Группа</option>
            <option value="all">Все</option>
          </select>
          <input type="text" class="criteriaValue" name="criteriaValue" placeholder="Значение критерия"><br>
          <button class="submit" type="button">Go</button>
        </form>
        <form id="studentUpdate" name="studentUpdate" method="POST">
          <select name="criteria">
            <option value="default">Выберите поле, которое надо обновить</option>
            <option value="name">Имя</option>
            <option value="birth">День рождения</option>
            <option value="gender">Пол</option>
            <option value="group">Группа</option>
          </select>
          <input type="text" class="criteriaValue" name="criteriaValue" placeholder="Значение поля"><br>
          <button class="submit" type="button">Go</button>
        </form>
        <form id="studentDelete" name="studentDelete" method="GET">
          <select name="criteria">
            <option value="default">Выберите критерий удаления</option>
            <option value="id">ID</option>
            <option value="name">Имя</option>
            <option value="birth">День рождения</option>
            <option value="gender">Пол</option>
            <option value="group">Группа</option>
          </select>
          <input type="text" class="criteriaValue" name="criteriaValue" placeholder="Значение критерия"><br>
          <button class="submit" type="button">Go</button>
        </form>
      <!--/Действия со студентами-->

      <!--Действия с преподавателями-->
      <!--/Действия с преподавателями-->

      <!--Действия с группами-->
      <!--/Действия с группами-->
      </div>

      <div id="output">
        <p class="message"> ${message} </p>
        <p class="output"> ${output} </p>
        <p class="errors"> ${errors} </p>
      </div>

      <footer>Подвал</footer>
    </div>
  </body>
</html>
