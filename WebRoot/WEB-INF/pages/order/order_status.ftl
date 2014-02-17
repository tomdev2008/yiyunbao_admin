<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
 </head>
<body>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/order/queryStatus/${orderId}" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="10" > 
        <div property="columns">
            <div field="status">
            	货物状态
            </div>            
            <div field="create_time" >
            	时间
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
        //格式化状态
        if (field == "status") {
        	if(value == 1){
            	e.cellHtml = "到厂";
            }
            else if(value == 2){
            	e.cellHtml = "启程";
            }
            else if(value == 3){
            	e.cellHtml = "在途";
            }
            else if(value == 4){
            	e.cellHtml = "到达";
            }
            else if(value == 5){
            	e.cellHtml = "卸货";
            }
            else{
            	e.cellHtml = "未知";
            }

        }
    });
    
</script>
</html>
