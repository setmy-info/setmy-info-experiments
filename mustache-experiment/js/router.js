var app = (function (app) {

    app.router = {
        init: function () {
            console.log("app.router.init");
        }
    };

    app.router.init();

    return app;
})(app || {}); 