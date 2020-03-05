$(document).ready(function () {
    var successColor = "#E7E7E7";
    var failColor = "red";
    var table = $("#groupOutput .outputTable");

    /** Группа **/
    var groupTableHead = "\t<thead><tr>\n" +
        "\t\t<th>ID</th>\n" +
        "\t\t<th>Номер</th>\n" +
        "\t\t<th>Операции</th>\n" +
        "\t</tr></thead><tbody></tbody>";


    $("#status").show();
    $("#studentsTable").hide();
    $("#groupsTable").show();
    $("#teachersTable").hide();
    $("#status").html("Загрузка...");
    $.get("group/selectAllGroups", function (data) {
        table.show().html("");

        data = JSON.parse(data);
        var errors = data.errors;

        if (jQuery.isEmptyObject(errors)) {
            table.append(groupTableHead);

            var groups = data.groups;

            groups.forEach(function (group) {
                addGroupRow(group);
            });

            $("#status").html("");

            addDeleteEventHandler("#groupOutput .outputTable tr",
                "group");
            addUpdateEventHandler("#groupOutput .outputTable tr",
                "group");
            addGroupInfoEventHandler("tr");
            addGetTeachersHandler("tr");

        } else {
            $("#status").html("Нет записей в таблице.");
        }
    });


    $("#groupAdd").click(function () {
        $("#groupAddForm").show();
        $("#groupUpdateForm").hide();
        $("#groupSearchForm").hide();
    })

    $("#groupAddForm").submit(function (event) {
        event.preventDefault();
        $("#status").html("Загрузка...");
        var number = $("#groupAddForm .group").val();
        $.post("group/addGroup", {
            "group": number
        }, function (data) {
            data = JSON.parse(data);
            var errors = data.errors;

            if (jQuery.isEmptyObject(errors)) {
                var id = data["lastId"];
                var group = {
                    "id": id,
                    "number": number
                };
                addGroupRow(group);

                lightOn("#group" + id, successColor);

                addDeleteEventHandler("#group" + id,
                    "group");
                addUpdateEventHandler("#group" + id,
                    "group");
                addGroupInfoEventHandler("#group" + id);
                addGetTeachersHandler("#group" + id);
                $("#status").html("");
            } else {
                $("#status").html(errors);
            }
        });
    })

    $("#groupUpdateForm").submit(function (event) {
        event.preventDefault();
        $("#status").html("Загружаю...");

        var id = $("#groupUpdateForm .id").val();
        var group = $("#groupUpdateForm .group").val();

        $.post("group/updateGroup", {"id": id, "group": group}, function (data) {
            data = JSON.parse(data);
            var errors = data.errors;

            if (jQuery.isEmptyObject(errors)) {
                lightOn("#group" + id, successColor);
                $("#group" + id + " .group").html(group);
                $("#status").html("");
            } else {
                $("#status").html(errors);
                lightOn("#group" + id, failColor);
            }
        });
    });

    $("#groupSearchForm .group").keyup(function () {
        var group = $(this).val();
        $.get("group/selectGroup", {"group": group}, function (data) {
            table.show().html("");

            data = JSON.parse(data);

            var errors = data.errors;
            if (jQuery.isEmptyObject(errors)) {
                $("#status").html("");
            } else {
                $("#status").html(errors);
            }

            table.append(groupTableHead);

            var groups = data.groups;

            groups.forEach(function (group) {
                addGroupRow(group);
            });

            addDeleteEventHandler("#groupOutput .outputTable tr",
                "group");
            addUpdateEventHandler("#groupOutput .outputTable tr",
                "group");
            addGroupInfoEventHandler("tr");
            addGetTeachersHandler("tr");
        });
    })
    /** /Группа **/
})