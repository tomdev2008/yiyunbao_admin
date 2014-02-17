<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
 </head>
<body>
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;">
                <tr>
                    <td style="width:70px;">补充说明：</td>
                    <td style="width:150px;">   
                    	<textarea id="cargo_remark" name="cargo_desc" cols="50" rows="10">${order.cargo_remark!""}</textarea> 
                    </td>
                </tr>
            </table>
        </div>
        <div style="text-align:center;padding:10px;">               
            <a class="mini-button" onclick="onOk" style="width:60px;margin-right:20px;">确定</a>       
            <a class="mini-button" onclick="onCancel" style="width:60px;">取消</a>       
        </div>        
</body>
<script>
    mini.parse();

        function SaveData() {
        	var cargo_remark = $("#cargo_remark").val();
            $.ajax({
                url: "/admin/order/updateDesc",
                data: { cargo_remark: cargo_remark , order_id:${order.id} },
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
