var wsHost = document.domain;
// 如果你修改了application-dev.yml 中的端口号，这里的ws端口也要一块修改
var wsPort = 8101;

$("#add-friend-menu").on('click', function () {
    layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        shadeClose: true,
        skin: 'yourclass',
        content: '<div style=\"width: 500px; height: 300px; background-color: white; border-radius: 5px; padding: 10px 50px;\">' +
            '<p style="text-align: left; line-height: 50px; font-weight: 700; font-size: 16px; color: #424242">搜索添加好友 ' +
            '<span style="font-size: 13px; font-weight: 400; float: right; color: gray">手机号检索</span></p>' +
            '<input id="search-mobile" style="height: 45px; margin-top: 20px; border: 1px solid #c1d5f3; border-radius: 3px; width: 100%; text-indent: 10px;" placeholder="输入要检索的手机号, 回车搜索">' +
            '<div style="width: 100%; margin-top: 20px; font-size: 14px; font-weight: 400" id="search-result">' +
            '检索结果: '+
            '</div>' +
            '</div>'
    });


    // 监听搜索的回车事件
    $("#search-mobile").keydown(function(event) {
        if (event.keyCode == 13) {
            var searchMobile = $("#search-mobile").val();
            if (searchMobile == '' || searchMobile.toString().trim() == '') {
                layer.msg("输入空内容");
                return;
            }
            $("#search-result").empty();
            $.ajax({
                url:"/api/user/search/"+searchMobile,
                type:"get",
                success:function (data) {
                    data = eval(data);
                    if (data.success){
                        var user = data.data;
                        if(user == null) {
                            $("#search-result").append("未查询到账号(<span style='color: orangered'>"+searchMobile+"</span>)用户！")
                        } else {
                            $("#search-result").append("<div class=\"search-user-card\">\n" +
                                "        <img src=\""+user.photo+"?x-oss-process=image/resize,m_fill,w_60,h_60\" class=\"search-user-photo\">\n" +
                                "        <p class=\"search-username\">"+user.userName+"</p>\n" +
                                "        <p class=\"search-mobile\">"+user.mobile+"</p>\n" +
                                "        <button class=\"applyAddUser\" onclick='applyAddUserFriend(\""+user.userId+"\")'>申请加好友</button>\n" +
                                "    </div>")
                        }
                    } else {
                        layer.msg("服务异常");
                    }
                },error:function () {
                    layer.msg("网络异常");
                }
            })
        }
    })
})

function applyAddUserFriend(friendId) {
    $.ajax({
        url:"/api/friend/applyAdd/"+friendId,
        type:"post",
        success:function (data) {
            data = eval(data);
            if (data.success){
                layer.msg("申请已发出");
            } else {
                layer.msg(data.msg);
            }
        },error:function () {
            layer.msg("网络异常");
        }
    })
}

countNewMessage();
function countNewMessage() {
    $.ajax({
        url:"/api/friend/countUnHandleApply",
        type:"get",
        success:function (data) {
            data = eval(data);
            if (data.success){
                if (data.data > 0) {
                    $(".new-message-icon").show();
                }
            }
        }
    })
}


function loadUnHandleApplyList() {
    $(".apply-unhandle-list").empty();
    $.ajax({
        url:"/api/friend/listUnHandleApplyUsers",
        type:"get",
        success:function (data) {
            data = eval(data);
            if (data.success){
                var list = data.data;
                if (list.length == 0) {
                    $(".apply-unhandle-list").append("<p style='line-height: 400px; color: #666'>暂无申请</p>");
                    return;
                }
                $.each(list, function (index, apply) {
                    var applyItem = "<div class='apply-item' id='apply-item-"+apply.applyId+"'>" +
                        "<img class='applyUserPhoto' src=\""+apply.applyUser.photo+"\">" +
                        "<span class='applyUserName'>"+apply.applyUser.userName+"</span>" +
                        "<a onclick='applyPass(\""+apply.applyId+"\")' class='apply-pass'>同意</a>" +
                        "<a onclick='applyRefuse(\""+apply.applyId+"\")' class='apply-refuse'>拒绝</a>" +
                        "</div>";
                    $(".apply-unhandle-list").append(applyItem);
                })
            }
        }
    })
}

function loadFriendList(userName) {
    $(".friend-list").empty();
    $.ajax({
        url:"/api/friend/listFriendUsers?userName="+userName,
        type:"get",
        success:function (data) {
            data = eval(data);
            if (data.success){
                var list = data.data;
                if (list.length == 0) {
                    $(".friend-list").append("<p style='line-height: 400px; color: #666'>还没有任何好友</p>");
                    return;
                }
                $.each(list, function (index, apply) {
                    var applyItem = "<a href='/chat?chatid="+apply.applyUser.userId+"'><div class='apply-item' id='apply-item-"+apply.applyId+"'>" +
                        "<img class='applyUserPhoto' src=\""+apply.applyUser.photo+"\">" +
                        "<span class='friendUserName'>"+apply.applyUser.userName+"</span>" +
                        "</div></a>";
                    $(".friend-list").append(applyItem);
                })
            }
        }
    })
}

function applyPass(id) {
    $.ajax({
        url:"/api/friend/applyPass/"+id,
        type:"post",
        success:function (data) {
            data = eval(data);
            if (data.success){
                layer.msg("通过");
                $("#apply-item-"+id).fadeOut(500);
            } else {
                layer.msg("服务异常");
            }
        }
    })
}

function applyRefuse(id) {
    $.ajax({
        url:"/api/friend/applyRefuse/"+id,
        type:"post",
        success:function (data) {
            data = eval(data);
            if (data.success){
                layer.msg("已拒绝");
                $("#apply-item-"+id).fadeOut(500);
            } else {
                layer.msg("服务异常");
            }
        }
    })
}

var hideChatMainListContainer = function () {
    $(".chat-main-list-container").hide();
}
var initCharMenuIconStatus = function () {
    $(".chat-menu-icon").removeClass("selected-icon");
}

$(".message-icon").on('click', function () {
    hideChatMainListContainer();
    initCharMenuIconStatus();
    $(".apply-list-container").show();
    $(".message-icon").addClass("selected-icon");

    loadUnHandleApplyList();
})

$(".friend-list-icon").on('click', function () {
    hideChatMainListContainer();
    initCharMenuIconStatus();
    $(".friend-list-container").show();
    $(".friend-list-icon").addClass("selected-icon");

    loadFriendList('');
})

$(".chat-icon").on('click', function () {
    hideChatMainListContainer();
    initCharMenuIconStatus();
    $(".chat-user-list-container").show();
    $(".chat-icon").addClass("selected-icon");

    loadFriendList('');
})

$("#search-username").keydown(function(event) {
    if (event.keyCode == 13) {
        loadFriendList($("#search-username").val());
    }
})

function formatMsgTime(timespan){
    var dateTime = new Date(timespan);
    var year = dateTime.getFullYear();
    var month = dateTime.getMonth() + 1;
    var day = dateTime.getDate();
    var hour = dateTime.getHours();
    var minute = dateTime.getMinutes();
    var nowDate = new Date();
    var now_new = new Date().getTime()
    var milliseconds = 0;
    var timeSpanStr;
    milliseconds = now_new - timespan;
    if (milliseconds <= 1000 * 60 * 1) {
        timeSpanStr = '刚刚';
    }
    else if (1000 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60) {
        timeSpanStr = Math.round((milliseconds / (1000 * 60))) + '分钟前';
    }
    else if (1000 * 60 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24) {
        timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60)) + '小时前';
    }
    else if (1000 * 60 * 60 * 24 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24 * 15) {
        timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60 * 24)) + '天前';
    }
    else if (milliseconds > 1000 * 60 * 60 * 24 * 15 && year == nowDate.getFullYear()) {
        timeSpanStr = day + ' / ' + month + '月';
    } else {
        timeSpanStr = year + ' / ' + month + ' / ' + day;
    }
    return timeSpanStr;
};


function codeContentEnCode(str) {
    if (str == null || str == '') {return str;}
    str = str.replaceAll(/\r\n/g,"<br>")
    str = str.replaceAll(/\n/g,"<br>");
    str = str.replaceAll(/\'/g,"&#39;");
    str = str.replaceAll(/\"/g,"&quot;");
    str = str.replace(/&/g,"&amp;");
    str = str.replace(/</g,"&lt;");
    str = str.replace(/>/g,"&gt;");
    return str;
}

function messContentEnCode(str) {
    if (str == null || str == '') {return str;}
    str = str.replaceAll(/\r\n/g,"<br>")
    str = str.replaceAll(/\n/g,"<br>");
    str = str.replace(/&/g,"&amp;");
    str = str.replace(/</g,"&lt;");
    str = str.replace(/>/g,"&gt;");
    str = str.replaceAll("&lt;br&gt;","<br>")
    return str;
}