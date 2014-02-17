<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
 </head>
<body>
        <div class="mini-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
	                <td style="width:100%;">
		                <a class="mini-button" iconCls="icon-ok" onclick="pass()" plain="true">通过</a>
		                <a class="mini-button" iconCls="icon-no" onclick="reject()" plain="true">拒绝</a>
	                </td>
                    <td style="white-space:nowrap;">
                        <input id="user_phone" class="mini-textbox" emptyText="货主手机号" style="width:150px;" onenter="search"/>  
                        <input id="user_name" class="mini-textbox" emptyText="货主姓名" style="width:150px;" onenter="search"/>  
                        <!--<input id="beginDate" class="mini-datepicker" format="yyyy-MM-dd" emptyText="发布开始时间"/>
                        <input id="endDate" class="mini-datepicker" format="yyyy-MM-dd" emptyText="发布结束时间"/> 
                        <input id="cargo_type" class="mini-combobox" style="width:150px;" textField="name" valueField="id" emptyText="请选择货物类型" url="/admin/dict/getAllCargoType" showNullItem="true"/> 
                        <input id="car_type" class="mini-combobox" style="width:150px;" textField="name" valueField="id" emptyText="请选择车型" url="/admin/dict/getAllCarType"  showNullItem="true"/> -->
                        <a class="mini-button" onclick="search()" plain="true" iconCls="icon-search">查询</a>
                    </td>
                </tr>
            </table>           
        </div>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/order/query?status=0" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20" > 
        <div property="columns" field="rowid" name="checkid">
        	<div type="checkcolumn" ></div>
        	<div name="action" width="50" align="center" headerAlign="center"  renderer="onActionRenderer">操作</div>
        	<div field="id" allowSort="true" width="90" headerAlign="center">编号</div>          
            <div field="user_name" width="65" headerAlign="center">货主</div>            
            <div field="user_phone"  width="90" headerAlign="center">货主电话</div>
            <div field="cargo_user_name" width="85" headerAlign="center" >联系人</div>            
            <div field="cargo_user_phone"  width="90" headerAlign="center">联系电话</div>
            <div field="cargo_desc"   width="100" headerAlign="center">货物描述</div>     
            <div field="cargo_remark" headerAlign="center">补充说明</div>     
            <div field="start_province_name"  headerAlign="center" width="70">出发省</div>     
            <div field="start_city_name"  headerAlign="center" width="70">出发市</div>     
            <div field="start_district_name"  headerAlign="center" width="70">出发县</div>
             <div field="end_province_name"  headerAlign="center" width="70">目的省</div>     
            <div field="end_city_name"  headerAlign="center" width="70">目的市</div>     
            <div field="end_district_name"  headerAlign="center" width="70">目的县</div>
            <div field="price"  allowSort="true" headerAlign="center">价格</div>     
            <div field="cargo_number" headerAlign="center">货物数量</div>     
            <div field="create_time"  allowSort="true" headerAlign="center">发布时间</div>     
            <div field="cargo_photo1"  allowSort="true" headerAlign="center">货物照片</div>     
            <div field="cargo_type_name"  headerAlign="center">货物类型</div>     
            <div field="car_type_name"  headerAlign="center">车型</div>     
            <div field="car_length"  allowSort="true" headerAlign="center">车长</div>     
            <div field="user_bond"  allowSort="true" headerAlign="center">货主保证金</div>
            <div field="validate_time"  allowSort="true" headerAlign="center">订单有效期</div>
            <div field="loading_time"  allowSort="true" headerAlign="center">预期装货时间</div>
            <div field="disburden_type_name"  allowSort="true" headerAlign="center">上下车方式</div>
            <div field="driver_name"  headerAlign="center">中标司机</div>
            <div field="driver_phone"  headerAlign="center">司机手机</div>
            <div field="driver_bond" headerAlign="center">司机保证金</div>
            <div field="deal_time" headerAlign="center">成交时间 </div>
            
        </div>
		</div> 
	</div>

</body>
<script>
    mini.parse();
    var grid = mini.get("datagrid");
    grid.load();
    grid.frozenColumns(0, 0);
    
    grid.on("drawcell", function (e) {
        var record = e.record,
        column = e.column,
        field = e.field,
        value = e.value;
        //格式化状态
        if (field == "status") {
        	if(value == 0){
            	e.cellHtml = "已创建";
            }
            else if(value == 1){
            	e.cellHtml = "审核通过";
            }
            else if(value == 2){
            	e.cellHtml = "审核未通过";
            }
            else if(value == 3){
            	e.cellHtml = "已中标";
            }
            else if(value == 4){
            	e.cellHtml = "已取消";
            }
            else if(value == 4){
            	e.cellHtml = "已完成";
            }
            else{
            	e.cellHtml = "未知";
            }

        }
        if (field == "cargo_photo1") {
        	if(value){
            	e.cellHtml = '<a href="' + value + '" target="_blank"> <img src="' + value + '" width="50" height="50"></a>';
            }
            else{
            	e.cellHtml = "暂无";
            }

        }
        if (field == "cargo_number") {
        	var unit = "" ;
        	if(record.cargo_unit == 1){
        		unit = "车"
        	}
        	else if(record.cargo_unit == 2){
        		unit = "吨"
        	}
        	else if(record.cargo_unit == 3){
        		unit = "方"
        	}
        	e.cellHtml = value + "[" + unit + "]"

        }
    });
    
    function onActionRenderer(e) {
        var grid = e.sender;
        var record = e.record;
        var uid = record._uid;
        var rowIndex = e.rowIndex;

//        var s = ' <a   href="javascript:pass1(\'' + uid + '\')">通过</a>'
//       		    + ' <a   href="javascript:reject(\'' + uid + '\')">拒绝</a>'
//       		    + ' <a   href="javascript:edit(\'' + record.id + '\')">修改</a>' ;
        	var s = ' <a   href="javascript:edit(\'' + record.id + '\')">修改</a>' ;
        return s;
    }
    
    function search() {
        var user_phone = mini.get("user_phone").getValue();
        var user_name = mini.get("user_name").getValue();
//        var beginDate = mini.get("beginDate").getFormValue();
//        var endDate = mini.get("endDate").getFormValue();
//        var cargo_type = mini.get("cargo_type").getValue();
//        var car_type = mini.get("car_type").getValue();
//        grid.load({ user_phone: user_phone , user_name : user_name , beginDate : beginDate , endDate : endDate , cargo_type : cargo_type , car_type : car_type ,status:status});
        grid.load({user_phone: user_phone , user_name : user_name,status:status});
    }
    
    
   function pass() {  
	   var rows = grid.getSelecteds();//获取勾选的行
	   var row_ids = "";
	   var reg=/,$/;//表达式，去掉最后字符串最后一个逗号
            if (rows.length > 0) {
            	var length = rows.length;
            	 for(var i=0;i<rows.length;i++){
            		 row_ids = row_ids+rows[i].id+",";//把所有的id拼成一个ids字符串
            	 }
	    		 mini.confirm("确定审核通过？", "提醒",
	 		            function (action) {            
	 		                if (action == "ok") {
	 		                     grid.loading("处理中，请稍候......");
	 		                    $.ajax({
	 		                        url: "/admin/order/pass/" + row_ids.replace(reg,""),
	 		                        success: function (text) {
	 		                    		grid.reload();
	 		                        },
	 		                        error: function () {
	 		                        	mini.alert("审核失败，请检查后再试！");
	 		                        	grid.unmask();
	 		                        }
	 		                    });
	 		                }
	 		            }
	 		     );
            }
        }
        
    
    function reject() {
    	var rows = grid.getSelecteds();//获取勾选的行
 	   	var row_ids = "";
 	   	var reg=/,$/;//表达式，去掉最后字符串最后一个逗号
 	   if (rows.length > 0) {
 		   var length = rows.length;
       	 	for(var i=0;i<rows.length;i++){
       	 		row_ids = row_ids+rows[i].id+",";//把所有的id拼成一个ids字符串
       	 	}
	       	 mini.confirm("确定拒绝审核？", "提醒",
		        function (action) {            
		            if (action == "ok") {
		                 grid.loading("处理中，请稍候......");
		                $.ajax({
		                    url: "/admin/order/reject/" + row_ids.replace(reg,""),
		                    success: function (text) {
		                        grid.reload();
		                    },
		                    error: function () {
		                    	mini.alert("拒绝审核失败，请稍后再试");
		                    	grid.unmask();
		                    }
		                });
		            } 
		        }
		    );
 	   }
    }
    
        
     function edit(id) {
            mini.open({
                url: "/admin/order/editDesc/" + id,
                title: "修改补充说明", width: 500, height: 300,
                ondestroy: function (action) {
                    grid.reload();
                }
            });
        }
</script>
</html>
