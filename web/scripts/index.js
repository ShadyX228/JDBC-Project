$(document).ready(function(){
    /** Выбор формы **/
    $("#table select").change(
        function() {
            var table = $(this).val();
            showForm(table);

            $("#studentAction select").change(
                function() {
                    var action = $(this).val();
                    showActionForm(action);
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
                    showActionForm(action);
                    $("#groupSelect select").change(
                        function () {
                            var criteria = $(this).val();
                            $("#groupSelect .criteriaValue").show();
                            if(criteria == "ALL") {
                                $("#groupSelect .criteriaValue").hide();
                            }
                        }
                    );
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
    function showActionForm(action) {
        switch(action) {
            case "1": {
                hideAll(2);
                $("#studentAdd").show();
                break;
            }
            case "2": {
                hideAll(2);
                $("#studentSelect").show();
                break;
            }
            case "3": {
                hideAll(2);
                $("#studentUpdate").show();
                break;
            }
            case "4": {
                hideAll(2);
                $("#studentDelete").show();
                break;
            }
            default: {
                hideAll(2);
                break;
            }
        }
    }
    function hideAll(blockGroup) {
        if(blockGroup == 1) {
            $("#teacherAction").hide();
            $("#studentAction").hide();
            $("#groupAction").hide();
        }
        if(blockGroup == 2) {
            $("#studentAdd").hide();
            $("#studentSelect").hide();
            $("#studentUpdate").hide();
            $("#studentDelete").hide();
        }

    }
    /** /Отображение форм **/
});