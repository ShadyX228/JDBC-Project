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
        $("#groupNumber").html($("#group" + id).find(".group").html())
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

        $.post("teacherDeleteGroup", {"teacherId" : teacherId, "groupId" : groupId}, function(data) {
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

        $.post("teacherDeleteGroup", {"teacherId" : teacherId, "groupId" : groupId}, function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                $("#groupTeacher"+ groupId).hide();
                $("#groupTeacher"+ groupId).remove();
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
                $("#groups").html("нет. <span style=\"display: none\" class=\"error\">-1</span>");
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
/** /Функции **/