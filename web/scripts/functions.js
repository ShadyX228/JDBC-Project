/** Шаблоны **/
var groupInfoTemplate = "Преподаватели: " +
    "<div id=\"groupTeachersTable\"></div><br>" +
    "Студенты: " +
    "<span id=\"groupStudentsTable\"></span>";
/** /Шаблоны **/

/** Функции **/
function addDeleteEventHandler(selector, table) {
    $(selector).on("click", ".delete", function() {
        $("#studentUpdateForm").hide();
        $("#teacherUpdateForm").hide();
        $("#groupUpdateForm").hide();

        var a = $(this);
        var href = a.attr("href");
        var id = parseInt(href.match(/\d+/));
        $("#status").html("Загружаю...");
        var page = "";
        switch (table) {
            case "student" : {
                page = "studentDelete";
                break;
            }
            case "teacher" : {
                page = "teacherDelete";
                break;
            }
            case "group" : {
                page = "groupDelete";
                break;
            }
        }
        $.post(page, {"criteria" : "ID", "criteriaValue" : id}, function(data) {
            data = JSON.parse(data);
            var errors = data.errors;

            if(jQuery.isEmptyObject(errors)) {
                $("#" + table + id).hide();
                $("#status").html("");
                console.log(3);
            } else {
                $("#status").html("Ошибка удаления.")
            }
        });
    })
}
function addUpdateEventHandler(selector, table) {
    $(selector).on("click", ".update", function() {
        $("#" + table + "AddForm").hide();
        $("#" + table + "SearchForm").hide();
        var a = $(this);
        var href = a.attr("href");var id = parseInt(href.match(/\d+/));

        $("#" + table + "UpdateForm").show();
        var name = $("#" + table + id + " .name").html();
        var birth = $("#" + table + id + " .birth").html();;
        var gender = $("#" + table + id + " .gender").html();;
        var group = $("#" + table + id + " .group").html();
        $("#" + table + "UpdateForm .id").val(id);
        $("#" + table + "UpdateForm .name").val(name);
        $("#" + table + "UpdateForm .birth").val(birth);
        $("#" + table + "UpdateForm .gender").val(gender);
        $("#" + table + "UpdateForm .group").val(group);
    })
}
function addGroupInfoEventHandler(selector) {
    $("#groupOutput .outputTable " + selector).on("click", ".getInfo", function() {
        var a = $(this);
        var href = a.attr("href");
        var id = parseInt(href.match(/\d+/));
        $("#groupNumber").html($("#group" + id).find(".group").html())
        $("#status").html("Загружаю...");

        $.get("groupGetInfo", {"id" : id}, function(data) {
            data = JSON.parse(data);

            var errors = data.errors;
            var teachers = data.teachers;
            var students = data.students;

            $("#groupInfo #info").html(groupInfoTemplate);
            $("#groupStudentsTable").show();

            if(jQuery.isEmptyObject(errors)) {
                $("#groupInfo").show();
                if(jQuery.isEmptyObject(students)) {
                    $("#groupStudentsTable").html(" нет");
                } else {
                    $("#groupStudentsTable").html("");
                    students.forEach(function (student, position) {
                        if(position != students.length-1) {
                            $("#groupStudentsTable").append(student + ", ");
                        } else {
                            $("#groupStudentsTable").append(student + ".");
                        }
                    })
                }
                if(jQuery.isEmptyObject(teachers)) {
                    $("#groupTeachersTable").css("display", "inline");
                    $("#groupTeachersTable").html(" нет");
                } else {
                    $("#groupTeachersTable").html("");
                    $("#groupTeachersTable").css("display", "block");
                    $("#groupTeachersTable").html("\t<table><tr>\n" +
                        "\t\t<td style=\"display: none;\">" +
                        "ID группы" +
                        "</td>\n" +
                        "\t\t<td>ФИО</td>\n" +
                        "\t\t<td>Операции</td>\n" +
                        "\t</tr>");

                    teachers.forEach(function (teacher) {
                        $("#groupTeachersTable table").append("\t<tr id=\"groupTeacher"
                            + teacher["id"] + "\">\n" +
                            "\t\t<td class=\"groupId\" style=\"display: none;\">"
                            + id + "</td>\n" +
                            "\t\t<td>" + teacher["name"] + "</td>\n" +
                            "\t\t<td><a class=\"removeTeacherFromGroup\" " +
                            "href=\"#removeTeacher"
                            + teacher["id"]
                            + "FromGroup\">Убрать из группы</a></td>\n" +
                            "\t</tr>");
                    });
                    $("#groupTeachersTable").append("</table>")
                }

                addTeacherRemovingFromGroupHandler("#groupInfo table tr");
                $("#status").html("");
            } else {
                $("#status").html("Внутренняя ошибка.");
            }
        });

    })
}
function addGetTeachersHandler(selector) {
    $("#groupOutput .outputTable " + selector).on("click", ".putTeacherInGroup",
        function() {
        var a = $(this);
        var href = a.attr("href");
        var id = parseInt(href.match(/\d+/));
        $("#groupNumber").html($("#group" + id).find(".group").html())
        $("#status").html("Загружаю...");

        $.get("getTeachers", {"id" : id}, function(data) {
            data = JSON.parse(data);

            var errors = data.errors;
            var teachers = data.teachers;

            $("#info").html("<div id=\"groupTeachersTable\"></div>");
            $("#groupInfo").show();
            if(jQuery.isEmptyObject(errors)) {
                $("#groupTeachersTable").html("");
                $("#groupTeachersTable").css("display", "block");
                $("#groupTeachersTable").html("\t<table><tr>\n" +
                    "\t\t<td style=\"display: none;\">" +
                    "ID группы" +
                    "</td>\n" +
                    "\t\t<td>ФИО</td>\n" +
                    "\t\t<td>Операции</td>\n" +
                    "\t</tr>");

                teachers.forEach(function (teacher) {
                    $("#groupTeachersTable table").append("\t<tr id=\"freeTeacher"
                        + teacher["id"] + "\">\n" +
                        "\t\t<td class=\"groupId\" style=\"display: none;\">"
                        + id + "</td>\n" +
                        "\t\t<td>" + teacher["name"] + "</td>\n" +
                        "\t\t<td><a class=\"putInGroup\" " +
                        "href=\"#putTeacher"
                        + teacher["id"]
                        + "\">Назначить</a></td>\n" +
                        "\t</tr>");
                });
                $("#groupTeachersTable").append("</table>")

                addTeacherPuttingInGroupHandler("#groupInfo table tr");
            } else {
                $("#groupTeachersTable").html("Некого назначить: все " +
                    "преподаватели заняты.");
            }
            $("#status").html("");
        });

    })
}
function addTeacherRemovingFromGroupHandler(selector) {
    $(selector).on("click", ".removeTeacherFromGroup", function() {
        var a = $(this);
        var href = a.attr("href");
        var teacherId = parseInt(href.match(/\d+/));

        var groupId = $("#groupTeacher" + teacherId).find(".groupId").html();

        $.post("teacherDeleteGroup", {
            "teacherId" : teacherId,
                "groupId" : groupId},
            function(data) {

            data = JSON.parse(data);
            var errors = data.erorrs;

            if(jQuery.isEmptyObject(errors)) {
                $("#groupTeacher"+ teacherId).hide();
                $("#status").html("");
            } else {
                lightOn("#groupTeacher" + teacherId, failColor);
            }
        });

    })
}
function addGroupRemovingFromTeacherHandler(selector) {
    $(selector).on("click", ".removeTeacherFromGroup", function() {
        var a = $(this);
        var href = a.attr("href");
        var groupId = parseInt(href.match(/\d+/));

        var teacherId = $("#groupTeacher" + groupId).find(".teacherId").html();
        console.log(groupId + " "+ teacherId);

        $.post("teacherDeleteGroup", {
            "teacherId" : teacherId,
            "groupId" : groupId}, function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                $("#groupTeacher"+ groupId).hide().remove();
                $("#status").html("");
            } else {
                lightOn("#groupTeacher" + teacherId, failColor);
            }
        });

    })
}
function addTeacherPuttingInGroupHandler(selector) {
    $(selector).on("click", ".putInGroup", function() {
        var a = $(this);
        var href = a.attr("href");
        var teacherId = parseInt(href.match(/\d+/));

        var groupId = $("#freeTeacher" + teacherId).find(".groupId").html();
        console.log(teacherId + " " + groupId);

        $.post("teacherPutInGroup", {"teacherId" : teacherId, "groupId" : groupId}, function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                $("#freeTeacher"+ teacherId).hide();
                $("#status").html("");
            } else {
                lightOn("#freeTeacher" + teacherId, failColor);
            }
        });

    })
}
function addTeacherInfoEventHandler(selector) {
    $("#teacherOutput .outputTable " + selector).on("click", ".getTeacherInfo", function() {
        var a = $(this);
        var href = a.attr("href");
        var id = parseInt(href.match(/\d+/));
        $("#teacherName").html($("#teacher" + id).find(".name").html())
        $("#teacherId").html(id)
        $("#status").html("Загружаю...");

        $.get("teacherGetInfo", {"id" : id}, function(data) {
            $("#teacherInfo").show();

            data = JSON.parse(data);

            var error = data.errors;
            if(jQuery.isEmptyObject(error)) {
                var groups = data.groups;
                if(!jQuery.isEmptyObject(groups)) {
                    $("#groups").css("display", "block");
                    $("#groups").html("");

                    $("#groups").append("\t<table><tr>\n" +
                        "\t\t<td style=\"display: none;\">" +
                        "ID преподавателя" +
                        "</td>\n" +
                        "\t\t<td>Номер</td>\n" +
                        "\t\t<td>Операции</td>\n" +
                        "\t</tr>");


                    Object.keys(groups).forEach(function (groupId) {
                        $("#groups table").append("\t<tr id=\"groupTeacher"
                            + groupId + "\">\n" +
                            "\t\t<td class=\"teacherId\" style=\"display: none;\">"
                            + id + "</td>\n" +
                            "\t\t<td class=\"number\">"
                            + groups[groupId] + "</td>\n" +
                            "\t\t<td><a class=\"removeTeacherFromGroup\"" +
                            " href=\"#removeGroupFromTeacher"
                            + groupId + "FromGroup\">" +
                            "Убрать группу" +
                            "</a></td>\n" +
                            "\t</tr>");

                    });

                    addGroupRemovingFromTeacherHandler("#teacherInfo table tr");

                    $("#groups").append("</table>");
                } else {
                    $("#groups").css("display", "inline");
                    $("#groups").html("нет");
                }
                $("#status").html("");
            } else {
                $("#status").html("Внутренняя ошибка.");
            }
        });

    })
}

function lightOn(selector, color) {
    $(selector).css("background-color", color);
    setTimeout(function() {
        $(selector).css("background-color", "white");
        $(selector).removeAttr("style");
    }, 1000)

}

function addStudentRow(student) {
        var birth = student["birth"];
        var birthViewable = new Date(birth);
        birthViewable = birthViewable.getDate() + "."
            + (1+birthViewable.getMonth()) + "."
            + birthViewable.getFullYear();

        $("#studentOutput .outputTable tbody").append(
            "\t<tr id='student" + student["id"] + "'>\n" +
            "\t\t<td class='id'>" + student["id"] + "</td>\n" +
            "\t\t<td class='name'>" + student["name"] + "</td>\n" +
            "\t\t<td class='birth'>" + birth + "</td>\n" +
            "\t\t<td class='birthViewable'>" + birthViewable + "</td>\n" +
            "\t\t<td class='gender'>" + student["gender"] + "</td>\n" +
            "\t\t<td class='group'>" + student["group"]["number"] + "</td>\n" +
            "\t\t<td class='operations'>" +
            "<a class=\"delete\" href=\"#deleteStudent" + student["id"] + "\">Удалить</a><br>" +
            "<a class=\"update\" href=\"#updateStudent" + student["id"] + "\">Изменить</a>"
            + "</td>\n" +
            "\t</tr>"
        );
}
function addGroupRow(group) {
        $("#groupOutput .outputTable tbody").append(
            "\t<tr id='group" + group["id"] + "'>\n" +
            "\t\t<td class='id'>" + group["id"] + "</td>\n" +
            "\t\t<td class='group'>" + group["number"] + "</td>\n" +
            "\t\t<td class='operations'>" +
            "<a class=\"delete\" href=\"#deleteGroup" + group["id"] + "\">" +
            "Удалить" +
            "</a><br>" +
            "<a class=\"update\" href=\"#updateGroup" + group["id"] + "\">" +
            "Изменить" +
            "</a><br>" +
            "<a class=\"getInfo\" href=\"#getInfoGroup" + group["id"] + "\">" +
            "Информация" +
            "</a><br>" +
            "<a class=\"putTeacherInGroup\" href=\"#putTeacherInGroup" + group["id"] + "\">" +
            "Назначить преподавателя" +
            "</a><br>"
            + "</td>\n" +
            "\t</tr>"
        );
        console.log(group["id"] + " " + group["number"]);
}
function addTeacherRow(teacher) {
    var birth = teacher["birth"];
    var birthViewable = new Date(birth);
    birthViewable = birthViewable.getDate() + "."
        + (1+birthViewable.getMonth()) + "."
        + birthViewable.getFullYear();

    $("#teacherOutput .outputTable tbody").append("<tr id=\"teacher" + teacher["id"] + "\">" +
        "<td class=\"id\">" + teacher["id"] + "</td>" +
        "<td class=\"name\">" + teacher["name"] + "</td>" +
        "<td class=\"birth\">" + birth + "</td>" +
        "<td class=\"birthViewable\">" + birthViewable + "</td>" +
        "<td class=\"gender\">" + teacher["gender"] + "</td>" +
        "<td class=\"opeations\">" +
        "<a class=\"delete\" href=\"#deleteTeacher" + teacher["id"] + "\">" +
        "Удалить" +
        "</a>" +
        "<br>" +
        "<a class=\"update\" href=\"#updateTeacher" + teacher["id"] + "\">" +
        "Изменить" +
        "</a>" +
        "<br>" +
        "<a class=\"getTeacherInfo\" href=\"#getInfoTeacher" + teacher["id"] + "\">" +
        "Информация" +
        "</a>" +
        "</td>" +
        "</tr>");
}
/** /Функции **/