$(document).ready(function(){
    var successColor = "#CACACA";
    var failColor = "red";
    /** Выбор таблицы **/

    /** Студент **/
    $("#studentAction").click(function () {
        $("#status").show();
        $("#studentsTable").show();
        $("#groupsTable").hide();
        $("#teachersTable").hide();

        $("#status").html("Загрузка...");

        $.post("studentSelectAll", function(data) {
            $("#studentOutput .outputTable").show();
            $("#studentOutput .outputTable").html(data);
            $("#status").html("");

            addDeleteEventHandler("#studentOutput .outputTable tr","student");
            addUpdateEventHandler("#studentOutput .outputTable tr","student");
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
        $.post("studentAdd", {
            "name" : name,
            "birth" : birth,
            "gender" : gender,
            "group" : group}, function(data) {
            $("#status").html(data);
            var id = parseInt($("#status .lastId").html());
            if(!isNaN(id)) {
                $("#studentOutput .outputTable").append("<tr id=\"student" + id + "\">" +
                    "<td class=\"id\">" + id + "</td>" +
                    "<td class=\"name\">" + name + "</td>" +
                    "<td class=\"birth\">" + birth + "</td>" +
                    "<td class=\"gender\">" + gender + "</td>" +
                    "<td class=\"group\">" + group + "</td>" +
                    "<td class=\"opeations\"><a class=\"delete\" href=\"#deleteStudent" + id + "\">Удалить</a><br><a class=\"update\" href=\"#updateStudent" + id + "\">Изменить</a></td>" +
                    "</tr>");
                lightOn("#student" + id,successColor);
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
        var id =  $("#studentSearchForm .id").val();
        var name =  $("#studentSearchForm .name").val();
        var birth =  $("#studentSearchForm .birth").val();
        var gender =$("#studentSearchForm .gender").val();
        var group = $("#studentSearchForm .group").val();
        $.get("test", {"id" : id, "name" : name, "birth" : birth, "gender" : gender, "group" : group}, function(data) {
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
        $("#status").show();
        $("#studentsTable").hide();
        $("#groupsTable").show();
        $("#teachersTable").hide();

        $("#status").html("Загрузка...");

        $.post("groupSelectAll", function(data) {
            $("#groupOutput .outputTable").show();
            $("#groupOutput .outputTable").html(data);
            $("#status").html("");

            addDeleteEventHandler("#groupOutput .outputTable tr","group");
            addUpdateEventHandler("#groupOutput .outputTable tr","group");
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
                $("#groupOutput .outputTable").append("<tr id=\"group" + id + "\">" +
                    "<td class=\"id\">" + id + "</td>" +
                    "<td class=\"group\">" + group + "</td>" +
                    "<td class=\"opeations\">" +
                    "<a class=\"delete\" href=\"#deleteGroup" + id + "\">Удалить</a><br>" +
                    "<a class=\"update\" href=\"#updateGroup" + id + "\">Изменить</a><br>" +
                    "<a class=\"getInfo\" href=\"#getInfoGroup" + id + "\">Информация</a><br>" +
                    "<a class=\"putTeacherInGroup\" href=\"#putTeacherInGroup" + id + "\">Назначить преподавателя</a></td>" +
                    "</tr>");
                lightOn("#group" + id,successColor);

                addDeleteEventHandler("#group"+id, "group");
                addUpdateEventHandler("#group"+id, "group");
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
    $("#groupSearchForm").submit(function (event) {
        event.preventDefault();
        var id =  $("#groupSearchForm .id").val();
        var group = $("#groupSearchForm .group").val();
        $.get("groupSelect", {"id" : id, "group" : group}, function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                $("#groupOutput .outputTable").html(data);
                $("#status").html("");
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
        $("#studentsTable").hide();
        $("#groupsTable").hide();
        $("#teachersTable").show();

        $("#status").html("Загрузка...");
    });
    /** /Преподаватель **/

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
    $("#studentSearchFormClose").click(function (event) {
        event.preventDefault();
        $("#studentSearchForm").hide();
    })
    $("#studentSearch").click(function () {
        $("#studentSearchForm").show();
        $("#studentUpdateForm").hide();
        $("#studentAddForm").hide();
    })

    $("#groupAddFormClose").click(function (event) {
        event.preventDefault();
        $("#groupAddForm").hide();
    })
    $("#groupUpdateFormClose").click(function (event) {
        event.preventDefault();
        $("#groupUpdateForm").hide();
    })
    $("#closeGroupInfo").click(function (event) {
        event.preventDefault();
        $("#groupInfo").hide();
    })
    $("#groupSearchFormClose").click(function (event) {
        event.preventDefault();
        $("#groupSearchForm").hide();
    })
    $("#groupSearch").click(function () {
        $("#groupSearchForm").show();
        $("#groupUpdateForm").hide();
        $("#groupAddForm").hide();
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
                var error = parseInt($("#status .error").html());
                if(isNaN(error)) {
                    $("#" + table + id).hide();
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
            $("#groupNumber").html($(selector).find(".group").html())
            $("#status").html("Загружаю...");

            $.get("groupGetInfo", {"id" : id}, function(data) {
                $("#groupInfo").show();
                $("#info").html(data);
                addTeacherRemovingFromGroupHandler("#groupInfo table tr");
                $("#status").html("");
            });

        })
    }
    function addGetTeachersHandler(selector) {
        $("#groupOutput .outputTable " + selector).on("click", ".putTeacherInGroup", function() {
            var a = $(this);
            var href = a.attr("href");
            var id = parseInt(href.match(/\d+/));
            $("#groupNumber").html($(selector).find(".group").html())
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

            var groupId = $("#teacher" + teacherId).find(".groupId").html();

            $.post("teacherDeleteGroup", {"teacherId" : teacherId, "groupId" : groupId}, function(data) {
                $("#status").html(data);
                var error = parseInt($("#status .error").html());
                if(isNaN(error)) {
                    $("#teacher"+ teacherId).hide();
                    $("#status").html("");
                } else {
                    lightOn("#teacher" + teacherId, failColor);
               }
            });

        })
    }
    function addTeacherPuttingInGroupHandler(selector) {
        $(selector).on("click", ".putInGroup", function() {
            var a = $(this);
            var href = a.attr("href");
            var teacherId = parseInt(href.match(/\d+/));

            var groupId = $("#teacher" + teacherId).find(".groupId").html();

            alert(teacherId + " " + groupId);
            $.post("teacherPutInGroup", {"teacherId" : teacherId, "groupId" : groupId}, function(data) {
                $("#status").html(data);
                var error = parseInt($("#status .error").html());
                if(isNaN(error)) {
                    $("#teacher"+ teacherId).hide();
                    $("#status").html("");
                } else {
                    lightOn("#teacher" + teacherId, failColor);
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
    /** /Функции **/

});