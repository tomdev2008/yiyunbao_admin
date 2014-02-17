<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
 </head>
<body>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/business/queryUserBusiness/${userId}" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="10" showEmptyText="true" emptyText="暂无"> 
        <div property="columns">
            <div field="business_type" width="100">
            	交易类型
            </div>            
            <div field="business_amount" width="100">
            	交易金额
            </div>
            <div field="before_balance" >
            	交易前余额
            </div>
            <div field="after_balance" >
            	交易后余额
            </div>
            <div field="create_time" >
            	交易时间
            </div>
            <div field="sysUserName" >
            	系统管理员
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
        if (field == "business_type") {
        	if(value == 0){
            	e.cellHtml = "充值";
            }
            else if(value == 1){
            	e.cellHtml = "提现";
            }
            else if(value == 2){
            	e.cellHtml = "违约扣除";
            }
            else if(value == 3){
            	e.cellHtml = "成功交易扣除";
            }
            else if(value == 4){
            	e.cellHtml = "验证身份扣除";
            }
            else if(value == 5){
            	e.cellHtml = "冻结";
            }
            else if(value == 6){
            	e.cellHtml = "保证金";
            }
            else{
            	e.cellHtml = "未知";
            }
        }
        
    });
</script>
</html>
