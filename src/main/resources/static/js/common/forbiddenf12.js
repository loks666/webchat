//取消选中并且取消右键操作
document.body.onselectstart =
    document.body.oncontextmenu =
        function () {
            return false;
        };

if(window.location.href.indexOf('#debug')==-1){
    setInterval(function(){
        (function (a) {return (function (a) {return (Function('Function(arguments[0]+"' + a + '")()'))})(a)})('bugger')('de', 0, 0, (0, 0));
    }, 1000);
}

setInterval(function() {
    check()
}, 1000);
var check = function() {
    function doCheck(a) {
        if (("" + a / a)["length"] !== 1 || a % 20 === 0) {
            (function() {}
                ["constructor"]("debugger")())
        } else {
            (function() {}
                ["constructor"]("debugger")())
        }
        doCheck(++a)
    }
    try {
        doCheck(0)
    } catch (err) {}
};
check();

//禁止鼠标右击
document.oncontextmenu = function() {
    event.returnValue = false;
};
//禁用开发者工具F12
document.onkeydown = document.onkeyup = document.onkeypress = function(
    event
    ){
    let e = event || window.event || arguments.callee.caller.arguments[0];
    if (e && e.keyCode == 123) {
    e.returnValue = false;
    return false;
}
};
    let userAgent = navigator.userAgent;
    if (userAgent.indexOf("Firefox") > -1) {
    let checkStatus;
    let devtools = /./;
    devtools.toString = function() {
    checkStatus = "on";
};
    setInterval(function() {
    checkStatus = "off";
    console.log(devtools);
    console.log(checkStatus);
    console.clear();
    if (checkStatus === "on") {
    let target = "";
    try {
    window.open("about:blank", (target = "_self"));
} catch (err) {
    let a = document.createElement("button");
    a.onclick = function() {
    window.open("about:blank", (target = "_self"));
};
    a.click();
}
}
}, 200);
} else {
    //禁用控制台
    let ConsoleManager = {
    onOpen: function() {
    alert("Console is opened");
},
    onClose: function() {
    alert("Console is closed");
},
    init: function() {
    let self = this;
    let x = document.createElement("div");
    let isOpening = false,
    isOpened = false;
    Object.defineProperty(x, "id", {
    get: function() {
    if (!isOpening) {
    self.onOpen();
    isOpening = true;
}
    isOpened = true;
    return true;
}
});
    setInterval(function() {
    isOpened = false;
    console.info(x);
    console.clear();
    if (!isOpened && isOpening) {
    self.onClose();
    isOpening = false;
}
}, 200);
}
};
    ConsoleManager.onOpen = function() {
    //打开控制台，跳转
    let target = "";
    try {
    window.open("about:blank", (target = "_self"));
} catch (err) {
    let a = document.createElement("button");
    a.onclick = function() {
    window.open("about:blank", (target = "_self"));
};
    a.click();
}
};
    ConsoleManager.onClose = function() {
    alert("Console is closed!!!!!");
};
    ConsoleManager.init();
}