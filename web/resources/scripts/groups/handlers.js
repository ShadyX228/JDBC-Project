$(document).ready(function () {
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
})