<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
 </head>
<body>
         <form id="form1" method="post">
        	<table style="table-layout:fixed;">
                <tr>
                    <td style="width:100px;"><font color="red">*</font>标题：</td>
                    <td style="width:200px;">    
                        <input name="msg_title" class="mini-textbox" required="true" style="width:400px;"	 />
                    </td>
                </tr>
                <tr >
               		 <td ><font color="red">*</font>內容：</td>
                	 <td >      
                	    <textarea name="msg_content"  class="mini-textarea" required="true" style="width:400px;height:150px;"></textarea>
                	 </td>
                </tr>     
            </table>
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
                url: "/admin/msg/save",
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
