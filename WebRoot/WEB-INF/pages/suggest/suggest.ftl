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
                        <input id="phone" class="mini-textbox" emptyText="手机号" style="width:150px;" onenter="onKeyEnter"/>  
                        <input id="beginDate" class="mini-datepicker" format="yyyy-MM-dd" emptyText="开始时间"/>
                        <input id="endDate" class="mini-datepicker" format="yyyy-MM-dd" emptyText="结束时间"/>  
                        <a class="mini-button" onclick="search()" plain="true" iconCls="icon-search">查询</a>
                    </td>
                </tr>
            </table>           
        </div>
        <div class="mini-fit" >
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="suggest/query" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20"> 
        <div property="columns">
        	 <div name="action"  headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">操作</div>
            <div field="phone" allowSort="true" renderer="onGenderRenderer" align="center" headerAlign="center">
            	电话
            </div>            
            <div field="suggest_content" >
            	建议内容
            </div>
            <div field="create_time"  allowSort="true" dateFormat="yyyy-MM-dd">
            	提交时间
            </div>     
        </div>
		</div> 
	</div>

</body>
<script>
    mini.parse();
    var grid = mini.get("datagrid");
    grid.load();
    
     function onActionRenderer(e) {
        var grid = e.sender;
        var record = e.record;
        var uid = record._uid;
        var rowIndex = e.rowIndex;

        var s = ' <a   href="javascript:delRow(\'' + uid + '\')">删除</a>';

        if (grid.isEditingRow(record)) {
            s = '<a class="Update_Button" href="javascript:updateRow(\'' + uid + '\')">确定</a> '
                + '  <a class="Cancel_Button" href="javascript:cancelRow(\'' + uid + '\')">取消</a>'
        }
        return s;
    }
    
    function search() {
        var phone = mini.get("phone").getValue();
        var beginDate = mini.get("beginDate").getFormValue();
        var endDate = mini.get("endDate").getFormValue();
        grid.load({ phone: phone , beginDate : beginDate , endDate : endDate});
    }
        
    
     function delRow(row_uid) {
             var row = grid.getRowByUID(row_uid);
            if (row) {
            	 mini.confirm("确定删除记录？", "确定？",
		            function (action) {            
		                if (action == "ok") {
		                     grid.loading("删除中，请稍候......");
		                    $.ajax({
		                        url: "/admin/suggest/delete/" + row.id,
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
