<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
	    <script src="/admin/js/boot.js"></script>
	    <script src="/admin/js/swfupload/swfupload.js" type="text/javascript"></script>
	    <style type="text/css">
	    	html, body{
			  margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
			} 
	    </style>
	    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	    <title>批量导入司机</title>
	</head>
	<body>
	    <div id="batchDriver_win" 
	    	class="mini-window" 
	    	title="批量导入司机" 
	    	style="width:300px;height:120px;" 
		    showCloseButton="false" 
		    allowResize="false" allowDrag="false"
		    showFooter="true"
		    >
		    <div style="margin-top:15px;">
			    <label>请选择文件：</label>
				<input id="importDriver" class="mini-fileupload"
				name="file" limitType="*.xls" 
				flashUrl="/admin/js/swfupload/swfupload.swf"
				uploadUrl="/admin/driver/uploadDriverFile" 
				onuploadsuccess="uploadsuccess" 
				onuploaderror="uploaderror"
				 />
			</div>
			<div property="footer" style="text-align:center;padding:5px;padding-right:15px;">
		       <div class="mini-button" plain="false"  onclick="fileUpload" >上传</div>
		    </div>
		</div>
	    <script type="text/javascript">
  			mini.parse();
  			mini.get("batchDriver_win").show();
  			
  			function fileUpload(){
  				var upload = mini.get("importDriver");
  				if(upload.getText() == ""){
  					mini.alert("请选择上传到的文件！");
  					return;
  				}
  				upload.startUpload();
  			}
  			function uploadsuccess(){
  				var load = mini.loading("文件上传成功！正在导入数据..", "导入中");
  				$.post("/admin/driver/batchImportDriver",function(text){
  					mini.hideMessageBox(load);
  					mini.alert(text.data);
  				});
  				mini.get("importDriver").setText("");
  			}
  			
  		</script>
	</body>
</html>