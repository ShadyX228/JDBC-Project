$(document).ready(function () {
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
    $("#studentSearchForm .group").keyup(function () {
        var group = $(this).val();
        $.get("groupSelect", {"group" : group}, function(data) {
            $("#status").html(data);
            var error = parseInt($("#status .error").html());
            if(isNaN(error)) {
                $("#status").html("");
                $("#studentSearchForm .submit").prop("disabled", false);
            } else {
                $("#studentSearchForm .submit").prop("disabled", true);
                $("#status").html("Группы с введенным номером не существует.");
            }
        });
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

    $("#teacherUpdateFormClose").click(function (event) {
        event.preventDefault();
        $("#teacherUpdateForm").hide();
    })
    $("#teacherAddFormClose").click(function (event) {
        event.preventDefault();
        $("#teacherAddForm").hide();
    })
    $("#teacherSearchFormClose").click(function (event) {
        event.preventDefault();
        $("#teacherSearchForm").hide();
    })
    $("#teacherSearch").click(function () {
        $("#teacherSearchForm").show();
        $("#teacherUpdateForm").hide();
        $("#teacherAddForm").hide();
    })
    $("#closeTeacherInfo").click(function (event) {
        event.preventDefault();
        $("#teacherInfo").hide();
    })
    $("#teacherInfo #putTeacherInGroup .group").keyup(function () {
        var group = $(this).val();
        $.get("groupSelect", {"group" : group}, function(data) {
            data = JSON.parse(data);

            var errors = data.errors;
            if(jQuery.isEmptyObject(errors)) {
                $("#status").html("");
                $("#putTeacherInGroup .submit").prop("disabled", false);
            } else {
                $("#putTeacherInGroup .submit").prop("disabled", true);
                $("#status").html("Группы с введенным номером не существует.");
            }
        });

    })
    /** /Прочие обработчики **/
})