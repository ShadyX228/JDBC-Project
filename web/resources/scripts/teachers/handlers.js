$(document).ready(function () {
    $("#teacherUpdateFormClose").click(function (event) {
        event.preventDefault();
        $("#teacherUpdateForm").hide();
    });
    $("#teacherAddFormClose").click(function (event) {
        event.preventDefault();
        $("#teacherAddForm").hide();
    });
    $("#closeTeacherInfo").click(function (event) {
        event.preventDefault();
        $("#groups .display").DataTable().destroy();
        $("#putTeacherInGroup")[0].reset();
        $("#teacherInfo").hide();
    });
    $("#teacherInfo #putTeacherInGroup .group").keyup(function () {
        var group = $(this).val();
        if(!jQuery.isEmptyObject(group)) {
            $.get("group/selectGroup", {"group": group}, function (data) {
                var errors = data.errors;
                if (jQuery.isEmptyObject(errors)) {
                    $("#status").html("");
                    $("#putTeacherInGroup .submit").prop("disabled", false);
                } else {
                    $("#putTeacherInGroup .submit").prop("disabled", true);
                    $("#status").html(errors);
                }
            });
        } else {
            $("#putTeacherInGroup .submit").prop("disabled", false);
            $("#status").html("");
        }
    })
});