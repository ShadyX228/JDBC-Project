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

            $("#teacherAction select").change(
                function() {
                    var action = $(this).val();
                    showTeacherActionForm(action);
                    $("#teacherSelect select").change(
                        function () {
                            var criteria = $(this).val();
                            $("#teacherSelect .criteriaValue").show();
                            if(criteria == "ALL") {
                                $("#teacherSelect .criteriaValue").hide();
                            }
                        }
                    );
                }
            );
        }
    )
    /** /Выбор формы **/

    /** /Ajax-запросы **/

    $("#studentAdd").submit(function (event) {
        event.preventDefault();
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
    $("#studentSelect").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var criteria = $("#studentSelect select").val();
        var criteriaValue = $("#studentSelect input").val();
        $.get("studentSelect", {"criteria" : criteria, "criteriaValue" : criteriaValue}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#studentUpdate").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var id = $("#studentUpdate .id").val();
        var criteria = $("#studentUpdate select").val();
        var criteriaValue = $("#studentUpdate .criteriaValue").val();
        $.post("studentUpdate", {"id" : id, "criteria" : criteria, "criteriaValue" : criteriaValue}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#studentDelete").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var criteria = $("#studentDelete select").val();
        var criteriaValue = $("#studentDelete input").val();
        $.post("studentDelete", {"criteria" : criteria, "criteriaValue" : criteriaValue}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });

    $("#teacherAdd").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var name = $("#teacherAdd .name").val();
        var birth = $("#teacherAdd .birth").val();
        var gender = $("#teacherAdd .gender input[type=radio]:checked").val();
        var group = $("#teacherAdd .group").val();
        $.post("teacherAdd", {
            "name" : name,
            "birth" : birth,
            "gender" : gender,
            "group" : group}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#teacherSelect").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var criteria = $("#teacherSelect select").val();
        var criteriaValue = $("#teacherSelect input").val();
        $.get("teacherSelect", {"criteria" : criteria, "criteriaValue" : criteriaValue}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#teacherUpdate").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var id = $("#teacherUpdate .id").val();
        var criteria = $("#teacherUpdate select").val();
        var criteriaValue = $("#teacherUpdate .criteriaValue").val();
        $.post("teacherUpdate", {"id" : id, "criteria" : criteria, "criteriaValue" : criteriaValue}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#teacherDelete").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var id = $("#teacherDelete .id").val();
        var criteria = $("#teacherDelete select").val();
        var criteriaValue = $("#teacherDelete .criteriaValue").val();
        $.post("teacherDelete", {"id" : id, "criteria" : criteria, "criteriaValue" : criteriaValue}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#teacherPutInGroup").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var id = $("#teacherPutInGroup .id").val();
        var number = $("#teacherPutInGroup .number").val();
        $.post("teacherPutInGroup", {"id" : id, "number" : number}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#teacherDeleteGroup").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var id = $("#teacherDeleteGroup .id").val();
        var number = $("#teacherDeleteGroup .number").val();
        $.post("teacherDeleteGroup", {"id" : id, "number" : number}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });

    $("#groupAdd").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var number = $("#groupAdd .number").val();
        $.post("groupAdd", {"number" : number}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#groupSelect").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var number = $("#groupSelect .number").val();
        var checkAll = $("#groupSelect input[type=checkbox]").is(":not(:checked)");
        $.get("groupSelect", {"number" : number, "checkAll" : checkAll}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#groupDelete").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var number = $("#groupDelete .number").val();
        $.post("groupDelete", {"number" : number}, function(data) {
            $(".message").html(data);
            $(".status").html("");
        });
    });
    $("#groupUpdate").submit(function (event) {
        event.preventDefault();
        $(".status").html("Загружаю...");
        var number = $("#groupUpdate .number").val();
        var newNumber = $("#groupUpdate .newNumber").val();
        $.post("groupUpdate", {"number" : number, "newNumber" : newNumber}, function(data) {
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
    function showTeacherActionForm(action) {
        hideAll(2);
        switch(action) {
            case "1": {
                $("#teacherAdd").show();
                break;
            }
            case "2": {
                $("#teacherSelect").show();
                break;
            }
            case "3": {
                $("#teacherUpdate").show();
                break;
            }
            case "4": {
                $("#teacherDelete").show();
                break;
            }
            case "5": {
                $("#teacherPutInGroup").show();
                break;
            }
            case "6": {
                $("#teacherDeleteGroup").show();
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
            case "4": {
                $("#groupUpdate").show();
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

            $("#teacherAdd").hide();
            $("#teacherSelect").hide();
            $("#teacherUpdate").hide();
            $("#teacherDelete").hide();
            $("#teacherPutInGroup").hide();
            $("#teacherSelectGroups").hide();
            $("#teacherDeleteGroup").hide();

            $("#groupAdd").hide();
            $("#groupSelect").hide();
            $("#groupDelete").hide();
            $("#groupUpdate").hide();
        }

    }
    /** /Отображение форм **/
});