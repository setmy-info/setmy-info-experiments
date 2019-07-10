var app = (function (app) {

    app.router = {
        init: function () {
            console.log("app.router.init");
            app.hashHandler = this.hashHandler;
        },
        hashHandler: function (hash) {
            console.log("Hash change event: ", hash);
            // TODO : by config find callback function to call
        }
    };

    app.router.init();

    return app;
})(app || {}); 