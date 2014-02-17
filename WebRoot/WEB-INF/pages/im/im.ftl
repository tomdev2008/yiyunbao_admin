<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
 </head>
<body>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/im/queryIm/${orderId}" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="10" > 
        <div property="columns">
            <div field="sender" >
            	发送方
            </div>            
            <div field="message_type" >
            	消息类型
            </div>            
            <div field="message_content">
            	内容
            </div>     
            <div field="send_time" >
            	发送时间
            </div>     
            <div field="status"  >
            	状态
            </div>     
        </div>
		</div> 
	</div>

</body>
<script>
    mini.parse();
    var grid = mini.get("datagrid");
    grid.load();
    
    grid.on("drawcell", function (e) {
        var record = e.record,
        column = e.column,
        field = e.field,
        value = e.value;
         //格式化发送方
        if (field == "sender") {
        	if(value == 1){
            	e.cellHtml = "货主";
            }
            else if(value == 2){
            	e.cellHtml = "司机";
            }
            else{
            	e.cellHtml = "其他";
            }
        }
        //格式化消息类型
        if (field == "message_type") {
        	if(value == 1){
            	e.cellHtml = "文本";
            }
            else if(value == 2){
            	e.cellHtml = "图片";
            }
            else if(value == 3){
            	e.cellHtml = "音频";
            }
            else if(value == 4){
            	e.cellHtml = "司机操作";
            }
            else{
            	e.cellHtml = "其他";
            }
        }
        //格式化状态
        if (field == "status") {
        	if(value == 0){
            	e.cellHtml = "已发送";
            }
            else if(value == 1){
            	e.cellHtml = "<font color='green'>已接收</font>";
            }
            else{
            	e.cellHtml = "未知";
            }
        }
    });
    
</script>
</html>
