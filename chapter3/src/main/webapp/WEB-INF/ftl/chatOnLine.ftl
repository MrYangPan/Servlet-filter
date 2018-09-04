<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style type="text/css">
        body {
            vertical-align: top;
        }

        #user-list {
            float: left;
            width: 150px;
            height: 200px;
            overflow: auto;
            padding: 10px;
            border: 1px #ccc solid;
            margin: 0 10px 50px 0;
        }

        #user-list ul {
            padding-left: 0px;
            margin: 0;
            list-style-type: none;
        }

        #msg-box {
            width: 500px;
            height: 200px;
            overflow: auto;
            border: 1px #ccc solid;
            margin-bottom: 50px;
            padding: 10px;
        }

        .msg {
            padding: 2px 0;
        }
    </style>
</head>
<script src="../../static/jquery.1.10.2.js"></script>
<body>
<div>
    <div style="float: left;">在线用户列表</div>
    <div style="float: left;width: 85px;height: 1px;"></div>
    <div>消息列表</div>
    <div id="user-list">
        <ul>
        <#list users as item>
            <li id="user-${item}">${item}</li>
        </#list>
        </ul>
    </div>
    <div id="msg-box"></div>
    我自己：<span id="username">${username}</span><br/>
    发送给：<input type='text' id="receiver">(不填代表所有人)<br/>
    消&nbsp;&nbsp;息：<textarea id="msg" cols="20" rows="5"></textarea><br/>
    <input id="send-btn" type="button" value="发送"/>
</div>

<script type="text/javascript">
    $(function () {
        var query = {};
        query.msgBox = $("#msg-box");
        query.url = '${url}';
        query.usrename = '${username}';
        query.init = function () {
            var url = query.url + "?command=poll&username=" + query.usrename;
            query.longPolling(url, query.callback);
            $("#send-btn").click(query.send);
        };
        query.login = function (username) {
            var userLi = $("#user-" + username);
            if (!userLi.length) {
                userLi = $("<li></li>").attr("id", "user-" + username).text(username);
                $("#user-list > ul").append(userLi);
                query.publish(null, username + "上线了");
            }
        };
        query.logout = function (username) {
            var userLi = $("#user-" + username);
            if (userLi.length) {
                userLi.remove();
                query.publish(null, username + "下线了");
            }
        };
        query.publish = function (username, msg) {
            if (username) {
                query.login(username);
            }
            var msgList = query.msgBox.find(".msg");
            if (msgList.length >= 50) {//如果消息超过50个，删除老的
                for (var i = msgList.length - 50 - 1; i >= 0; i--) {
                    $(msgList.get(i)).remove();
                }
            }
            query.msgBox.append($("<div></div>").addClass("msg").html(msg));
            query.msgBox.animate({scrollTop: query.msgBox.scrollTop() + query.msgBox.height()}, 300);
        };
        query.callback = function (data) {
            if (!data) {
                return;
            }
            data = $.parseJSON(data);
            switch (data.type) {
                case "login" :
                    query.login(data.username);
                    break;
                case "logout" :
                    query.logout(data.username);
                    break;
                case "msg" :
                    query.publish(data.username, data.username + ":" + data.msg);
                    break;
                default :
                    //ignore
            }
        };
        query.longPolling = function (url, callback) {
            $.ajax({
                url: url,
                async: true,
                cache: false,
                global: false,
                timeout: 30 * 1000,
                success: function (data, status, request) {
                    callback(request.responseText);
                    data = null;
                    status = null;
                    request = null;
                    setTimeout(
                            function () {
                                query.longPolling(url, callback);
                            },
                            10
                    );
                },
                error: function (xmlHR, textStatus, errorThrown) {
                    xmlHR = null;
                    textStatus = null;
                    errorThrown = null;
                    setTimeout(
                            function () {
                                query.longPolling(url, callback);
                            },
                            5 * 1000
                    );
                }
            });
        };
        query.send = function () {
            var receiver = $("#receiver").val();
            var msg = $("#msg").val();
            query.publish(null, "我:" + msg);
            $.get(query.url + "?command=send&receiver=" + receiver + "&msg=" + msg + "&username=" + query.usrename);
        };
        query.init();
    });
</script>
</body>
</html>