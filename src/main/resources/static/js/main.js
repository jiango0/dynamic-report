$(function(){
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
                url: "data/base/save",
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

    $("#create_connection_window").on('hidden', function(){
        $('#form')[0].reset();
    });

});

function checkData(d) {
    for (var key in d) {
        if (d[key] == '' || d[key].length == 0) {
            $("#"+key).next().html($("#"+key).parent().prev().html() + "格式有误");
            $("#"+key).parent().parent().addClass("error");
        } else {
            $("#"+key).next().html('');
            $("#"+key).parent().parent().removeClass("error");
        }
    }

    return $("#form").find(".error").length == 0;
}


