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

            if(jQuery.isEmptyObject(errors)) {
                $("#groupInfo").show();
                if(jQuery.isEmptyObject(students)) {
                    $("#groupStudentsTable").html(" нет");
                    console.log(1);
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
            $("#groupInfo").show();
            $("#info").html(data);
            addTeacherPuttingInGroupHandler("#groupInfo table tr");
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
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
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
    $("#teacherOutput .outputTable " + selector).on("click", ".getInfo", function() {
        var a = $(this);
        var href = a.attr("href");
        var id = parseInt(href.match(/\d+/));
        $("#teacherName").html($("#teacher" + id).find(".name").html())
        $("#teacherId").html(id)
        $("#status").html("Загружаю...");

        $.get("teacherGetInfo", {"id" : id}, function(data) {
            $("#teacherInfo").show();
            $("#status").html(data);
            var error = $("#status .error").html();
            if(isNaN(error)) {
                $("#groups").html(data);
                addGroupRemovingFromTeacherHandler("#teacherInfo table tr");
                //addTeacherAddingInGroupHandler("#teacherInfo table tr");
            } else {
                $("#groups").html(
                    "нет. <span style=\"display: none\" class=\"error\">-1</span>"
                );
            }
            $("#status").html("");
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
        $("#studentOutput .outputTable").append(
            "\t<tr id='student" + student["id"] + "'>\n" +
            "\t\t<td class='id'>" + student["id"] + "</td>\n" +
            "\t\t<td class='name'>" + student["name"] + "</td>\n" +
            "\t\t<td class='birth'>" + student["birth"] + "</td>\n" +
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
        $("#groupOutput .outputTable").append(
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
            "Назначить преподаватлея" +
            "</a><br>"
            + "</td>\n" +
            "\t</tr>"
        );
}

/*function createTableOfTeachersInGroup() {
    $("#info").ht
}*/
/** /Функции
 *
 * response.getWriter().write("\t<table><tr>\n" +
 "\t\t<td style=\"display: none;\">" +
 "ID группы" +
 "</td>\n" +
 "\t\t<td>ФИО</td>\n" +
 "\t\t<td>Операции</td>\n" +
 "\t</tr>");
 for (Teacher teacher : group.getTeachers()) {
                        response.getWriter().write("\t<tr id=\"groupTeacher"
                                + teacher.getId() + "\">\n" +
                                "\t\t<td class=\"groupId\" style=\"display: none;\">"
                                + group.getId() + "</td>\n" +
                                "\t\t<td>" + teacher.getName() + "</td>\n" +
                                "\t\t<td><a class=\"removeTeacherFromGroup\" " +
                                "href=\"#removeTeacher"
                                + teacher.getId()
                                + "FromGroup\">Убрать из группы</a></td>\n" +
                                "\t</tr>");
                    }
 * **/