$(function(){
    //加载数据源
    $.ajax({
        type: "post",
        url: "/data/base/list",
        dataType: 'json',
        contentType: 'application/json',
        data:{},
        success: function(result){
            var treeData = [];
            for (var i=0; i<result.data.length; i++) {
                var obj = {};
                obj.text = result.data[i].name;
                obj.level = 0;
                obj.id = result.data[i].id;
                var modelInfoList = result.data[i].modelInfoList;
                if (modelInfoList !== null && modelInfoList.length > 0) {
                    obj.nodes = [];
                    for (var k=0; k<modelInfoList.length; k++) {
                        obj.nodes.push({
                            "text":modelInfoList[k].title,
                            "level":1,
                            "id":modelInfoList[k].id,
                            "sql":modelInfoList[k].content,
                            "datasourceName":obj.text,
                            "datasourceId":modelInfoList[k].dataSourceId,
                            "datasource":result.data[i]
                        });
                    }
                }

                treeData.push(obj);
            }
            $('#treeview1').treeview({
                data: treeData,
                onNodeSelected: function(event, data) {
                    $("#table_sql").html("");
                    if (data.sql !== null && data.sql !== "" && data.sql !== undefined) {
                        $("#datasource_driverClassName").val(data.datasource.driverClassName);
                        $("#datasource_id").val(data.datasource.id);
                        $("#datasource_username").val(data.datasource.username);
                        $("#datasource_password").val(data.datasource.password);
                        $("#datasource_url").val(data.datasource.url);

                        $.ajax({
                            type: "post",
                            url: "/basic/getTableHead",
                            dataType: 'json',
                            headers: getHeads(),
                            data:{"sql":data.sql},
                            success: function(result){
                                createTable(result.data);
                                createCondition(result.data);
                            }
                        });
                    }
                    $("#title").val(data.text);
                    $("#content").val(data.sql);
                    $("#dataSourceId").text(data.datasourceName);
                    $("#dataSourceId").attr("data-source-id", data.datasourceId);
                    $("#dataSourceId").attr("data-id", data.id);

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
        if (checkData(d, $("#form"))) {
            $.ajax({
                type: "post",
                url: "/data/base/save",
                dataType: 'json',
                contentType: 'application/json',
                data:JSON.stringify(d),
                success: function(result){
                    console.log(result);
                    if (result.code === '200') {
                        alert('成功');
                    }
                    $("#close_connection").click();
                }
            });
        }
    });

    //创建模版
    $("#create_model").click(function(){
        var node = $('#treeview1').treeview('getSelected')[0];
        if (node === undefined || node.level !== 0) {
            alert('模版只能创建在数据源下面');
            return;
        }
        $("#table_sql").html("");
        $('#form2')[0].reset();
        console.log(node);
        $("#dataSourceId").text(node.text);
        $("#dataSourceId").attr("data-source-id", node.id);
        $("#dataSourceId").attr("data-id", "");
    });

    //保存模版
    $("#save_model").click(function(){
        var d = {};
        var t = $("#form2").serializeArray();
        $.each(t, function () {
            d[this.name] = this.value;
        });
        d.dataSourceId = $("#dataSourceId").attr("data-source-id");
        d.id = $("#dataSourceId").attr("data-id");
        if (checkData(d, $("#form2"))) {
            $.ajax({
                type: "post",
                url: "/model/save",
                dataType: 'json',
                contentType: 'application/json',
                data:JSON.stringify(d),
                success: function(result){
                    console.log(result);
                    if (result.code === '200') {
                        alert('成功');
                    }
                    $("#close_connection").click();
                }
            });
        }
    });

    $("#query_data").click(function(){
        var content = $("#content").val();
        if (content !== '') {
            var d = {};
            var t = $("#form3").serializeArray();
            $.each(t, function () {
                d[this.name] = this.value;
            });
            for (var key in d) {
                var v = d[key];
                if (v === '' || v === null || v === undefined) {
                    continue;
                }
                content = content.replace(key, '"' + v + '"');
            }

            getSqlResult(content);
        }

        return false;
    });

    //表单内容发生变化时，检查表单内容
    $("form input").change(function(){
        if ($(this).next().html().length > 0) {
            var form = $(this).parent().parent().parent();
            var d = {};
            var t = form.serializeArray();
            $.each(t, function () {
                d[this.name] = this.value;
            });
            checkData(d,form);
        }
    });

    //关闭窗口时触发
    $("#create_connection_window").on('hidden.bs.modal', function(){
        $('#form')[0].reset();
        $('#form').find("span").html("");
        $('#form').find(".has-error").removeClass("has-error")
    });

});

    function getSqlResult(sql) {
        $.ajax({
            type: "post",
            url: "/basic/sql",
            dataType: 'json',
            headers: getHeads(),
            data:{"sql":sql},
            success: function(result){
                createTable(result.data);
            }
        });
    }

    function createTable(data) {
        var sqlEntitieList = data.sqlEntitieList;
        if (sqlEntitieList !== null && sqlEntitieList.length > 0) {
            var th = "";
            var thArr = [];
            for (var i=0; i<sqlEntitieList.length; i++) {
                var fieldName = sqlEntitieList[i].fieldName !== null ? sqlEntitieList[i].fieldName : sqlEntitieList[i].aliasCode;
                th += "<th>" + fieldName + "</th>";
                thArr.push(sqlEntitieList[i].aliasCode !== null ? sqlEntitieList[i].aliasCode : sqlEntitieList[i].field);
            }
            $("#table_sql").html("<thead><tr>" + th + "</thead></tr>");

            var list = data.list;
            if (list !== null && list.length > 0) {
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

    function createCondition(data) {
        $("#condition").html("");
        var div = "";
        var conditions = data.conditions;
        if (conditions !== null && conditions.length > 0) {
            for (var i=0; i<conditions.length; i++) {
                var module = "";
                if (conditions[i].moduleType === 'input') {
                    module = `\ 
                           <div class="col-sm-3">\
                           <input type="text" id="`+conditions[i].moduleCode+`" name="`+conditions[i].moduleCode+`" class="form-control"/>\
                           </div>\
                          `;
                } else if(conditions[i].moduleType === 'date') {
                    module = `\
                <div class="col-sm-3 input-append date form_datetime">\
                    <input size="16" type="text" id="`+conditions[i].moduleCode+`" name="`+conditions[i].moduleCode+`" class="form-control" readonly="readonly"/>\
                    <span class="add-on"><i class="icon-remove"></i></span>\
                    <span class="add-on"><i class="icon-th"></i></span>\
                </div>\
                `;
                }
                div += `\
            <div class="form-group">\
                <label class="col-sm-1 control-label">`+conditions[i].moduleName+`</label>\
                 `+module+`\
            </div>\
            `;
            }

            $("#condition").html(div);

            $(".form_datetime").datetimepicker({
                format: "yyyy-mm-dd hh:ii",
                autoclose: true,
                todayBtn: true,
                // bootcssVer:3,
                language:'zh-CN',
                minuteStep: 10
            });
        }
    }

    function checkData(d, obj) {
        for (var key in d) {
            if (d[key] == '' || d[key].length == 0) {
                $("#"+key).next().html($("#"+key).parent().prev().html() + "格式有误").css("color", "#a94442");
                $("#"+key).parent().parent().addClass("has-error");
            } else {
                $("#"+key).next().html('').css("color", "");
                $("#"+key).parent().parent().removeClass("has-error");
            }
        }

        return obj.find(".has-error").length == 0;
    }

    function getHeads() {
        var heads = {};
        heads.driverClassName = $("#datasource_driverClassName").val();
        heads.url = $("#datasource_url").val();
        heads.username = $("#datasource_username").val();
        heads.password = $("#datasource_password").val();
        heads.id = $("#datasource_id").val();
        return heads;
    }