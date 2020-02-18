$(document).ready(function () {
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
})