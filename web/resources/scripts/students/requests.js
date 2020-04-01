$(document).ready(function () {

    /** Студент **/
    var table = $("#studentOutput .outputTable").DataTable({
        language : {
            url : "/resources/scripts/datatables/locale/Russian.json"
        },
        serverSide : true,
        ajax: {
            url: "student/selectAllStudents",
            type: "GET",
            dataSrc : "students"
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
                "className" : "group",
                "data" : "group"
            },
            {
                "className" : "operations",
                "data": "id",
                "render": function (data) {
                    return "<a class=\"delete\" href=\"#deleteStudent" + data + "\">Удалить</a> | " +
                        "<a class=\"update\" href=\"#updateStudent" + data + "\">Изменить</a>";
                }
            }
        ]
    });

    $("#studentAdd").click(function () {
        $("#studentAddForm").show();
        $("#studentUpdateForm").hide();
    });

    $("#studentAddForm").submit(function (event) {
        event.preventDefault();
       // $("#studentAddForm .submit").prop("disabled", true);
        console.log($(this).serialize());
        $.post("student/addStudent", $(this).serialize(), function (data) {
            var errors = data.errors;
            $("#status").html(errors);

            table.page("last").draw("page");
        });
    });

    table.on("click", ".delete", function() {
        $("#teacherUpdateForm").hide();

        var a = $(this);
        var href = a.attr("href");
        var id = parseInt(href.match(/\d+/));

        $.post("student/deleteStudent", {"id" : id}, function(data) {
            var errors = data.errors;
            $("#status").html(errors);

            if(table.page.info().recordsTotal == 1) {
                table.page("previous").draw("page")
            }
            table.draw(false);
        });
    });

    table.on("click", ".update", function() {
        $("#studentAddForm").hide();

        var a = $(this);
        var href = a.attr("href");
        var id = parseInt(href.match(/\d+/));
        var name = a.parent().parent().find(".name").html();
        var birthday = a.parent().parent().find(".birthday").html();
        var gender = a.parent().parent().find(".gender").html();
        var group = a.parent().parent().find(".group").html();

        $("#studentUpdateForm").show();

        $("#studentUpdateForm .id").val(id);
        $("#studentUpdateForm .name").val(name);
        $("#studentUpdateForm .birth").val(birthday
            .replace(/(\d{2}).(\d{2}).(\d{4})/, "$3-$2-$1"));
        $("#studentUpdateForm .gender").val(gender);
        $("#studentUpdateForm .group").val(group);
    });

    $("#studentUpdateForm").submit(function (event) {
        event.preventDefault();

        $.post("student/updateStudent", $(this).serialize(), function (data) {
            var errors = data.errors;
            $("#status").html(errors);
            table.draw(false);
        });
    });

    $("#studentAddForm .groupNum").keyup(function () {
        var group = $(this).val();
        if(!jQuery.isEmptyObject(group)) {
            $.get("group/selectAllGroups", {"search[value]" : group, "start" : 1, "length" : 1}, function (data) {
                var errors = data.errors;
                if (jQuery.isEmptyObject(errors)) {
                    $("#studentAddForm .submit").prop("disabled", false);
                    $("#studentAddForm .group").val(data.groups[0].id);
                } else {
                    $("#studentAddForm .submit").prop("disabled", true);
                }
                $("#status").html(errors);
            });
        } else {
            $("#studentAddForm .submit").prop("disabled", false);
            $("#studentAddForm .group").val("");
            $("#status").html("");
        }
    });
    /** /Студент **/
});