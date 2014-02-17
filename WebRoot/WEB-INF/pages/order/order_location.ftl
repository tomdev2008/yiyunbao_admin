<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
 </head>
<body>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/order/queryLocation/${orderId}" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="10" > 
        <div property="columns">
            <div field="location" width="300">
            	位置
            </div>            
            <div field="longitude">
            	经度
            </div>            
            <div field="latitude">
            	纬度
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
    
</script>
</html>
