/**
 * 隐藏鉴权盒子
 */
var hideAuthContainer = function () {
    $("#login-container").hide();
    $("#registry-container").hide();
}
/**
 * 去注册
 */
$(".registry").on('click', function () {
    hideAuthContainer();
    $("#registry-container").fadeIn(200);
})
/**
 * 去登录
 */
$(".login").on('click', function () {
    gotoLogin();
})

var gotoLogin = function () {
    hideAuthContainer();
    $("#login-container").fadeIn(200);
}

/**
 * 切换验证码
 */
$("#pic-valid-code").on('click', function () {
    $(this).attr('src', "/api/valid-code/code?token="+Math.random());
})


function invokeLoginServer() {
    var mobile = $("#login-mobile").val();
    var password = $("#login-pwd").val();
    if (mobile == '') {
        layer.msg("手机号不能为空");
        return;
    }
    if (password == '') {
        layer.msg("密码不能为空");
        return;
    }
    $.ajax({
        url: "/api/user/login",
        type: "post",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data: "{\"mobile\":\"" + mobile + "\", \"password\": \"" + password + "\"}",
        success: function (data) {
            data = eval(data);
            if (data.success) {
                layer.closeAll();
                // 登录成功跳转聊天室
                location.href = "/chat";
            } else {
                layer.msg(data.msg);
            }
        }, error: function () {
            layer.msg("服务端异常");
        }
    })
}

/**
 * 注册
 * @param userName
 * @param number
 * @param password
 * @param picCode
 */
function invokeRegistryServer() {
    var userName = $("#registry-username").val();
    var number = $("#registry-mobile").val();
    var password = $("#registry-pwd").val();
    var picCode = $("#registry-pic-code").val();
    if (userName == '' || userName.length < 3 || userName.length > 12) {
        layer.msg("用户名非法：长度3 ~ 12位字符");
        return;
    }
    if (number == '') {
        layer.msg("账号不能为空");
        return;
    }
    var reg = /^[0-9a-zA-Z_]{1,}$/;
    if (!reg.test(number)) {
        layer.msg("账号格式不合法！");
        return;
    }
    if (picCode == '') {
        layer.msg("数字验证码不能为空");
        return;
    }
    if (password == '') {
        layer.msg("密码不能为空");
        return;
    }
    $.ajax({
        url: "/api/user/registry",
        type: "post",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data: "{\"mobile\":\""+number+"\", \"password\": \""+password+"\", \"userName\": \""+userName+"\", \"picCheckCode\": \""+picCode+"\"}",
        success:function (data) {
            data = eval(data);
            if (data.success){
                // 刷新用户信息
                layer.closeAll();
                layer.msg("注册成功！");
                gotoLogin();
            } else {
                layer.msg(data.msg);
            }
        },error:function () {
            layer.msg("服务端异常");
        }
    })
}

/**
 * 退出登录
 */
function loginOut() {
    $.ajax({
        url:"/api/user/logout",
        type:"get",
        success:function () {
            location.reload();
        },error:function () {}
    })
}