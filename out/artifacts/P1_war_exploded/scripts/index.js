$(document).ready(function(){
    var successColor = "#CACACA";
    var failColor = "red";
    var title = "Информационная система для работы с базой студентов, групп и преподавателей";
    /** Выбор таблицы **/

    /** Студент **/
    $("#studentAction").click(function () {
        $("title").html("Студенты | " + title);
        $("#status").show();
        $("#studentsTable").show();
        $("#groupsTable").hide();
        $("#teachersTable").hide();

        $("#status").html("Загрузка...");

        $.post("studentSelectAll", function(data) {
            $("#studentOutput .outputTable").show();
            $("#studentOutput .outputTable").html("");

            data = JSON.parse(data);
            $("#studentOutput .outputTable").append(
                "\t<tr>\n" +
                "\t\t<td>ID</td>\n" +
                "\t\t<td>ФИО</td>\n" +
                "\t\t<td>День рождения</td>\n" +
                "\t\t<td>Пол</td>\n" +
                "\t\t<td>Группа</td>\n" +
                "\t\t<td>Операции</td>\n" +
                "\t</tr>"
            );
            var students = data.students;
            students.forEach(function(student, i, students) {
                addStudentRow(student);
            });

            $("#status").html("");

            addDeleteEventHandler("#studentOutput .outputTable tr",
                "student");
            addUpdateEventHandler("#studentOutput .outputTable tr",
                "student");
        });
    });
    $("#studentAdd").click(function () {
        $("#studentAddForm").show();
        $("#studentUpdateForm").hide();
        $("#studentSearchForm").hide();
    })
    $("#studentAddForm").submit(function (event) {
        event.preventDefault();
        $("#status").html("Загрузка...");
        var name = $("#studentAddForm .name").val();
        var birth = $("#studentAddForm .birth").val();
        var gender = $("#studentAddForm .gender").val();
        var group = $("#studentAddForm .group").val();
        $.post("studentAdd", $(this).serialize(), function(data) {
            $("#status").html(data);
            var id = parseInt($("#status .lastId").html());
            if(!isNaN(id)) {
                $("#studentOutput .outputTable").append("<tr id=\"student" + id + "\">" +
                    "<td class=\"id\">" + id + "</td>" +
                    "<td class=\"name\">" + name + "</td>" +
                    "<td class=\"birth\">" + birth + "</td>" +
                    "<td class=\"gender\">" + gender + "</td>" +
                    "<td class=\"group\">" + group + "</td>" +
                    "<td class=\"opeations\">" +
                    "<a class=\"delete\" href=\"#deleteStudent" + id + "\">" +
                    "Удалить" +
                    "</a>" +
                    "<br>" +
                    "<a class=\"update\" href=\"#updateStudent" + id + "\">" +
                    "Изменить" +
                    "</a>" +
                    "</td>" +
                    "</tr>");
                lightOn("#student" + id,successColor);
                addDeleteEventHandler("#student"+id,
                    "student");
                addUpdateEventHandler("#student"+id,
                    "student");
            }
        });
    })
    $("#studentUpdateForm").submit(function (event) {
        event.preventDefault();
        $("#status").html("Загружаю...");
        var id =  $("#studentUpdateForm .id").val();
        var name =  $("#studentUpdateForm .name").val();
        var birth =  $("#studentUpdateForm .birth").val();
        var gender =$("#studentUpdateForm .gender").val();
        var group = $("#studentUpdateForm .group").val();
        $.post("studentUpdate", {"id" : id,
            "name" : name,
            "birth" : birth,
            "gender" : gender,
            "group" : group}, function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                lightOn("#student" + id,successColor);
                $("#student" + id + " .name").html(name);
                $("#student" + id + " .birth").html(birth);
                $("#student" + id + " .gender").html(gender);
                $("#student" + id + " .group").html(group);
                $("#status").html("");
            } else {
                lightOn("#student" + id,failColor);
            }
        });
    });
    $("#studentSearchForm").submit(function (event) {
        event.preventDefault();
        $.get("studentSelect", $(this).serialize(), function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                $("#studentOutput .outputTable").html(data);
                $("#status").html("");
            }
            addDeleteEventHandler("tr", "student");
            addUpdateEventHandler("tr", "student");
        });
    })
    /** /Студент **/

    /** Группа **/
    $("#groupAction").click(function () {
        $("title").html("Группы | " + title);
        $("#status").show();
        $("#studentsTable").hide();
        $("#groupsTable").show();
        $("#teachersTable").hide();

        $("#status").html("Загрузка...");

        $.post("groupSelectAll", function(data) {
            $("#groupOutput .outputTable").show();
            $("#groupOutput .outputTable").html(data);
            $("#status").html("");

            addDeleteEventHandler("#groupOutput .outputTable tr",
                "group");
            addUpdateEventHandler("#groupOutput .outputTable tr",
                "group");
            addGroupInfoEventHandler("tr");
            addGetTeachersHandler("tr");
        });
    });
    $("#groupAdd").click(function () {
        $("#groupAddForm").show();
        $("#groupUpdateForm").hide();
        $("#groupSearchForm").hide();
    })
    $("#groupAddForm").submit(function (event) {
        event.preventDefault();
        $("#status").html("Загрузка...");
        var group = $("#groupAddForm .group").val();
        $.post("groupAdd", {
            "group" : group}, function(data) {
            $("#status").html(data);
            var id = parseInt($("#status .lastId").html());
            if(!isNaN(id)) {
                $("#groupOutput .outputTable").append(
                    "<tr id=\"group" + id + "\">" +
                    "<td class=\"id\">" + id + "</td>" +
                    "<td class=\"group\">" + group + "</td>" +
                    "<td class=\"opeations\">" +
                    "<a class=\"delete\" href=\"#deleteGroup" + id + "\">" +
                    "Удалить" +
                    "</a>" +
                    "<br>" +
                    "<a class=\"update\" href=\"#updateGroup" + id + "\">" +
                    "Изменить" +
                    "</a>" +
                    "<br>" +
                    "<a class=\"getInfo\" href=\"#getInfoGroup" + id + "\">" +
                    "Информация" +
                    "</a>" +
                    "<br>" +
                    "<a class=\"putTeacherInGroup\" href=\"#putTeacherInGroup" + id + "\">" +
                    "Назначить преподавателя" +
                    "</a>" +
                    "</td>" +
                    "</tr>");
                lightOn("#group" + id,successColor);

                addDeleteEventHandler("#group"+id,
                    "group");
                addUpdateEventHandler("#group"+id,
                    "group");
                addGroupInfoEventHandler("#group"+id);
                addGetTeachersHandler("#group"+id);
            }
        });
    })
    $("#groupUpdateForm").submit(function (event) {
        event.preventDefault();
        $("#status").html("Загружаю...");
        var id =  $("#groupUpdateForm .id").val();
        var group = $("#groupUpdateForm .group").val();
        console.log(id + " " + group);
        $.post("groupUpdate", {"id" : id, "group" : group}, function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                lightOn("#group" + id,successColor);
                $("#group" + id + " .group").html(group);
                $("#status").html("");
            } else {
                lightOn("#group" + id,failColor);
            }
        });
    });
    $("#groupSearchForm .group").keyup(function () {
        var group = $(this).val();
        $.get("groupSelect", {"group" : group}, function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                $("#groupOutput .outputTable").html(data);
                $("#status").html("");
            } else {
                $("#status").html("Группы с введенным номером не существует.");
            }
            addDeleteEventHandler("tr", "group");
            addUpdateEventHandler("tr", "group");
            addGroupInfoEventHandler("tr");
            addGetTeachersHandler("tr");
        });
    })
    /** /Группа **/

    /** Преподаватель **/
    $("#teacherAction").click(function () {
        $("title").html("Преподаватели | " + title);
        $("#status").show();
        $("#studentsTable").hide();
        $("#groupsTable").hide();
        $("#teachersTable").show();

        $("#status").html("Загрузка...");
        $.post("teacherSelectAll", function(data) {
            $("#teacherOutput .outputTable").show();
            $("#teacherOutput .outputTable").html(data);
            $("#status").html("");

            addDeleteEventHandler("#teacherOutput .outputTable tr",
                "teacher");
            addUpdateEventHandler("#teacherOutput .outputTable tr",
                "teacher");
            addTeacherInfoEventHandler("tr");
            addGetTeachersHandler("tr");
        });
    });
    $("#teacherAdd").click(function () {
        $("#teacherAddForm").show();
        $("#teacherUpdateForm").hide();
        $("#teacherSearchForm").hide();
    })
    $("#teacherAddForm").submit(function (event) {
        event.preventDefault();
        $("#status").html("Загрузка...");
        var name = $("#teacherAddForm .name").val();
        var birth = $("#teacherAddForm .birth").val();
        var gender = $("#teacherAddForm .gender").val();
        $.post("teacherAdd", $(this).serialize(), function(data) {
            $("#status").html(data);
            var id = parseInt($("#status .lastId").html());
            if(!isNaN(id)) {
                $("#teacherOutput .outputTable").append("<tr id=\"teacher" + id + "\">" +
                    "<td class=\"id\">" + id + "</td>" +
                    "<td class=\"name\">" + name + "</td>" +
                    "<td class=\"birth\">" + birth + "</td>" +
                    "<td class=\"gender\">" + gender + "</td>" +
                    "<td class=\"opeations\">" +
                    "<a class=\"delete\" href=\"#deleteTeacher" + id + "\">" +
                    "Удалить" +
                    "</a>" +
                    "<br>" +
                    "<a class=\"update\" href=\"#updateTeacher" + id + "\">" +
                    "Изменить" +
                    "</a>" +
                    "<br>" +
                    "<a class=\"getTeacherInfo\" href=\"#getInfoTeacher" + id + "\">" +
                    "Информация" +
                    "</a>" +
                    "</td>" +
                    "</tr>");
                lightOn("#teacher" + id,successColor);
                addDeleteEventHandler("#teacher"+id, "teacher");
                addUpdateEventHandler("#teacher"+id, "teacher");
                addTeacherInfoEventHandler("#teacher"+id);
            }
        });
    })
    $("#teacherUpdateForm").submit(function (event) {
        event.preventDefault();
        $("#status").html("Загружаю...");
        var id =  $("#teacherUpdateForm .id").val();
        var name =  $("#teacherUpdateForm .name").val();
        var birth =  $("#teacherUpdateForm .birth").val();
        var gender =$("#teacherUpdateForm .gender").val();
        $.post("teacherUpdate", {
            "id" : id,
            "name" : name,
            "birth" : birth,
            "gender" : gender},
            function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                lightOn("#teacher" + id,successColor);
                $("#teacher" + id + " .name").html(name);
                $("#teacher" + id + " .birth").html(birth);
                $("#teacher" + id + " .gender").html(gender);
                $("#status").html("");
            } else {
                lightOn("#teacher" + id,failColor);
            }
        });
    });
    $("#teacherSearchForm").submit(function (event) {
        event.preventDefault();
        $.get("teacherSelect", $(this).serialize(), function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                $("#teacherOutput .outputTable").html(data);
                $("#status").html("");
            }
            addDeleteEventHandler("tr", "teacher");
            addUpdateEventHandler("tr", "teacher");
            addTeacherInfoEventHandler("tr");
        });
    })
    $("#putTeacherInGroup").submit(function (event) {
        event.preventDefault();
        var teacherId = $("#teacherInfo #teacherId").html();
        var groupNumber = $("#putTeacherInGroup .group").val();
        console.log(teacherId + " " + groupNumber);

        $.post("teacherPutInGroupByNumber", {
            "teacherId" : teacherId,
            "groupNumber" : groupNumber}, function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {

                var emptyCheck = parseInt($("#teacherInfo .error").html());
                if(!isNaN(emptyCheck)) {
                    //alert(1); // создать таблицу
                    var table = "\t<table><tr>\n" +
                        "\t\t<td style=\"display: none;\">ID преподавателя</td>\n" +
                        "\t\t<td>Номер</td>\n" +
                        "\t\t<td>Операции</td>\n" +
                        "\t</tr>";
                    table += "\t<tr id=\"groupTeacher" + $("#status .lastId").html() + "\">\n" +
                        "\t\t<td class=\"teacherId\" style=\"display: none;\">"
                        + teacherId +
                        "</td>\n" +
                        "\t\t<td class=\"number\">" + groupNumber + "</td>\n" +
                        "\t\t<td>" +
                        "<a class=\"removeTeacherFromGroup\" href=\"#removeGroupFromTeacher" + $("#status .lastId").html() + "FromGroup\">" +
                        "Убрать группу" +
                        "</a>" +
                        "</td>\n" +
                        "\t</tr>";
                    table += "</table>";
                    $("#groups").html(table);
                    addGroupRemovingFromTeacherHandler("#groupTeacher" + $("#status .lastId").html());
                } else {
                    $("#teacherInfo table").append(
                        "\t<tr id=\"groupTeacher" + $("#status .lastId").html() + "\">\n" +
                        "\t\t<td class=\"teacherId\" style=\"display: none;\">"
                        + teacherId +
                        "</td>\n" +
                        "\t\t<td class=\"number\">"
                        + groupNumber +
                        "</td>\n" +
                        "\t\t<td>" +
                        "<a class=\"removeTeacherFromGroup\" " +
                        "href=\"#removeGroupFromTeacher" + $("#status .lastId").html() + "FromGroup\">" +
                        "Убрать группу" +
                        "</a>" +
                        "</td>\n" +
                        "\t</tr>");
                    addGroupRemovingFromTeacherHandler(
                        "#groupTeacher" + $("#status .lastId").html()
                    );
                }
                $("#status").html("");
            }
        });
    })
    /** /Преподаватель **/

    /** /Выбор таблицы **/
});