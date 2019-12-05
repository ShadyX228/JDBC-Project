$(document).ready(function(){
    /** Выбор таблицы **/
    /** Студент **/
    $("#studentAction").click(function () {
        $("#studentsTable").show();
        $("#groupsTable").hide();
        $("#teachersTable").hide();

        $("#status").html("Загрузка...");

        $.post("studentSelectAll", function(data) {
            $("#studentOutput .outputTable").show();
            $("#studentOutput .outputTable").html(data);
            $("#status").html("");
            /** Запрос на удаление **/
            addDeleteEventHandler("#studentOutput .outputTable tr","student");
            addUpdateEventHandler("#studentOutput .outputTable tr","student");
            /** /Запрос на удаление **/
        });
    });
    $("#studentAdd").click(function () {
        $("#studentAddForm").show();
    })
    $("#studentAddForm").submit(function (event) {
        event.preventDefault();
        $("#status").html("Загрузка...");
        var name = $("#studentAddForm .name").val();
        var birth = $("#studentAddForm .birth").val();
        var gender = $("#studentAddForm .gender").val();
        var group = $("#studentAddForm .group").val();
        $.post("studentAdd", {
            "name" : name,
            "birth" : birth,
            "gender" : gender,
            "group" : group}, function(data) {
            $("#status").html(data);
            var id = parseInt($("#status .lastId").html());
            if(!isNaN(id)) {
                lightOn("#student" + id,"#AEB5FF");
                $("#studentOutput .outputTable").append("<tr id=\"student" + id + "\">" +
                    "<td class=\"id\">" + id + "</td>" +
                    "<td class=\"name\">" + name + "</td>" +
                    "<td class=\"birth\">" + birth + "</td>" +
                    "<td class=\"gender\">" + gender + "</td>" +
                    "<td class=\"group\">" + group + "</td>" +
                    "<td class=\"opeations\"><a class=\"delete\" href=\"#deleteStudent" + id + "\">Удалить</a><br><a class=\"update\" href=\"#updateStudent" + id + "\">Изменить</a></td>" +
                    "</tr>");
                addDeleteEventHandler("#student"+id, "student");
                addUpdateEventHandler("#student"+id, "student");
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
        $.post("studentUpdate", {"id" : id, "name" : name, "birth" : birth, "gender" : gender, "group" : group}, function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                lightOn("#student" + id,"#AEB5FF");
                $("#student" + id + " .name").html(name);
                $("#student" + id + " .birth").html(birth);
                $("#student" + id + " .gender").html(gender);
                $("#student" + id + " .group").html(group);
            }
        });
    });
    /** /Студент **/

    $("#groupAction").click(function () {
        $("#studentsTable").hide();
        $("#groupsTable").show();
        $("#teachersTable").hide();

        $("#status").html("Загрузка...");
    });
    $("#teacherAction").click(function () {
        $("#studentsTable").hide();
        $("#groupsTable").hide();
        $("#teachersTable").show();

        $("#status").html("Загрузка...");
    });
    /** /Выбор таблицы **/


    /** Прочие обработчики **/
    $("#studentUpdateFormClose").click(function (event) {
        event.preventDefault();
        $("#studentUpdateForm").hide();
    })
    $("#studentAddFormClose").click(function (event) {
        event.preventDefault();
        $("#studentAddForm").hide();
    })
    /** /Прочие обработчики **/

    /** Функции **/
    function addDeleteEventHandler(selector, table) {
        $(selector).on("click", ".delete", function() {
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
            $("#status").html("Загрузка...");
            $.post(page, {"criteria" : "ID", "criteriaValue" : id}, function(data) {
                $("#status").html(data);
                $("#" + table + id).hide();
            });
        })
    }
    function addUpdateEventHandler(selector, table) {
        $(selector).on("click", ".update", function() {
            var a = $(this);
            var href = a.attr("href");var id = parseInt(href.match(/\d+/));

            $("#studentUpdateForm").show();
            var name = $("#" + table + id + " .name").html();
            var birth = $("#" + table + id + " .birth").html();;
            var gender = $("#" + table + id + " .gender").html();;
            var group = $("#" + table + id + " .group").html();
            $("#studentUpdateForm .id").val(id);
            $("#studentUpdateForm .name").val(name);
            $("#studentUpdateForm .birth").val(birth);
            $("#studentUpdateForm .gender").val(gender);
            $("#studentUpdateForm .group").val(group);
        })
    }
    function lightOn(selector, color) {
        $(selector).css("background-color", color);
        setTimeout(function() {
            $(selector).css("background-color", "white");
        }, 1000)

    }
    /** /Функции **/

});