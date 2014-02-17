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
                    	<a class="mini-button" iconCls="icon-add" onclick="add()">增加</a>
                    </td>
                    <td style="white-space:nowrap;">
                        <input id="user_account" class="mini-textbox" emptyText="登录账号" style="width:150px;" onenter="search"/>  
                        <input id="user_name" class="mini-textbox" emptyText="姓名" style="width:150px;" onenter="search"/>  
                        <input id="cell" class="mini-textbox" emptyText="手机号" style="width:150px;" onenter="search"/>  
                        <a class="mini-button" onclick="search()" plain="true" iconCls="icon-search">查询</a>
                    </td>
                </tr>
            </table>           
        </div>
        <div class="mini-fit" >
			<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" url="/admin/sys/user/list" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20" > 
		        <div property="columns">
		        	<div name="action" width="200" headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">
		        		操作
		        	</div>
		            <div field="id" allowSort="true" align="center" headerAlign="center">
		            	编号
		            </div>            
		            <div field="user_account">
		            	登录账号
		            </div>
		            <div field="user_name">
		            	姓名
		            	<input property="editor" class="mini-textbox" required="true" style="width:100%;"/>
		            </div>
		            <div field="max_money"  allowSort="true">
		            	每日最大充值金额
		            	<input property="editor" class="mini-spinner" maxValue="10000000" vtype="int" increment="100" required="true" style="width:100%;"/>
		            </div>
		            <div field="id_card">
		            	身份证
		            	<input property="editor" class="mini-textbox" style="width:100%;"/>
		            </div>     
		            <div field="email"  allowSort="true">
		            	邮箱
		            	<input property="editor" class="mini-textbox" style="width:100%;"/>
		            </div>     
		            <div field="cell"  >
		            	手机号码
		            	<input property="editor" class="mini-textbox" style="width:100%;"/>
		            </div>     
		            <div field="tel" >
		            	座机号码
		            	<input property="editor" class="mini-textbox" style="width:100%;"/>
		            </div>     
		            <div field="create_time"  allowSort="true">
		            	创建时间
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

        var s = ' <a   href="javascript:delRow(\'' + uid + '\')">删除</a>'
        		+ ' <a   href="javascript:editRow(\'' + uid + '\')">修改</a>' 
        		+ ' <a   href="javascript:showPrivilege(\'' + uid + '\')">权限</a>'; 

        if (grid.isEditingRow(record)) {
            s = '<a class="Update_Button" href="javascript:updateRow(\'' + uid + '\')">确定</a> '
                + '  <a class="Cancel_Button" href="javascript:cancelRow(\'' + uid + '\')">取消</a>';
        }
        return s;
    }
    
    function search() {
        var user_account = mini.get("user_account").getValue();
        var user_name = mini.get("user_name").getValue();
        var cell = mini.get("cell").getValue();
        grid.load({ user_account: user_account , user_name : user_name , cell : cell});
    }
        
    function delRow(row_uid) {
            var row = grid.getRowByUID(row_uid);
            if (row) {
            	 mini.confirm("确定删除记录？", "确定？",
		            function (action) {            
		                if (action == "ok") {
		                     grid.loading("删除中，请稍候......");
		                    $.ajax({
		                        url: "/admin/sys/user/delete/" + row.id,
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
   
    function add() {
            mini.open({
                url: "/admin/sys/user/add",
                title: "新增员工", width: 600, height: 360,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { action: "new"};
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                    grid.reload();
                }
            });
        }
        
        
     function editRow(row_uid) {
        var row = grid.getRowByUID(row_uid);
        if (row) {
            grid.cancelEdit();
            grid.beginEditRow(row);
        }
    }
        
    function cancelRow(row_uid) {            
        grid.reload();
    }
    
    function updateRow(row_uid) {
        var row = grid.getRowByUID(row_uid);
        grid.commitEdit();
        var rowData = grid.getChanges();
        
        grid.loading("保存中，请稍后......");
        var json = mini.encode(rowData);
        
        $.ajax({
            url: "/admin/sys/user/update",
            data: { data: json },
            success: function (text) {
                grid.reload();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText);
            }
        });

    }
    
    function showPrivilege(row_uid) {
		var row = grid.getRowByUID(row_uid);
        if (row) {
	        mini.open({
                url: "/admin/sys/privilege/userPrivilege/" + row.id,
                title: "权限设置", 
                width: 700, 
                height: 500
            });
        }
    }
</script>
</html>
