<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
 </head>
<body>
         <form id="form1" method="post">
        <input name="id" class="mini-hidden" />
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;">
                <tr>
                    <td style="width:70px;">原密码：</td>
                    <td style="width:150px;">    
                        <input name="oldPWD" class="mini-password" required="true"  emptyText="请输入原来的密码"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:70px;">新密码：</td>
                    <td style="width:150px;">    
                        <input name="newPWD" class="mini-password" required="true"  emptyText="请输入新密码"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:70px;">再输一次：</td>
                    <td style="width:150px;">    
                        <input name="newPWD2" class="mini-password" required="true"  emptyText="请再输入一次新密码"/>
                    </td>
                </tr>
            </table>
        </div>
        <div style="text-align:center;padding:10px;">               
            <a class="mini-button" onclick="onOk" style="width:60px;margin-right:20px;">确定</a>       
            <a class="mini-button" onclick="onCancel" style="width:60px;">取消</a>       
        </div>        
    </form>

</body>
<script>
    mini.parse();
    var form = new mini.Form("form1");

        function SaveData() {
            var o = form.getData();            

            form.validate();
            if (form.isValid() == false) return;

            var json = mini.encode([o]);
            $.ajax({
                url: "/admin/updatePWD",
                data: { data: json },
                cache: false,
                success: function (text) {
                	var o = mini.decode(text);
                	if(!o.success){
                		mini.alert(o.data);
                	}else{
                		alert("修改密码成功，请用新密码重新登录");
	                    parent.window.location="/admin/logout";
                	}
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert(jqXHR.responseText);
                    CloseWindow();
                }
            });
        }

        function GetData() {
            var o = form.getData();
            return o;
        }
        
        function CloseWindow(action) {            
            if (action == "close" && form.isChanged()) {
                if (confirm("数据被修改了，是否先保存？")) {
                    return false;
                }
            }
            if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
            else window.close();            
        }
        
        function onOk(e) {
            SaveData();
        }
        
        function onCancel(e) {
            CloseWindow("cancel");
        }

</script>
</html>
