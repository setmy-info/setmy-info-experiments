
var templateConfig = {
    mainComponent: "templates/main.html",
    secondComponent: "templates/second.html",
    repeatingComponent: "templates/repeating.html"
};

//<editor-fold defaultstate="collapsed" desc="Template loading">

Element.prototype.isComponent = function () {
    return (this.dataset && this.dataset.smiComponent);
};

Element.prototype.findComponents = function () {
    var childrens = this.childNodes;
    var child;
    var i = 0;
    for (; i < childrens.length; i++) {
        child = childrens[i];
        if (child.findComponents) {
            child.findComponents();
        }
        if (child.isComponent && child.isComponent()) {
            console.log("Found component: ", child.dataset.smiComponent);
            app.templateCache.loadTemplate(child.dataset.smiComponent, function (text) {
                console.log("text: ", text);
                var rendered = Mustache.render(text, {label: "Sub component"});
                console.log("rendered: ", rendered);
                child.innerHTML = rendered;
            });
        }
    }
};

var loaderResource = (function (loaderResource) {

    loaderResource.loadTemplate = function (templateFileName, callback) {
        var request = new XMLHttpRequest();
        request.open('GET', templateFileName, true);
        request.onload = function () {
            if (request.status >= 200 && request.status < 400) {
                var text = request.responseText;
                callback(text);
            }
        };
        request.send();
    };

    return loaderResource;

})(loaderResource || {});

var templateCache = (function (templateCache) {

    templateCache.loaderResource = loaderResource;
    templateCache.templateConfig = templateConfig;
    templateCache.loaded = {};

    templateCache.loadTemplate = function (name, callback) {
        var text = this.loaded[name];
        if (text) {
            callback(text);
            return;
        }
        var that = this, templateFileName = this.templateConfig[name];
        this.loaderResource.loadTemplate(templateFileName, function (text) {
            that.loaded[name] = text;
            callback(that.loaded[name]);
        });
    };

    return templateCache;

})(templateCache || {});

//</editor-fold>

var app = (function (app) {

    app.templateCache = templateCache;
    app.MAIN_ID = "main";
    app.COMPONENT_PROPERTY_NAME = "smiComponent";
    app.model = {};
    app.mainElement = null;

    app.onLoad = function (event) {
        console.log("Event: ", event);
        this.init();
    };

    app.init = function () {
        console.log("app.init");
        this.mainElement = document.getElementById(this.MAIN_ID);
        this.templateCache.loadTemplate('mainComponent', function (text) {
            var rendered = Mustache.render(text, {name: "Imre"});
            var element = document.getElementById(app.MAIN_ID);
            element.innerHTML = rendered;
            element.findComponents();
            /*element = document.getElementById('helloWorld');
             if (element) {
             console.log("Hello World element found");
             }*/
        });
    };

    return app;
})(app || {});


//<editor-fold defaultstate="collapsed" desc="comment">
/*
 window.onhashchange = this.onHashChange;
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
 app.loadTemplate('templates/main.html');
 }
 };
 function doggle(id) {
 console.log('Doggle!');
 var element = document.getElementById(id);
 element.doggleVisibility();
 }
 
 Element.prototype.doggleVisibility = function () {
 if (typeof (this.previousDisplay) !== 'undefined' && this.previousDisplay.length > 0) {
 console.log("TO: ", this.previousDisplay);
 this.style.display = this.previousDisplay;
 this.previousDisplay = "";
 } else {
 this.previousDisplay = this.style.display;
 console.log("TO none, from: '" + this.style.display + "'");
 this.style.display = 'none';
 }
 };
 */
//</editor-fold>
