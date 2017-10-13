$(document).ready(function () {
    var mditor = Mditor.fromTextarea(document.getElementById('editor'));
    mditor.on('ready', function () {
        mditor.value = '> 请输入主题内容';
    });
    mditor.highlight = {};

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
            Roo.post("/topic/publish", $('#topic-form').serialize(),
                function (data, textStatus, jqXHR) {
                    console.log(data);
                    if (data && data.success) {
                        Roo.alertOk('主题发布成功', function () {
                            window.location.href = '/';
                        });
                    } else {
                        Roo.alertError(data.msg || '主题发布失败', function () {
                            if (data.code && data.code == 10000) {
                                window.location.reload();
                            }
                        });
                    }
                });
        }
    });

    $('#nodeSlug').on('change', function () {
        $('#nodeTitle').val($("#topic-form #nodeSlug option:selected").text());
    });

});