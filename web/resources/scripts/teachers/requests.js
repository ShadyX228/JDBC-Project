$(document).ready(function () {
    var teacherInfoTemplate = "<div id=\"info\">" +
        "<fieldset>" +
        "<legend>Группы</legend>" +
        "<div id=\"groups\">" +
        "<table class=\"display\">" +
        "<thead>" +
        "<tr>" +
        "<th>Номер</th>" +
        "<th>Операции</th>" +
        "</tr>" +
        "</thead>" +
        "<tbody>" +
        "</tbody>" +
        "</table>" +
        "</div>" +
        "</fieldset>" +
        "</div>";

    /** Преподаватель **/
    var table = $("#teacherOutput .outputTable").DataTable({
        language : {
            url : "/resources/scripts/datatables/locale/Russian.json"
        },
        serverSide : true,
        ajax: {
            url: "teacher/selectAllTeachers",
            type: "GET",
            dataSrc : "teachers"
        },
        stateSave: true,
        columns : [
            {
                "className" : "name",
                "data" : "name"
            },
            {
                "className" : "birthday",
                "data" : "birthday"
            },
            {
                "className" : "gender",
                "data" : "gender"
            },
            {
                "className" : "operations",
                "data": "id",
                "render": function (data) {
                    return "<a class=\"delete\" href=\"#deleteTeacher" + data + "\">Удалить</a> | " +
                        "<a class=\"update\" href=\"#updateTeacher" + data + "\">Изменить</a> | " +
                        "<a class=\"getInfo\" href=\"#getInfoTeacher" + data + "\">Информация</a>";
                }
            }
        ]
    });

    $("#teacherAdd").click(function () {
        $("#teacherAddForm").show();
        $("#teacherUpdateForm").hide();
    });

    $("#teacherAddForm").submit(function (event) {
        event.preventDefault();
        $.post("teacher/addTeacher", $(this).serialize(),
            function (data) {
            var errors = data.errors;
            $("#status").html(errors);

            table.page("last").draw("page");
        });
    });

    $("#teacherUpdateForm").submit(function (event) {
        event.preventDefault();

        $.post("teacher/updateTeacher", $(this).serialize(), function (data) {
            var errors = data.errors;
            $("#status").html(errors);
            table.draw(false);
        });
    });

    table.on("click", ".delete", function() {
        $("#teacherUpdateForm").hide();

        var a = $(this);
        var href = a.attr("href");
        var id = parseInt(href.match(/\d+/));

        $.post("teacher/deleteTeacher", {"id" : id}, function(data) {
            var errors = data.errors;
            $("#status").html(errors);

            if(table.page.info().recordsTotal == 1) {
                table.page("previous").draw("page")
            }
            table.draw(false);
        });
    });

    table.on("click", ".update", function() {
        $("#teacherAddForm").hide();

        var a = $(this);
        var href = a.attr("href");
        var id = parseInt(href.match(/\d+/));
        var name = a.parent().parent().find(".name").html();
        var birthday = a.parent().parent().find(".birthday").html();
        var gender = a.parent().parent().find(".gender").html();

        $("#teacherUpdateForm").show();

        $("#teacherUpdateForm .id").val(id);
        $("#teacherUpdateForm .name").val(name);
        $("#teacherUpdateForm .birth").val(birthday
            .replace(/(\d{2}).(\d{2}).(\d{4})/, "$3-$2-$1"));
        $("#teacherUpdateForm .gender").val(gender);
    });

    table.on("click", ".getInfo", function() {
        $("#info").html(teacherInfoTemplate);
        var a = $(this);
        var href = a.attr("href");
        var teacherId = parseInt(href.match(/\d+/));
        var teacherName = a.parent().parent().find(".name").html();

        console.log(teacherId + " " + teacherName);
        $("#teacherId").html(teacherId);
        $("#teacherName").html(teacherName);
        $("#teacherInfo").show();
        var tableGroups = $("#groups .display").DataTable({
            language : {
                url : "/resources/scripts/datatables/locale/Russian.json"
            },
            serverSide : true,
            ajax: {
                url: "teacher/getTeacherInfo",
                type: "GET",
                data : function(param) {
                    param.id = teacherId;
                },
                dataSrc : "groups"
            },
            paging: false,
            searching: false,
            info: false,
            columns : [
                {
                    "data" : "number"
                },
                {
                    "data" : "id",
                    "render" : function (data) {
                        return "<a class=\"removeTeacherFromGroup\" "
                            + "href=\"#removeTeacher"
                            + data + "FromGroup\">Убрать группу</a>";
                    }
                }
            ]
        });

        tableGroups.on("click", ".removeTeacherFromGroup", function() {
            var a = $(this);
            var href = a.attr("href");
            var groupId = parseInt(href.match(/\d+/));

            $.post("teacher/removeTeacherFromGroup", {"groupId" : groupId, "teacherId" : teacherId}, function(data) {
                var errors = data.errors;
                $("#status").html(errors);
                tableGroups.draw();
            });
        });

        $("#putTeacherInGroup").unbind("submit").submit(function (event) {
            event.preventDefault();
            var teacherId = $("#teacherInfo #teacherId").html();
            var groupNumber = $("#putTeacherInGroup .group").val();
            console.log(teacherId + " " + groupNumber);

            $.post("teacher/putTeacherInGroup", {
                "teacherId" : teacherId,
                "groupNumber" : groupNumber}, function(data) {
                var errors = data.errors;
                $("#status").html(errors);
                tableGroups.draw();
            });
        });
    });

    $("#putTeacherInGroup").submit(function (event) {
        event.preventDefault();
        var teacherId = $("#teacherInfo #teacherId").html();
        var groupNumber = $("#putTeacherInGroup .group").val();
        console.log(teacherId + " " + groupNumber);

        $.post("teacher/putTeacherInGroup", {
            "teacherId" : teacherId,
            "groupNumber" : groupNumber}, function(data) {
            var errors = data.errors;
            $("#status").html(errors);
        });
    });

    /** /Преподаватель **/
});