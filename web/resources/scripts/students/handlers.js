$(document).ready(function () {
    $("#studentUpdateFormClose").click(function (event) {
        event.preventDefault();
        $("#studentUpdateForm").hide();
    });
    $("#studentAddFormClose").click(function (event) {
        event.preventDefault();
        $("#studentAddForm").hide();
    });
    $("#studentSearchFormClose").click(function (event) {
        event.preventDefault();
        $("#studentSearchForm").hide();
    });
    $("#studentSearch").click(function () {
        $("#studentSearchForm").show();
        $("#studentUpdateForm").hide();
        $("#studentAddForm").hide();
    });
    $("#studentSearchForm .group").keyup(function () {
        var group = $(this).val();
        if(!jQuery.isEmptyObject(group)) {
            $.get("group/selectGroup", {"group": group}, function (data) {
                $("#status").html(data);
                var errors = data.errors;
                if (jQuery.isEmptyObject(errors)) {
                    $("#status").html("");
                    $("#studentSearchForm .submit").prop("disabled", false);
                } else {
                    $("#studentSearchForm .submit").prop("disabled", true);
                    $("#status").html(errors);
                }
            });
        } else {
            $("#studentSearchForm .submit").prop("disabled", false);
            $("#status").html("");
        }
    })
});