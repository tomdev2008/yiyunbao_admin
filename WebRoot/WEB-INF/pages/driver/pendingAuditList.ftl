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
                    	<!--<a class="mini-button" iconCls="icon-add" onclick="exportExcel()">全部导出excel</a>-->
                    </td>
                    <td style="white-space:nowrap;">
                        <input id="phone" class="mini-textbox" emptyText="手机号" style="width:150px;" onenter="search"/>  
                        <input id="name" class="mini-textbox" emptyText="姓名" style="width:150px;" onenter="search"/>  
                        <input id="id_card" class="mini-textbox" emptyText="身份证" style="width:150px;" onenter="search"/>  
                        <input id="plate_number" class="mini-textbox" emptyText="车牌号" style="width:150px;" onenter="search"/>  
                        <a class="mini-button" onclick="search()" plain="true" iconCls="icon-search">查询</a>
                    </td>
                </tr>
            </table>           
        </div>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/driver/queryPendingAuditList" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20" > 
        <div property="columns">
        	<div name="action" width="200" headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">操作</div>
            <div field="phone" allowSort="true" renderer="onGenderRenderer" align="center" headerAlign="center">
            	电话
            </div>            
            <div field="name"  allowSort="true">
            	姓名
            </div>
	    <div field="main_route" width="200">
            	主营路线
            </div>
            <div field="id_card"  allowSort="true">
            	身份证
            </div>     
            <div field="plate_number"  allowSort="true">
            	车牌号
            </div>     
            <div field="car_type_name"  >
            	车型
            </div>     
            <div field="car_length" >
            	车长
            </div>     
            <div field="gold"  allowSort="true">
            	物流币
            </div>     
            <div field="id_card_photo"  >
            	身份证照片
            </div>     
            <div field="license_photo"  >
            	驾驶证照片
            </div>     
            <div field="registration_photo"  >
            	行驶证照片
            </div>     
            <div field="total_deal"  allowSort="true">
            	历史交易总金额
            </div>     
            <div field="recommender"  allowSort="true">
            	推荐人账号
            </div>       
            <div field="regist_time"  allowSort="true">
            	注册时间
            </div>     
        </div>
		</div> 
	</div>
<form id="excelForm"  action="/admin/driver/exportPendingAudit" method="post" target="excelIFrame">
    </form>
    <iframe id="excelIFrame" name="excelIFrame" style="display:none;"></iframe>
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
        var uid = record._uid;
        //格式化
        if (field == "id_card_photo" || field == "license_photo" || field == "registration_photo") {
        	if(value){
            	e.cellHtml = '<a href="' + value + '" target="_blank"> <img src="' + value + '" width="50" height="50"></a>';
            }
            else{
            	e.cellHtml = "暂无";
            }

        }
       
    });
    
     function onActionRenderer(e) {
        var grid = e.sender;
        var record = e.record;
        var uid = record._uid;
        var rowIndex = e.rowIndex;

        var s = ' <a   href="javascript:pass(\'' + uid + '\')">审核通过</a>'
        		+ ' <a   href="javascript:reject(\'' + uid + '\')">拒绝审核</a>' 
        		+ ' <a   href="javascript:edit(\'' + uid + '\')">修改</a>' ;

        return s;
    }
    
    function search() {
        var phone = mini.get("phone").getValue();
        var name = mini.get("name").getValue();
        var id_card = mini.get("id_card").getValue();
        var plate_number = mini.get("plate_number").getValue();
        grid.load({ phone: phone , name : name , id_card : id_card , plate_number : plate_number});
    }
        
    function pass(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
            	 mini.confirm("确定审核通过？", "确定？",
		            function (action) {            
		                if (action == "ok") {
		                     grid.loading("处理中，请稍候......");
		                    $.ajax({
		                        url: "/admin/driver/pass/" + row.id,
		                        success: function (text) {
		                            grid.reload();
		                        },
		                        error: function () {
		                        	mini.alert("审核失败，请稍后再试");
		                        	grid.unmask();
		                        }
		                    });
		                } 
		            }
		        );
		        
            }
        }
        
    function reject(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
            	 mini.confirm("确定拒绝审核？", "确定？",
		            function (action) {            
		                if (action == "ok") {
		                     grid.loading("处理中，请稍候......");
		                    $.ajax({
		                        url: "/admin/driver/reject/" + row.id,
		                        success: function (text) {
		                            grid.reload();
		                        },
		                        error: function () {
		                        	mini.alert("审核失败，请稍后再试");
		                        	grid.unmask();
		                        }
		                    });
		                } 
		            }
		        );
		        
            }
        }
   
    function edit(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
                 mini.open({
                    url: "/admin/driver/edit/" + row.id,
                    title: "修改", 
                    width: 700, 
                    height: 600,
                    ondestroy: function (action) {
                        //grid.reload();
                        search();
                    }
                });
            }
     }
  
   function exportExcel() {
        var excelForm = document.getElementById("excelForm");
        excelForm.submit();
     }
</script>
</html>
