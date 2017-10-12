var roo = {};
roo.alertBox = function (content) {
    new Noty({
        type: 'info',
        layout: 'topRight',
        text: content,
        timeout: 2000
    }).show();
};

$('.modal-close,.modal-background').click(function () {
    $('#roo-modal').removeClass('is-active');
});

