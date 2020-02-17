$(document).ready(function () {
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
})