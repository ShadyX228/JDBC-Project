$(document).ready(function () {
    var successColor = "#E7E7E7";
    var failColor = "red";

    /** Преподаватель **/
    $("#status").show().html("Загрузка...");
    $("#studentsTable").hide();
    $("#groupsTable").hide();
    $("#teachersTable").show();
    $.post("teacherSelectAll", function(data) {
        $("#teacherOutput .outputTable").show();
        $("#teacherOutput .outputTable").html("");
        data = JSON.parse(data);

        var errors = data.errors;
        if(jQuery.isEmptyObject(errors)) {
            $("#teacherOutput .outputTable").append(
                "\t<thead><tr>\n" +
                "\t\t<th>ID</th>\n" +
                "\t\t<th>ФИО</th>\n" +
                "\t\t<th>День рождения</th>\n" +
                "\t\t<th>Пол</th>\n" +
                "\t\t<th>Операции</th>\n" +
                "\t</tr></thead><tbody></tbody>"
            );
            var teachers = data.teachers;


            teachers.forEach(function (teacher) {
                addTeacherRow(teacher);
            });

            $("#status").html("");

            addDeleteEventHandler("#teacherOutput .outputTable tr",
                "teacher");
            addUpdateEventHandler("#teacherOutput .outputTable tr",
                "teacher");
            addTeacherInfoEventHandler("tr");
            addGetTeachersHandler("tr");
        } else {
            $("#status").html("Нет записей в таблице.");
        }
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

            data = JSON.parse(data);
            var errors = data.errors;
            var id = data["lastId"];
            if(jQuery.isEmptyObject(errors)) {
                $("#status").html("");
                var teacher = {
                    "id": id,
                    "name": name,
                    "birth": birth,
                    "gender": gender,
                };
                addTeacherRow(teacher);
                lightOn("#teacher" + id,successColor);
                addDeleteEventHandler("#teacher"+id, "teacher");
                addUpdateEventHandler("#teacher"+id, "teacher");
                addTeacherInfoEventHandler("#teacher"+id);
                console.log(3);
            } else {
                $("#status").html("Ошибка. Перепроверьте введеннные данные.");
            }
        });
    });
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

                data = JSON.parse(data);
                var errors = data.errors;
                if(jQuery.isEmptyObject(errors)) {
                    lightOn("#teacher" + id,successColor);
                    $("#teacher" + id + " .name").html(name);

                    var birthViewable = new Date(birth);
                    birthViewable = birthViewable.getDate() + "."
                        + (1+birthViewable.getMonth()) + "."
                        + birthViewable.getFullYear();

                    $("#teacher" + id + " .birth").html(birth);
                    $("#teacher" + id + " .birthViewable").html(birthViewable);
                    $("#teacher" + id + " .gender").html(gender);
                    $("#status").html("");
                } else {
                    $("#status").html("Ошибка. Перепроверьте введенные данные.");
                    lightOn("#teacher" + id,failColor);
                }
            });
    });
    $("#teacherSearchForm").submit(function (event) {
        event.preventDefault();
        $.get("teacherSelect", $(this).serialize(), function(data) {
            $("#teacherOutput .outputTable").html("");

            data = JSON.parse(data);

            var errors = data.errors;

            if(jQuery.isEmptyObject(errors)) {
                $("#teacherOutput .outputTable").append(
                    "\t<thead><tr>\n" +
                    "\t\t<th>ID</th>\n" +
                    "\t\t<th>ФИО</th>\n" +
                    "\t\t<th>День рождения</th>\n" +
                    "\t\t<th>Пол</th>\n" +
                    "\t\t<th>Операции</th>\n" +
                    "\t</tr></thead><tbody></tbody>"
                );
                var teachers = data.teachers;


                teachers.forEach(function (teacher) {
                    addTeacherRow(teacher);
                });

                $("#status").html("");

                addDeleteEventHandler("tr", "teacher");
                addUpdateEventHandler("tr", "teacher");
                addTeacherInfoEventHandler("tr");
            } else {
                $("#status").html("Нет записей в таблице.");
            }
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
            data = JSON.parse(data);

            var errors = data.errors;
            if(jQuery.isEmptyObject(errors)) {
                var lastId = data["groupId"];
                var emptyCheck = $( "#teacherInfo #groups" )
                    .has( "table" ).length ? true : false;
                console.log(emptyCheck);
                if(!emptyCheck) {
                    // создать таблицу
                    var table = "\t<table><tr>\n" +
                        "\t\t<td style=\"display: none;\">ID преподавателя</td>\n" +
                        "\t\t<td>Номер</td>\n" +
                        "\t\t<td>Операции</td>\n" +
                        "\t</tr>";
                    table += "\t<tr id=\"groupTeacher" + lastId + "\">\n" +
                        "\t\t<td class=\"teacherId\" style=\"display: none;\">"
                        + teacherId +
                        "</td>\n" +
                        "\t\t<td class=\"number\">" + groupNumber + "</td>\n" +
                        "\t\t<td>" +
                        "<a class=\"removeTeacherFromGroup\" href=\"#removeGroupFromTeacher" + lastId + "FromGroup\">" +
                        "Убрать группу" +
                        "</a>" +
                        "</td>\n" +
                        "\t</tr>";
                    table += "</table>";
                    $("#groups").html(table);
                    addGroupRemovingFromTeacherHandler(
                        "#groupTeacher" + lastId
                    );
                } else {
                    $("#teacherInfo table").append(
                        "\t<tr id=\"groupTeacher" + lastId + "\">\n" +
                        "\t\t<td class=\"teacherId\" style=\"display: none;\">"
                        + teacherId +
                        "</td>\n" +
                        "\t\t<td class=\"number\">"
                        + groupNumber +
                        "</td>\n" +
                        "\t\t<td>" +
                        "<a class=\"removeTeacherFromGroup\" " +
                        "href=\"#removeGroupFromTeacher" + lastId + "FromGroup\">" +
                        "Убрать группу" +
                        "</a>" +
                        "</td>\n" +
                        "\t</tr>");
                    addGroupRemovingFromTeacherHandler(
                        "#groupTeacher" + lastId
                    );
                }
                $("#status").html("");
            }
        });
    })
    /** /Преподаватель **/
})