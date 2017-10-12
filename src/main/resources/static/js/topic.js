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
                required: true,
                minlength: 5
            },
            nodeSlug: {
                required: true
            }
        },
        messages: {
            title: {
                required: '请输入主题标题',
                minlength: '标题最少5个字符'
            },
            editor: {
                required: '请输入主题内容',
                minlength: '内容最少5个字符'
            },
            nodeSlug: {
                required: '请选择节点'
            }
        },
        submitHandler: function () {
            $.ajax({
                url: "/topic/publish",
                type: "POST",
                data: $('#topic-form').serialize(),
                success: function (data, textStatus, jqXHR) {
                    console.log(data);
                    if (data && data.success) {
                        Roo.alertOk('主题发布成功');
                    } else {
                        Roo.alertError(data.msg || '主题发布失败');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    if (jqXHR.status == 400) {
                        Roo.alertError('没有权限操作');
                        return;
                    }
                    console.error("The following error occurred: " + textStatus, errorThrown);
                }
            });
        }
    });

    $('#nodeSlug').on('change', function () {
        $('#nodeTitle').val($("#topic-form #nodeSlug option:selected").text());
    });

});