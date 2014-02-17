<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
    <script>
    	var couponStatus = [
	    { "id": "0", "text": "未发送" },
	    { "id": "1", "text": "已发送" },
	    { "id": "2", "text": "已消费" }
		]
    </script>
 </head>
<body>
        <div class="mini-fit" >
         <div class="mini-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                    	<a class="mini-button" iconCls="icon-add" onclick="add()">新充值卡</a>
                    </td>
                     <td style="white-space:nowrap;">
                        <input id="status" name="status" class="mini-combobox"  data="couponStatus" valueField="id" textField="text"/> 
                        <a class="mini-button" onclick="search()" plain="true" iconCls="icon-search">查询</a>
                    </td>
                </tr>
            </table>           
        </div>
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/coupon/list" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20"> 
        <div property="columns">
        	 <div name="action"  headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">操作</div>
            <div field="card_no" allowSort="true" renderer="onGenderRenderer" align="center" headerAlign="center">
            	卡号
            </div>            
            <div field="send_mobile" >
            	业务员手机号
            </div>
            <div field="status" >
            	状态
            </div>
            <div field="use_time" >
            	消费时间
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
            	e.cellHtml = "未发送";
            }
            else if(value == 1){
            	e.cellHtml = "<font color='green'>已发送</font>";
            }
            else if(value == 2){
            	e.cellHtml = "<font color='red'>已消费</font>";
            }

        }
    });
    
     function onActionRenderer(e) {
        var grid = e.sender;
        var record = e.record;
        var uid = record._uid;
        var rowIndex = e.rowIndex;

        var s = ' <a   href="javascript:showSMS(\'' + uid + '\')">发送</a>';

        if (grid.isEditingRow(record)) {
            s = '<a class="Update_Button" href="javascript:updateRow(\'' + uid + '\')">确定</a> '
                + '  <a class="Cancel_Button" href="javascript:cancelRow(\'' + uid + '\')">取消</a>'
        }
        return s;
    }
    
     function add() {
     	$.post("/admin/coupon/add",
  		{
  			
 		},
 		 function(response,status){
 		 	if(!response.success){
 		 		mini.alert(response.data,'生成充值卡失败');
 		 	}
 		 	else{
 		 		grid.load();
 		 	}
 		 });
     }
    function search() {
        var status = mini.get("status").getValue();
        grid.load({ status: status });
    }  
    
     
    //显示短信发送窗口
    function showSMS(row_uid) {
        var row = grid.getRowByUID(row_uid);
        if (row) {
	        mini.prompt("请输入手机号码：", "发送充值卡密",
            function (action, value) {
                if (action == "ok") {
                	if(!value){
                		mini.alert("请输入手机号码");
                		return;
                	}
                    $.post("/admin/coupon/sms",
			  		 {
			  		  phone:value,
			  		  coupon_id:row.id
			 		 },
			 		 function(response,status){
			 		 	if(response.success){
			 		 		 mini.alert("发送成功");
			 		 	}
			 		 	else{
			 		 		 mini.alert(response.data);
			 		 	}
			 		 });
                } 
            },
            true
       	 );
       }
         
    } 
</script>
</html>
