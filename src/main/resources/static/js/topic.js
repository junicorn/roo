$(document).ready(function () {
    var mditor = Mditor.fromTextarea(document.getElementById('editor'));
    mditor.on('ready', function () {
        mditor.value = '> 请输入主题内容';
    });

    $("#topic-form").validate({
        rules: {
            title: {
                required: true,
                minlength: 5
            },
            editor: {
                required: true
            }
        },
        messages: {
            title: {
                required: '请输入标题',
                minlength: '标题最少5个字符'
            },
            editor: {
                required: '请输入内容'
            }
        },
        submitHandler: function () {
            $.ajax({
                url: "/topic/publish",
                type: "POST",
                data: $('#topic-form').serialize(),
                success: function (data, textStatus, jqXHR) {
                    if (data && data.success) {
                        roo.alertBox('主题发布成功');
                        window.location.href = '/';
                    } else {
                        roo.alertBox(data.msg || '主题发布失败');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    if (jqXHR.status == 400) {
                        roo.alertBox('没有权限操作');
                        return;
                    }
                    console.error("The following error occurred: " + textStatus, errorThrown);
                }
            });
        }
    });
});