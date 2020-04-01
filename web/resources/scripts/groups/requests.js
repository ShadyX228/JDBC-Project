$(document).ready(function () {
    var groupInfoTemplate = "<div id=\"info\">" +
        "<fieldset><legend>Преподаватели группы</legend>" +
        "<div id=\"groupTeachersTable\">" +
    "<table class=\"display\">" +
    "<thead>" +
        "<tr>" +
        "<th>ФИО</th>" +
        "<th>Операции</th>" +
        "</tr>" +
        "</thead>" +
        "<tbody>" +
        "</tbody>" +
        "</table>" +
        "</div></fieldset>" +
        "<fieldset>" +
        "<legend>Свободные преподаватели</legend>" +
        "<div id=\"freeTeachersTable\">" +
    "<table class=\"display\">" +
    "<thead>" +
        "<tr>" +
        "<th>ФИО</th>" +
        "<th>Операции</th>" +
        "</tr>" +
        "</thead>" +
        "<tbody>" +
        "</tbody>" +
        "</table>" +
        "</div></fieldset><fieldset style=\"padding: 5px;\">" +
        "<legend>Студенты</legend>" +
        "<span id=\"groupStudentsTable\"></span></fieldset>";
    /** Группа **/


    var table = $("#groupOutput .outputTable").DataTable({
        language : {
            url : "/resources/scripts/datatables/locale/Russian.json"
        },
        serverSide : true,
        ajax: {
            url: "group/selectAllGroups",
            type: "GET",
            dataSrc : "groups"
        },
        stateSave: true,
        columns : [
            {
                "className" :"number",
                "data" : "number"
            },
            {
                "className" : "operations",
                "data": "id",
                "render": function (data) {
                    return "<a class=\"delete\" href=\"#deleteGroup" + data + "\">Удалить</a> | " +
                        "<a class=\"update\" href=\"#updateGroup" + data + "\">Изменить</a> | " +
                        "<a class=\"getInfo\" href=\"#getInfoGroup" + data + "\">Информация</a>";
                }
            }
        ]
    });

    $("#groupAdd").click(function () {
        $("#groupAddForm").show();
        $("#groupUpdateForm").hide();
    });

    $("#groupAddForm").submit(function (event) {
        event.preventDefault();
        var number = $("#groupAddForm .group").val();
        $.post("group/addGroup", {
            "number": number
        }, function (data) {
            var errors = data.errors;
            $("#status").html(errors);

            table.page("last").draw("page");
        });
    });

    $("#groupUpdateForm").submit(function (event) {
        event.preventDefault();
        console.log( $(this).serialize());
        $.post("group/updateGroup", $(this).serialize(), function (data) {
            var errors = data.errors;
            $("#status").html(errors);
            table.draw(false);
        });
    });

    table.on("click", ".delete", function() {
        $("#groupUpdateForm").hide();

        var a = $(this);
        var href = a.attr("href");
        var id = parseInt(href.match(/\d+/));

        console.log(id);
        $.post("group/deleteGroup", {"id" : id}, function(data) {
            var errors = data.errors;
            $("#status").html(errors);

            if(table.page.info().recordsTotal == 1) {
                table.page("previous").draw("page")
            }
            table.draw(false);
        });
    });

    table.on("click", ".update", function() {
        $("#groupAddForm").hide();

        var a = $(this);
        var href = a.attr("href");
        var id = parseInt(href.match(/\d+/));
        var group = a.parent().parent().find(".number").html();


        $("#groupUpdateForm").show();

        $("#groupUpdateForm .id").val(id);
        $("#groupUpdateForm .group").val(group);
    });

    table.on("click", ".getInfo", function() {
        $("#info").html(groupInfoTemplate);
        var a = $(this);
        var href = a.attr("href");
        var groupId = parseInt(href.match(/\d+/));
        var number = a.parent().parent().find(".number").html();
        console.log(groupId + " " + number);
        $("#groupNumber").html(number);
        $("#groupInfo").show();

        var tableBoundedTeachers = $("#groupTeachersTable .display").DataTable({
            language : {
                url : "/resources/scripts/datatables/locale/Russian.json"
            },
            serverSide : true,
            ajax: {
                url: "group/getGroupInfoTeachers",
                type: "GET",
                data : function(param) {
                    param.id = groupId;
                },
                dataSrc : "teachers"
            },
            paging: false,
            searching: false,
            info: false,
            columns : [
                {
                    "data" : "name"
                },
                {
                    "data" : "id",
                    "render" : function (data) {
                        return "<a class=\"removeTeacherFromGroup\" "
                            + "href=\"#removeTeacher"
                            + data + "FromGroup\">Убрать из группы</a>";
                    }
                }
            ]
        });

        var tableFreeTeachers =  $("#freeTeachersTable .display").DataTable({
            language : {
                url : "/resources/scripts/datatables/locale/Russian.json"
            },
            serverSide : true,
            ajax: {
                url: "group/getFreeTeachers",
                type: "GET",
                data : function(param) {
                    param.id = groupId;
                },
                dataSrc : "teachers"
            },
            paging: false,
            searching: false,
            info: false,
            columns : [
                {
                    "data" : "name"
                },
                {
                    "data" : "id",
                    "render" : function (data) {
                        return "<a class=\"putTeacherInGroup\" "
                            + "href=\"#putTeacher" + "InGroup" + data +"\">Назначить</a>";
                    }
                }
            ]
        });

        $.get("group/getGroupInfoStudents", {"id" : groupId}, function(data) {
            var errors = data.errors;
            var students = data.students;
            $("#status").html(errors);
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
                });
            }

        });

        tableBoundedTeachers.on("click", ".removeTeacherFromGroup", function() {
            $("#groupUpdateForm").hide();

            var a = $(this);
            var href = a.attr("href");
            var teacherId = parseInt(href.match(/\d+/));

            $.post("group/deleteTeacherFromGroup", {"groupId" : groupId, "teacherId" : teacherId}, function(data) {
                var errors = data.errors;
                $("#status").html(errors);
                tableBoundedTeachers.draw();
                tableFreeTeachers.draw();
            });
        });

        tableFreeTeachers.on("click", ".putTeacherInGroup", function() {
            $("#groupUpdateForm").hide();

            var a = $(this);
            var href = a.attr("href");
            var teacherId = parseInt(href.match(/\d+/));

            $.post("group/putTeacherInGroup", {"groupId" : groupId, "teacherId" : teacherId}, function(data) {
                var errors = data.errors;
                $("#status").html(errors);
                tableFreeTeachers.draw();
                tableBoundedTeachers.draw();
            });
        });
    });


    /** /Группа **/
});