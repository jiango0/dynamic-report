$(function(){

    //加载数据源
    $.ajax({
        type: "post",
        url: "/data/base/list",
        dataType: 'json',
        contentType: 'application/json',
        data:{},
        success: function(result){
            console.log(result);
            var treeData = [];
            for (var i=0; i<result.data.length; i++) {
                var obj = {};
                obj.text = result.data[i].name;
                var modelInfoList = result.data[i].modelInfoList;
                if (modelInfoList !== null && modelInfoList.length > 0) {
                    obj.nodes = [];
                    for (var k=0; k<modelInfoList.length; k++) {
                        obj.nodes.push({"text":modelInfoList[k].title});
                    }
                }

                treeData.push(obj);
            }
            console.log(treeData)
            $('#treeview1').treeview({
                data: treeData,
                onNodeSelected: function(event, data) {

                }
            });
        }
    });

    //提交数据源
    $("#save_connection").click(function(){
        var d = {};
        var t = $("#form").serializeArray();
        $.each(t, function () {
            d[this.name] = this.value;
        });
        if (checkData(d)) {
            $.ajax({
                type: "post",
                url: "/data/base/save",
                dataType: 'json',
                contentType: 'application/json',
                data:JSON.stringify(d),
                success: function(data){
                    console.log(data);
                    $("#close_connection").click();
                }
            });
        }
    });

    //表单内容发生变化时，检查表单内容
    $("#form input").change(function(){
        if ($(this).next().html().length > 0) {
            var d = {};
            var t = $("#form").serializeArray();
            $.each(t, function () {
                d[this.name] = this.value;
            });
            checkData(d);
        }
    });

    //关闭窗口时触发
    $("#create_connection_window").on('hidden.bs.modal', function(){
        $('#form')[0].reset();
        $('#form').find("span").html("");
        $('#form').find(".has-error").removeClass("has-error")
    });

    getSqlResult();

});

    function getSqlResult() {
        $.ajax({
            type: "post",
            url: "/basic/sql",
            dataType: 'json',
            data:{"sql":"SELECT ri.title, ri.content, ec.id, ec.content eccontent, ec.create_date, ec.create_user_id, ec.del_flag, ec.info_id, ec.parent_id, ec.shelve_flag, (select title from resource_info where id = ec.info_id) as ti FROM event_comment ec LEFT JOIN resource_info ri ON ec.info_id = ri.id"},
            success: function(result){
                console.log(result);

                var sqlEntitieList = result.data.sqlEntitieList;
                if (sqlEntitieList !== '' && sqlEntitieList.length > 0) {
                    var th = "";
                    var thArr = [];
                    for (var i=0; i<sqlEntitieList.length; i++) {
                        var fieldName = sqlEntitieList[i].fieldName !== null ? sqlEntitieList[i].fieldName : sqlEntitieList[i].aliasCode;
                        th += "<th>" + fieldName + "</th>";
                        thArr.push(sqlEntitieList[i].aliasCode !== null ? sqlEntitieList[i].aliasCode : sqlEntitieList[i].field);
                    }
                    $("#table_sql").html("<thead><tr>" + th + "</thead></tr>");
                    console.log(thArr);

                    var list = result.data.list;
                    if (list !== '' && list.length > 0) {
                        var tr = "";
                        for (var k=0; k<list.length; k++) {
                            tr += "<tr>";

                            for (var j=0; j<thArr.length; j++) {
                                tr += "<td>" + list[k][thArr[j]] + "</td>"
                            }

                            tr += "</tr>";
                        }
                        $("#table_sql").append("<tbody>" + tr + "</tbody>");
                    }
                }

            }
        });
    }

    function checkData(d) {
        for (var key in d) {
            if (d[key] == '' || d[key].length == 0) {
                $("#"+key).next().html($("#"+key).parent().prev().html() + "格式有误").css("color", "#a94442");
                $("#"+key).parent().parent().addClass("has-error");
            } else {
                $("#"+key).next().html('').css("color", "");
                $("#"+key).parent().parent().removeClass("has-error");
            }
        }

        return $("#form").find(".error").length == 0;
    }