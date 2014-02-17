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
                    <td style="width:70px;">登录帐号：</td>
                    <td style="width:150px;">    
                        <input name="user_account" class="mini-textbox" required="true"  emptyText="请输入帐号"/>
                    </td>
                    <td style="width:70px;">密码：</td>
                    <td style="width:150px;">    
                        <input name="user_password" class="mini-textbox" required="true"  emptyText="请输入密码"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:70px;">真实姓名：</td>
                    <td style="width:150px;">    
                        <input name="user_name" class="mini-textbox" required="true"  emptyText="请输入真实姓名"/>
                    </td>
                    <td style="width:70px;">最大金额：</td>
                    <td style="width:150px;">    
                        <input name="max_money" class="mini-spinner" maxValue="10000000" increment="100" required="true" vtype="int" emptyText="每日最大充值金额"/>
                    </td>
                    
                </tr>
                <tr>
                    <td style="width:70px;">邮箱：</td>
                    <td style="width:150px;">    
                        <input name="email" class="mini-textbox"/>
                    </td>
                    <td style="width:70px;">手机：</td>
                    <td style="width:150px;">    
                        <input name="cell" class="mini-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:70px;">座机：</td>
                    <td style="width:150px;">    
                        <input name="tel" class="mini-textbox"/>
                    </td>
                    <td style="width:70px;">身份证：</td>
                    <td style="width:150px;">    
                        <input name="id_card" class="mini-textbox" />
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
                url: "/admin/sys/user/save",
                data: { data: json },
                cache: false,
                success: function (text) {
                	var o = mini.decode(text);
                	if(!o.success){
                		mini.alert(o.data);
                	}else{
	                    CloseWindow("save");
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
