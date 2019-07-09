var app = (function (app) {

    app.MAIN_ID = "main";
    app.model = {};
    app.mainElement = null;

    app.onLoad = function (event) {
        console.log("Event: ", event);
        this.init();
    };

    app.init = function () {
        console.log("app.init");
        this.mainElement = document.getElementById(this.MAIN_ID);
        console.log(window.location);
    };
    return app;
})(app || {}); 