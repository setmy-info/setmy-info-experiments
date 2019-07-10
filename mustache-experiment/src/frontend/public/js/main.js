var app = (function (app) {

    app.MAIN_ID = "main";
    app.model = {};
    app.mainElement = null;

    app.onLoad = function (event) {
        console.log("Event: ", event);
        this.init();
    };

    app.onHashChange = function (event) {
        var hash = window.location.hash.substring(1);
        var parsed = {
            hash: hash,
            parts: hash.split('/').filter(function (element) {
                return (!!element);
            }),
            event: event
        };
        for (var i = 0; i < parsed.parts.length; i++) {
            var part = parsed.parts[i];
            // TODO : automatic integer, float, boolean, string
        }
        if (app.hashHandler) {
            app.hashHandler(parsed);
            app.loadTemplate();
        }
    };

    app.init = function () {
        console.log("app.init");
        this.mainElement = document.getElementById(this.MAIN_ID);
        window.onhashchange = this.onHashChange;
        console.log(window.location);
    };

    app.loadTemplate = function () {
        $.get('template.html', function (template) {
            var rendered = Mustache.render(template, {name: "Imre"});
            document.getElementById(app.MAIN_ID).innerHTML = rendered;
        });
    };

    return app;

})(app || {}); 