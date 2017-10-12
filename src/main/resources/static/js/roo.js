var Roo = {};
Roo.alertBox = function (options) {
    new Noty({
        type: options.type || 'info',
        layout: options.layout || 'topRight',
        text: options.text || '提示',
        timeout: options.timeout || 2000
    }).show();
};

Roo.alertError = function (content) {
    Roo.alertBox({
        type: 'error',
        text: content
    });
};

Roo.alertOk = function (content) {
    Roo.alertBox({
        type: 'information',
        text: content
    });
};
