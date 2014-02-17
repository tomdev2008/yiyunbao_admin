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
                </tr>
            </table>           
        </div>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/user/queryByRecommender" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20" > 
        <div property="columns">
            <div field="phone" allowSort="true" renderer="onGenderRenderer" align="center" headerAlign="center">
            	电话
            </div>            
            <div field="company_name"  allowSort="true">
            	公司
            </div>
            <div field="name"  allowSort="true">
            	联系人
            </div>
            <div field="gold"  allowSort="true">
            	物流币
            </div>     
            <div field="telcom"  allowSort="true">
            	座机
            </div>     
            <div field="address"  allowSort="true">
            	地址
            </div>     
            <div field="fleet_count"  allowSort="true">
            	车队司机数目
            </div>     
            <div field="licence_photo"  allowSort="true">
            	营业执照
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
<form id="excelForm"  action="/admin/user/exportExcel" method="post" target="excelIFrame">
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
        //格式化状态
        if (field == "licence_photo") {
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

        var s = ' <a   href="javascript:delRow(\'' + uid + '\')">删除</a>'
        		+ ' <a   href="javascript:showReply(\'' + uid + '\')">评价</a>' 
        		+ ' <a   href="javascript:showOrder(\'' + uid + '\')">订单</a>' 
        		+ ' <a   href="javascript:showBusiness(\'' + uid + '\')">交易</a>' 
        		+ ' <a   href="javascript:showRecharge(\'' + uid + '\')">充值</a>' 
        		+ ' <a   href="javascript:edit(\'' + uid + '\')">修改</a>' ;

        if (grid.isEditingRow(record)) {
            s = '<a class="Update_Button" href="javascript:updateRow(\'' + uid + '\')">确定</a> '
                + '  <a class="Cancel_Button" href="javascript:cancelRow(\'' + uid + '\')">取消</a>';
        }
        return s;
    }
    
    function search() {
        var phone = mini.get("phone").getValue();
        var name = mini.get("name").getValue();
        var company_name = mini.get("company_name").getValue();
        var recommender = mini.get("recommender").getValue();
        grid.load({ phone: phone , name : name , company_name : company_name ,recommender:recommender});
    }
        
    function delRow(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
            	 mini.confirm("确定删除记录？", "确定？",
		            function (action) {            
		                if (action == "ok") {
		                     grid.loading("删除中，请稍候......");
		                    $.ajax({
		                        url: "/admin/user/delete/" + row.id,
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
     
     //查看评价
     function showReply(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
		         mini.open({
	                url: "/admin/userReply/" + row.id,
	                title: "查看评价", 
	                width: 600, 
	                height: 360
	            });
            }
     }
     
     //查看订单
     function showOrder(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
		         mini.open({
	                url: "/admin/order/userOrder/" + row.id,
	                title: "查看订单", 
	                width: 800, 
	                height: 360
	            });
            }
        }
          
     //查看交易
     function showBusiness(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
		         mini.open({
	                url: "/admin/business/userBusiness/" + row.id,
	                title: "查看交易", 
	                width: 700, 
	                height: 360
	            });
            }
        }
        
         //充值
     function showRecharge(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
		         mini.open({
	                url: "/admin/recharge/user/" + row.id,
	                title: "充值", 
	                width: 300, 
	                height: 150
	            });
            }
        }
        
     function add() {
        mini.open({
            url: "/admin/user/add",
            title: "增加货主", 
            width: 600, 
            height: 600,
            onload: function () {
                var iframe = this.getIFrameEl();
                var data = { action: "new"};
                //iframe.contentWindow.SetData(data);
            },
            ondestroy: function (action) {
                //grid.reload();
                search();
            }
        });
     }
     
     function exportExcel() {
        var excelForm = document.getElementById("excelForm");
        excelForm.submit();
     }
     
      //修改
     function edit(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
                 mini.open({
                    url: "/admin/user/edit/" + row.id,
                    title: "修改", 
                    width: 600, 
                    height: 600,
                    ondestroy: function (action) {
                        //grid.reload();
                        search();
                    }
                });
            }
     }
</script>
</html>
