$(document).ready(function(){
    /** Выбор формы **/
    $("#table select").change(
        function() {
            var table = $(this).val();
            showForm(table);

            $("#studentAction select").change(
                function() {
                    var action = $(this).val();
                    showStudentActionForm(action);
                    $("#studentSelect select").change(
                        function () {
                            var criteria = $(this).val();
                            $("#studentSelect .criteriaValue").show();
                            if(criteria == "ALL") {
                                $("#studentSelect .criteriaValue").hide();
                            }
                        }
                    );
                }
            );
            $("#groupAction select").change(
                function() {
                    var action = $(this).val();
                    showGroupActionForm(action);
                    var check = $(this).is(":not(:checked)");
                    var number = $("#groupSelect .number");
                    $("#groupSelect input[type=checkbox]").click(function () {
                        check = $(this).is(":not(:checked)");
                        if(check) {
                            number.show();
                        } else {
                            number.hide();
                        }
                    })
                }
            );

        }
    )
    /** /Выбор формы **/

    /** /Ajax-запросы **/
    $("#studentAdd .submit").click(function () {
        $(".status").html("Загружаю...");
        var name = $("#studentAdd .name").val();
        var birth = $("#studentAdd .birth").val();
        var gender = $("#studentAdd .gender input[type=radio]:checked").val();
        var group = $("#studentAdd .group").val();
        $.post("studentAdd", {
            "name" : name,
            "birth" : birth,
            "gender" : gender,
            "group" : group}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#studentSelect .submit").click(function () {
        $(".status").html("Загружаю...");
        var criteria = $("#studentSelect select").val();
        var criteriaValue = $("#studentSelect input").val();
        $.get("studentSelect", {"criteria" : criteria, "criteriaValue" : criteriaValue}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#studentUpdate .submit").click(function () {
        $(".status").html("Загружаю...");
        var id = $("#studentUpdate .id").val();
        var criteria = $("#studentUpdate select").val();
        var criteriaValue = $("#studentUpdate .criteriaValue").val();
        $.post("studentUpdate", {"id" : id, "criteria" : criteria, "criteriaValue" : criteriaValue}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#studentDelete .submit").click(function () {
        $(".status").html("Загружаю...");
        var criteria = $("#studentDelete select").val();
        var criteriaValue = $("#studentDelete input").val();
        $.get("studentDelete", {"criteria" : criteria, "criteriaValue" : criteriaValue}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });

    $("#groupSelect .submit").click(function () {
        $(".status").html("Загружаю...");
        var number = $("#groupSelect .number").val();
        var checkAll = $("#groupSelect input[type=checkbox]").is(":not(:checked)");
        $.get("groupSelect", {"number" : number, "checkAll" : checkAll}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    /** /Ajax-запросы **/



    /** Отображение форм **/
    function showForm(table) {
        switch(table) {
            case "1": {
                hideAll(1);
                hideAll(2);
                $("#studentAction").show();
                break;
            }
            case "2": {
                hideAll(1);
                hideAll(2);
                $("#teacherAction").show();
                break;
            }
            case "3": {
                hideAll(1);
                hideAll(2);
                $("#groupAction").show();
                break;
            }
            default: {
                hideAll(1);
                hideAll(2);
                break;
            }
        }
    }
    function showStudentActionForm(action) {
        hideAll(2);
        switch(action) {
            case "1": {
                $("#studentAdd").show();
                break;
            }
            case "2": {
                $("#studentSelect").show();
                break;
            }
            case "3": {
                $("#studentUpdate").show();
                break;
            }
            case "4": {
                $("#studentDelete").show();
                break;
            }


            default: {
                break;
            }
        }
    }
    function showGroupActionForm(action) {
        hideAll(2);
        switch(action) {
            case "1": {
                $("#groupAdd").show();
                break;
            }
            case "2": {
                $("#groupSelect").show();
                break;
            }
            case "3": {
                $("#groupDelete").show();
                break;
            }


            default: {
                break;
            }
        }
    }
    function hideAll(blockGroup) {
        if(blockGroup === 1) {
            $("#teacherAction").hide();
            $("#studentAction").hide();
            $("#groupAction").hide();
        }
        if(blockGroup === 2) {
            $("#studentAdd").hide();
            $("#studentSelect").hide();
            $("#studentUpdate").hide();
            $("#studentDelete").hide();

            $("#groupAdd").hide();
            $("#groupSelect").hide();
            $("#groupDelete").hide();
        }

    }
    /** /Отображение форм **/
});