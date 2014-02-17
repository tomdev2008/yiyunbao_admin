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
                    </td>
                    <td style="white-space:nowrap;">
                        <input id="start_point" class="mini-buttonedit" onbuttonclick="onAreaSelect" emptyText="出发地"/>    
                        <input id="end_point" class="mini-buttonedit" onbuttonclick="onAreaSelect" emptyText="目的地"/>  
                        <input id="user_phone" class="mini-textbox" emptyText="货主手机号" style="width:150px;" onenter="search"/>  
                        <input id="user_name" class="mini-textbox" emptyText="货主姓名" style="width:150px;" onenter="search"/>  
                        <input id="beginDate" class="mini-datepicker" format="yyyy-MM-dd" emptyText="发布开始时间"/>
                        <input id="endDate" class="mini-datepicker" format="yyyy-MM-dd" emptyText="发布结束时间"/> 
                    </td>
                </tr>
                <tr>
                    <td style="width:100%;">
                    </td>
                    <td style="white-space:nowrap;">
                        <input id="cargo_type" class="mini-combobox" style="width:150px;" textField="name" valueField="id" emptyText="请选择货物类型" url="/admin/dict/getAllCargoType" showNullItem="true"/> 
                        <input id="car_type" class="mini-combobox" style="width:150px;" textField="name" valueField="id" emptyText="请选择车型" url="/admin/dict/getAllCarType"  showNullItem="true"/> 
                        <a class="mini-button" onclick="search()" plain="true" iconCls="icon-search">查询</a>
                    </td>
                </tr>
            </table>           
        </div>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/order/query?status=99" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20" > 
        <div property="columns">
        	<div name="action" width="250" align="center" headerAlign="center"  renderer="onActionRenderer">操作</div>
            <div field="id" allowSort="true">
            	编号
            </div>            
            <div field="user_name" >
            	货主
            </div>            
            <div field="user_phone"  >
            	货主电话
            </div>
            <div field="price"  allowSort="true">
            	价格
            </div>     
            <div field="create_time"  allowSort="true">
            	发布时间
            </div>     
            <div field="cargo_desc"  >
            	货物描述
            </div>     
            <div field="cargo_remark" >
            	备注
            </div>     
            <div field="cargo_type_name"  >
            	货物类型
            </div>     
            <div field="car_type_name"  >
            	车型
            </div>     
            <div field="car_length"  allowSort="true">
            	车长
            </div>     
            <div field="start_province_name"  >
            	出发地【省】
            </div>     
            <div field="start_city_name"  >
            	出发地【市】
            </div>     
            <div field="start_district_name"  >
            	出发地【区县】
            </div>
             <div field="end_province_name"  >
            	目的地【省】
            </div>     
            <div field="end_city_name"  >
            	目的地【市】
            </div>     
            <div field="end_district_name"  >
            	目的地【区县】
            </div>
            <div field="user_proportion"  allowSort="true">
            	货主保证金比例(%)
            </div>
            <div field="user_bond"  allowSort="true">
            	货主保证金
            </div>
            <div field="validate_time"  allowSort="true">
            	订单有效期
            </div>
            <div field="loading_time"  allowSort="true">
            	预期装货时间
            </div>
            <div field="disburden_type_name"  allowSort="true">
            	上下车方式
            </div>
            <div field="driver_name"  >
            	中标司机
            </div>
            <div field="driver_phone"  >
            	司机手机
            </div>
            <div field="driver_proportion" >
            	司机保证金比例
            </div>
            <div field="driver_bond" >
            	司机保证金
            </div>
            <div field="deal_time" >
            	成交时间
            </div>
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
    });
    
    function onActionRenderer(e) {
        var grid = e.sender;
        var record = e.record;
        var uid = record._uid;
        var rowIndex = e.rowIndex;

        var s = ' <a   href="javascript:showVie(\'' + uid + '\')">抢单</a>'
        		+ ' <a   href="javascript:showStatus(\'' + uid + '\')">状态</a>' 
        		+ ' <a   href="javascript:showIM(\'' + uid + '\')">聊天</a>' 
        		+ ' <a   href="javascript:showLocation(\'' + uid + '\')">定位</a>' 
        		+ ' <a   href="javascript:showUserReply(\'' + uid + '\')">货主评价</a>' 
        		+ ' <a   href="javascript:showDriverReply(\'' + uid + '\')">司机评价</a>' ;
        return s;
    }
    
    function search() {
    	var start_point = mini.get("start_point").getValue();
    	var end_point = mini.get("end_point").getValue();
        var user_phone = mini.get("user_phone").getValue();
        var user_name = mini.get("user_name").getValue();
        var beginDate = mini.get("beginDate").getFormValue();
        var endDate = mini.get("endDate").getFormValue();
        var cargo_type = mini.get("cargo_type").getValue();
        var car_type = mini.get("car_type").getValue();
        grid.load({ start_point:start_point,end_point:end_point ,user_phone: user_phone , user_name : user_name , beginDate : beginDate , endDate : endDate , cargo_type : cargo_type , car_type : car_type });
    }
        
    function delRow(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
            	 mini.confirm("确定删除？", "确定？",
		            function (action) {            
		                if (action == "ok") {
		                     grid.loading("处理中，请稍候......");
		                    $.ajax({
		                        url: "/admin/driver/delete/" + row.id,
		                        success: function (text) {
		                            grid.reload();
		                        },
		                        error: function () {
		                        	mini.alert("删除失败，请稍后再试");
		                        	grid.unmask();
		                        }
		                    });
		                } 
		            }
		        );
		        
            }
        }
        
    function showVie(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
		         mini.open({
	                url: "/admin/order/vieList/" + row.id,
	                title: "查看抢单情况", 
	                width: 800, 
	                height: 360
	            });
            }
        }   
    
    function showIM(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
		         mini.open({
	                url: "/admin/im/" + row.id,
	                title: "查看聊天记录", 
	                width: 800, 
	                height: 360
	            });
            }
        }        
    //状态 
    function showStatus(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
		         mini.open({
	                url: "/admin/order/statusList/" + row.id,
	                title: "查看货物历史状态", 
	                width: 300, 
	                height: 360
	            });
            }
        }
                 
    //定位 
    function showLocation(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
		         mini.open({
	                url: "/admin/order/locationList/" + row.id,
	                title: "查看司机位置", 
	                width: 800, 
	                height: 360
	            });
            }
        }      
        
     //货主评价
    function showUserReply(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
		         mini.open({
	                url: "/admin/userReply/orderReply/" + row.id,
	                title: "查看货主评价", 
	                width: 800, 
	                height: 360
	            });
            }
        }   
     
    //司机评价
    function showDriverReply(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
		         mini.open({
	                url: "/admin/driverReply/orderReply/" + row.id,
	                title: "查看司机评价", 
	                width: 800, 
	                height: 360
	            });
            }
        }   
                
     function onAreaSelect(e) {
            var btnEdit = this;
            mini.open({
                url: "/admin/dict/areaTree",
                showMaxButton: false,
                title: "选择树",
                width: 350,
                height: 350,
                ondestroy: function (action) {                    
                    if (action == "ok") {
                        var iframe = this.getIFrameEl();
                        var data = iframe.contentWindow.GetData();
                        data = mini.clone(data);
                        if (data) {
                            btnEdit.setValue(data.id);
                            btnEdit.setText(data.name);
                        }
                    }
                }
            });            
             
        }        
</script>
</html>
