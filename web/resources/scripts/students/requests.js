$(document).ready(function () {
    var successColor = "#E7E7E7";
    var failColor = "red";
    var table = $("#studentOutput .outputTable");

    /** Студент **/
    $("#status").show().html("Загрузка...");
    $("#studentsTable").show();
    $("#groupsTable").hide();
    $("#teachersTable").hide();
    $.get("student/selectAllStudents", function(data) {
        table.show();
        table.html("");

        data = JSON.parse(data);

        var errors = data.errors;
        if(jQuery.isEmptyObject(errors)) {
            table.append(
                "\t<thead><tr>\n" +
                "\t\t<th>ID</th>\n" +
                "\t\t<th>ФИО</th>\n" +
                "\t\t<th>День рождения</th>\n" +
                "\t\t<th>Пол</th>\n" +
                "\t\t<th>Группа</th>\n" +
                "\t\t<th>Операции</th>\n" +
                "\t</tr></thead><tbody></tbody>"
            );
            var students = data.students;


            students.forEach(function (student) {
                addStudentRow(student);
            });

            $("#status").html("");

            addDeleteEventHandler("#studentOutput .outputTable tr",
                "student");
            addUpdateEventHandler("#studentOutput .outputTable tr",
                "student");
        } else {
            $("#status").html("Нет записей в таблице.");
        }
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
        $.post("student/addStudent", $(this).serialize(), function(data) {
            data = JSON.parse(data);
            var errors = data.errors;

            if(jQuery.isEmptyObject(errors)) {
                $("#status").html("");
                var lastId = data["lastId"];
                var student = {
                    "id": lastId,
                    "name": name,
                    "birth": birth,
                    "gender": gender,
                    "group": {"number" : group}
                };
                addStudentRow(student);
                lightOn("#student" + lastId,successColor);
                addDeleteEventHandler("#student"+lastId,
                    "student");
                addUpdateEventHandler("#student"+lastId,
                    "student");
            } else {
                $("#status").html(errors);
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
        $.post("student/updateStudent", {"id" : id,
            "name" : name,
            "birth" : birth,
            "gender" : gender,
            "group" : group}, function(data) {

            data = JSON.parse(data);
            var errors = data.errors;
            if(jQuery.isEmptyObject(errors)) {
                lightOn("#student" + id,successColor);
                $("#student" + id + " .name").html(name);

                var birthViewable = new Date(birth);
                birthViewable = birthViewable.getDate() + "."
                    + (1+birthViewable.getMonth()) + "."
                    + birthViewable.getFullYear();

                $("#student" + id + " .birth").html(birth);
                $("#student" + id + " .birthViewable").html(birthViewable);

                $("#student" + id + " .gender").html(gender);
                $("#student" + id + " .group").html(group);
                $("#status").html("");
            } else {
                $("#status").html("Ошибка. Перепроверьте введенные данные.");
                lightOn("#student" + id,failColor);
            }
        });
    });

    $("#studentSearchForm").submit(function (event) {
        event.preventDefault();
        $.get("student/selectStudent", $("#studentSearchForm").serialize(), function(data) {
            table.html("");

            data = JSON.parse(data);

            var errors = data.errors;
            if(jQuery.isEmptyObject(errors)) {
                table.append(
                    "\t<thead><tr>\n" +
                    "\t\t<th>ID</th>\n" +
                    "\t\t<th>ФИО</th>\n" +
                    "\t\t<th>День рождения</th>\n" +
                    "\t\t<th>Пол</th>\n" +
                    "\t\t<th>Группа</th>\n" +
                    "\t\t<th>Операции</th>\n" +
                    "\t</tr></thead><tbody></tbody>"
                );
                var students = data.students;


                students.forEach(function (student) {
                    addStudentRow(student);
                });

                $("#status").html("");

                addDeleteEventHandler("#studentOutput .outputTable tr",
                    "student");
                addUpdateEventHandler("#studentOutput .outputTable tr",
                    "student");
            } else {
                $("#status").html("Нет записей в таблице.");
            }
        });
    })
    /** /Студент **/
})