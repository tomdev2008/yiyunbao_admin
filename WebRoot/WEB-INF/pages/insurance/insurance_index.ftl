<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
    <script>
    	var couponStatus = [
	    { "id": "0", "text": "未处理" },
	    { "id": "1", "text": "已处理" }
		]
    </script>
 </head>
<body>
         <div class="mini-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                    	<a class="mini-button" iconCls="icon-add" onclick="exportExcel()">全部导出excel</a>
                    </td>
                     <td style="white-space:nowrap;">
                     	<input id="status" class="mini-combobox" style="width:150px;" textField="text" valueField="id" emptyText="请选择状态" data="couponStatus"  showNullItem="true"/>
                        <a class="mini-button" onclick="search()" plain="true" iconCls="icon-search">查询</a>
                    </td>
                </tr>
            </table>
        </div>
    <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/insurance/list" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20"> 
            <div property="columns">
                <div type="indexcolumn" ></div>
            	<div name="action"  headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">操作</div> 
                <div field="status" allowSort="false" align="center" headerAlign="center">
                	状态
                </div>
                <div field="shiping_number" allowSort="false" align="center" headerAlign="center">
                	运单号
                </div>
                <div field="insurer_name" >
                	投保人
                </div>
                <div field="insurer_phone" >
                    投保人联系电话
                </div>
                <div field="insured_name" >
                    被保险人
                </div>
                <div field="insured_phone" >
                    被保险人联系电话
                </div>
                 <div field="start_area">
                    起运地
                </div>
                 <div field="end_area">
                    目的地
                </div>
                <div field="cargo_desc" >
                    货物名称
                </div>
                <div field="ship_type" >
                    运输方式
                </div>
                <div field="plate_number">
                	车牌号
                </div>      
                <div field="insurance_type">
                	险种
                </div>
                <div field="packet_number" >
                	件数/重量
                </div>
                <div field="sign_time" >
                    签单日期
                </div>
                <div field="start_date" >
                    起运日期
                </div>
                <div field="amount_covered" >
                    保险金额
                </div>
                <div field="ratio" >
                    保险费率
                </div>
                <div field="insurance_charge" >
                    保险费
                </div>
                <div field="package_name" >
                    包装类型
                </div>
                <div field="cargo_name1" >
                    货物类型(大类)
                </div>
                <div field="cargo_name2" >
                    货物类型(小类)
                </div>
                <div field="receipt_title" >
                    发票抬头
                </div>
            </div>
		</div>
    </div>
    <form id="excelForm"  action="/admin/insurance/exportExcel" method="post" target="excelIFrame">
    </form>
    <iframe id="excelIFrame" name="excelIFrame" style="display:none;"></iframe>
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
            	e.cellHtml = "未处理";
            }
            else if(value == 1){
            	e.cellHtml = "<font color='green'>已处理</font>";
            }
        }             
        if (field == "insurance_type"){
        	if(value == 1){
        		e.cellHtml = "基本险";
        	}else if(value == 2){
        		e.cellHtml = "综合险";
        	}else if(value == 3){
        		e.cellHtml = "综合险附加被盗险";
        	}else{
        		e.cellHtml = "未知:" + value;
        	}
        }
    });
    
     function onActionRenderer(e) {
        var grid = e.sender;
        var record = e.record;
        var uid = record._uid;
        var rowIndex = e.rowIndex;
		
        var s = '';
        if(record.status == '0'){
        	s += ' <a href="javascript:deal(\'' + uid + '\')">标记为已处理</a>';
        }
        return s;
    }
    
    function onRenderTrace(e){
        var grid = e.sender;
        var record = e.record;
        var uid = record._uid;
        var rowIndex = e.rowIndex;
        var start_province_name = record.start_province_str || "";
        var start_city_name = record.start_city_str || "";
        var start_district_name = record.start_district_str || "";
        var end_province_name = record.end_province_str || "";
        var end_city_name = record.end_city_str || "";
        var end_district_name = record.end_district_str || "";
        var s = start_province_name + start_city_name  + start_district_name  + "-" + end_province_name + end_city_name + end_district_name;
        return s;
    }
    
     function exportExcel() {
        // var columns = grid.getBottomColumns();
        // function getColumns(columns) {
        //     columns = columns.clone();
        //     for (var i = columns.length - 1; i >= 0; i--) {
        //         var column = columns[i];
        //         if (!column.field) {
        //             columns.removeAt(i);
        //         } else {
        //             var c = { header: column.header, field: column.field };
        //             columns[i] = c;
        //         }
        //     }
        //     return columns;
        // }
        
        // var columns = getColumns(columns);
        // var form = $("#excelForm");
        // form.empty();
        // for(var i = 0; i < columns.length; i++){
        //     form.append('<input type="hidden" name="id" value=""/>');
        // }
        var excelForm = document.getElementById("excelForm");
        excelForm.submit();
     }
    function search() {
        var status = mini.get("status").getValue();
        grid.load({status:status});
    }
    
     
    //显示详情
    function viewInsure(row_uid) {
        var row = grid.getRowByUID(row_uid);
        if (row) {
            mini.open({
                url: "/admin/insurance/viewInsure/" +  + row.id,
                title: "保单详情", 
                width: 850, 
                height: 650
            });
        }
    } 
    
    
    function deal(row_uid) {
             var row = grid.getRowByUID(row_uid);
            if (row) {
            	 mini.confirm("确定标记为已处理吗？", "确定？",
		            function (action) {            
		                if (action == "ok") {
		                     grid.loading("处理中，请稍候......");
		                    $.ajax({
		                        url: "/admin/insurance/deal/" + row.id,
		                        success: function (text) {
		                            grid.reload();
		                        },
		                        error: function () {
		                        	mini.alert("处理失败，请稍后再试");
		                        	grid.unmask();
		                        }
		                    });
		                } 
		            }
		        );
		        
            }
        }
</script>
</body>
</html>
