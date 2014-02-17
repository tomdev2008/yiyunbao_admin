<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
 </head>
<body>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/order/queryVie/${orderId}" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="10" > 
        <div property="columns">
            <div field="driver_name" allowSort="true">
            	司机
            </div>            
            <div field="phone" >
            	司机电话
            </div>            
            <div field="driver_proportion">
            	保证金比例(%)
            </div>     
            <div field="driver_bond"  allowSort="true">
            	保证金
            </div>     
            <div field="create_time"  >
            	抢单时间
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
        //格式化状态
        if (field == "status") {
        	if(value == 0){
            	e.cellHtml = "已抢";
            }
            else if(value == 1){
            	e.cellHtml = "<font color='green'>中标</font>";
            }
            else if(value == 2){
            	e.cellHtml = "<font color='red'>退出</font>";
            }
            else{
            	e.cellHtml = "未知";
            }

        }
    });
    
</script>
</html>
