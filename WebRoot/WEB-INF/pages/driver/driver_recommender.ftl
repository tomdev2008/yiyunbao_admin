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
                </tr>
            </table>           
        </div>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/driver/queryByRecommender" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20" > 
        <div property="columns">
            <div field="phone" allowSort="true" renderer="onGenderRenderer" align="center" headerAlign="center">
            	电话
            </div>            
            <div field="name"  allowSort="true">
            	姓名
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
            <div field="total_deal"  allowSort="true">
            	历史交易总金额
            </div>     
           <div field="recommender"  allowSort="true">
            	推荐人账号
            </div>   
            <div field="regist_time"  allowSort="true">
            	注册时间
            </div>     
            <div field="login_time"  allowSort="true">
            	最后登录时间
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
    
     function onActionRenderer(e) {
        var grid = e.sender;
        var record = e.record;
        var uid = record._uid;
        var rowIndex = e.rowIndex;

        var s = ' <a   href="javascript:delRow(\'' + uid + '\')">删除</a>';
        return s;
    }
    
    function search() {
        var phone = mini.get("phone").getValue();
        var name = mini.get("name").getValue();
        var id_card = mini.get("id_card").getValue();
        var plate_number = mini.get("plate_number").getValue();
        grid.load({ phone: phone , name : name , id_card : id_card , plate_number : plate_number});
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
</script>
</html>
