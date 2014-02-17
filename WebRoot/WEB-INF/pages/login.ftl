
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>用户登录</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <style type="text/css">
    body
    {
        width:100%;height:100%;margin:0;overflow:hidden;
    }
    </style>
    <script src="/admin/js/boot.js"></script>
    
</head>
<body >   
<div id="loginWindow" class="mini-window" title="欢迎进入物流管理系统，请登录" style="width:350px;height:165px;" 
   showModal="true" showCloseButton="false"
    >

    <div id="loginForm" style="padding:15px;padding-top:10px;">
        <table >
            <tr>
                <td style="width:60px;"><label for="username$text">帐号：</label></td>
                <td>
                    <input id="account" name="account"  class="mini-textbox" required="true" style="width:150px;"/>
                </td>    
            </tr>
            <tr>
                <td style="width:60px;"><label for="pwd$text">密码：</label></td>
                <td>
                    <input id="pwd" name="pwd"  class="mini-password" requiredErrorText="密码不能为空" required="true" style="width:150px;" onenter="onLoginClick"/>
                </td>
            </tr>            
            <tr>
                <td></td>
                <td style="padding-top:5px;">
                    <a onclick="onLoginClick" class="mini-button" style="width:60px;">登录</a>
                    <a onclick="onResetClick" class="mini-button" style="width:60px;">重置</a>
                </td>
            </tr>
        </table>
    </div>

</div>


    

    
    <script type="text/javascript">
        mini.parse();

        var loginWindow = mini.get("loginWindow");
        loginWindow.show();

        window.onresize = function () {
        
            loginWindow.show();
        }

        function onLoginClick(e) {
            var form = new mini.Form("#loginWindow");

            form.validate();
            if (form.isValid() == false) return;
            var account = mini.get("account").getValue();
            var pwd = mini.get("pwd").getValue();
            var msg = mini.loading("正在登录，请稍候...", "登录中");
			$.ajax({
                url: "/admin/doLogin",
                data: { account:account , pwd:pwd },
                cache: false,
                success: function (text) {
                	mini.hideMessageBox(msg);
                	var o = mini.decode(text);
                	if(!o.success){
                		mini.alert(o.data);
                	}else{
	                    window.location="/admin/main";
                	}
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	mini.hideMessageBox(msg);
                    alert(jqXHR.responseText);
                    CloseWindow();
                }
            });
        }
        function onResetClick(e) {
            var form = new mini.Form("#loginWindow");
            form.clear();
        }

    </script>

</body>
</html>