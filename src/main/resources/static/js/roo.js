var Roo = {};
Roo.alertBox = function (options) {
    new Noty({
        type: options.type || 'info',
        layout: options.layout || 'topRight',
        text: options.text || '提示',
        timeout: options.timeout || ( options.afterClose ? 800 : 1500),
        callbacks: {
            afterClose: options.afterClose || function () {
            }
        }
    }).show();
};

Roo.alertError = function (content, callback) {
    Roo.alertBox({
        type: 'error',
        text: content,
        afterClose: callback
    });
};

Roo.alertOk = function (content, callback) {
    Roo.alertBox({
        type: 'information',
        text: content,
        afterClose: callback
    });
};

Roo.post = function (url, data, success, error) {
    $.ajax({
        url: url,
        type: "POST",
        data: data,
        success: success,
        error: error || function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status == 400) {
                Roo.alertError('没有权限操作');
                return;
            }
            console.error("The following error occurred: " + textStatus, errorThrown);
        }
    });
};
