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
                    	<a class="mini-button" iconCls="icon-add" onclick="add()">添加商户</a>
                        <a class="mini-button" iconCls="icon-add" onclick="exportExcel()">全部导出excel</a>
                    </td>
                    <td style="white-space:nowrap;">
                        <input id="vender_name" class="mini-textbox" emptyText="商家名称" style="width:150px;" onenter="search"/>
                        <input id="goods_name" class="mini-textbox" emptyText="商品名称" style="width:150px;" onenter="search"/>
                        <input id="contact" class="mini-textbox" emptyText="联系人" style="width:150px;" onenter="search"/>
                        <input id="vender_phone" class="mini-textbox" emptyText="联系电话" style="width:150px;" onenter="search"/>
                        <a class="mini-button" onclick="search()" plain="true" iconCls="icon-search">查询</a>
                    </td>
                </tr>
            </table>           
        </div>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/vender/query" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20" > 
        <div property="columns">
        	<div name="action" width="200" headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">操作</div>
            <div field="vender_name"  align="center" headerAlign="center">
            	商家名称
            </div>            
            <div field="category"  align="center" headerAlign="center">
            	分类
            </div>            
            <div field="vender_province_name"  allowSort="false" >
            	省
            </div>
            <div field="vender_city_name"  allowSort="false" >
            	市
            </div>
            <div field="vender_district_name"  allowSort="false" >
            	区县
            </div>
            <div field="vender_address"  >
            	商家地址
            </div>
            <div field="contact"  >
            	联系人
            </div>
            <div field="vender_mobile"  >
            	联系手机
            </div>
            <div field="vender_phone"  >
            	联系电话
            </div>     
            <div field="goods_name"  >
            	商品名称
            </div>     
            <div field="normal_price"  >
            	正常价格
            </div>     
            <div field="member_price"  >
            	会员特惠
            </div>     
            <div field="other"  >
            	其他
            </div>     
            <div field="score"  >
            	评分
            </div>
            <div field="photo1"  >
            	照片1
            </div>
            <div field="photo2"  >
            	照片2
            </div>
            <div field="photo3"  >
            	照片3
            </div>
            <div field="photo4"  >
            	照片4
            </div>
            <div field="photo5"  >
            	照片5
            </div>
            <div field="longitude"  >
            	经度
            </div>
            <div field="latitude"  >
            	纬度
            </div>
        </div>
		</div> 
	</div>
    <form id="excelForm"  action="/admin/vender/exportExcel" method="post" target="excelIFrame">
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
        if (field == "photo1" || field == "photo2" || field == "photo3"|| field == "photo4"|| field == "photo5") {
        	if(value){
            	e.cellHtml = '<a href="' + value + '" target="_blank"> <img src="' + value + '" width="50" height="50"></a>';
            }
            else{
            	e.cellHtml = "";
            }

        }
    });
    
     function onActionRenderer(e) {
        var grid = e.sender;
        var record = e.record;
        var uid = record._uid;
        var rowIndex = e.rowIndex;

        var s = ' <a   href="javascript:delRow(\'' + uid + '\')">删除</a>'
                + ' <a   href="javascript:edit(\'' + uid + '\')">修改</a>'
        		+ ' <a   href="javascript:showReply(\'' + uid + '\')">查看评价</a>';

        if (grid.isEditingRow(record)) {
            s = '<a class="Update_Button" href="javascript:updateRow(\'' + uid + '\')">确定</a> '
                + '  <a class="Cancel_Button" href="javascript:cancelRow(\'' + uid + '\')">取消</a>';
        }
        return s;
    }
    
    function search() {
        var vender_name = mini.get("vender_name").getValue();
        var goods_name = mini.get("goods_name").getValue();
        var contact = mini.get("contact").getValue();
        var vender_phone = mini.get("vender_phone").getValue();
        grid.load({ vender_name: vender_name , goods_name : goods_name, contact:contact, vender_phone: vender_phone});
    }
    
     function exportExcel() {
        var excelForm = document.getElementById("excelForm");
        excelForm.submit();
     }
    
     function add() {
        mini.open({
            url: "/admin/vender/add",
            title: "增加商户", 
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
        
    function delRow(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
            	 mini.confirm("确定删除记录？", "确定？",
		            function (action) {            
		                if (action == "ok") {
		                     grid.loading("删除中，请稍候......");
		                    $.ajax({
		                        url: "/admin/vender/delete/" + row.id,
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
     
     //修改
     function edit(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
                 mini.open({
                    url: "/admin/vender/edit/" + row.id,
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
     
     //查看评价
     function showReply(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
		         mini.open({
	                url: "/admin/venderReply/" + row.id,
	                title: "查看评价", 
	                width: 650, 
	                height: 360
	            });
            }
     }
</script>
</html>
