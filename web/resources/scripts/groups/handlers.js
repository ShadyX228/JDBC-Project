$(document).ready(function () {
    $("#groupAddFormClose").click(function (event) {
        event.preventDefault();
        $("#groupAddForm").hide();
    });
    $("#groupUpdateFormClose").click(function (event) {
        event.preventDefault();
        $("#groupUpdateForm").hide();
    });
    $("#closeGroupInfo").click(function (event) {
        event.preventDefault();
        $("#groupTeachersTable .display").DataTable().destroy();
        $("#freeTeachersTable .display").DataTable().destroy();
        $("#groupTeachersTable").remove();
        $("#freeTeachersTable").remove();
        $("#groupInfo").hide();
    });
});