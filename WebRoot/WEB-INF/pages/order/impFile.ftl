<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <script src="/admin/js/swfupload/swfupload.js" type="text/javascript"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
    <link href="/admin/js/miniui/themes/blue/skin.css" rel="stylesheet" type="text/css" />
 </head>
<body>
	<div id="div1" class="mini-toolbar" style="border-bottom:0;padding:30px;">
		   文件位置：<input id="fileupload1" class="mini-fileupload" name="Fdata" limitType="*.xls;*.xlsx" 
				    flashUrl="/admin/js/swfupload/swfupload.swf"
				    uploadUrl="/admin/order/fileUpload"
				    onuploadsuccess="onUploadSuccess" 
				    onuploaderror="onUploadError"/>
	        <a class="mini-button " plain="true" iconCls="icon-upload" onclick="startUpload()">上传</a>
	    <div class="description" style="">
	        <h3>友情提示</h3>
	        <p>
	        	请选择你要上传的Excl文件，同时请注意文件的格式...<br/>
	        	<label style="color:green;">支持.xls和.xlsx格式</label>
	        </p>
	    </div>
	</div>
</body>
</html>	
<script>
	mini.parse();
	var div = mini.get("div1");
	
	function startUpload() {
		mini.confirm("文件上传后，会同步更新数据，你确定这样做吗？", "提醒",
	            function (action) {            
	                if (action == "ok") {
	                	div.loading("处理中，请稍候......");
	            		var fileupload = mini.get("fileupload1");
	            		fileupload.startUpload();
	                } 
	            }
	        );
	}
	
	function onUploadSuccess(e) {
		mini.showMessageBox({
            showHeader: true,
            width: 250,
            buttons: ["确定"],
            message: "上传成功.",
            iconCls: "mini-messagebox-info",
        });
        this.setText("");
        div.unmask();
    }
	
    function onUploadError(e) {
    	div.unmask();
    	mini.alert("上传失败，请稍后再试.");
    }
</script>