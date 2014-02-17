<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
 </head>
<body>
         <form id="form1" method="post" enctype="multipart/form-data" action="/admin/ad/save" >
        	<table style="table-layout:fixed;">
                <tr>
                    <td style="width:120px;">广告位置：</td>
                    <td style="width:200px;">   
                    	<select name="ad_location" style="width:300px;">
                    		<option value="1">首页</option>
                    		<option value="2">个人主页顶部</option>
                    	</select> 
                    </td>
                </tr>
                <tr>
                    <td style="width:120px;">上传图片：</td>
                    <td style="width:200px;">    
                         <input id="ad_img" name="ad_img" class="mini-htmlfile" style="width:300px;" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:120px;">广告标题：</td>
                    <td style="width:200px;">    
                        <input name="ad_title" class="mini-textbox" style="width:300px;"	 />
                    </td>
                </tr>
                <tr>
                    <td style="width:120px;">链接地址：</td>
                    <td style="width:200px;">    
                        <input name="ad_link" class="mini-textbox" style="width:300px;"	 />
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

            $("#form1").submit();
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
