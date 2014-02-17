<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
 	<title>首页</title>
    <script src="/admin/js/boot.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
    .header
    {
        background:url(/admin/images/header.gif) repeat-x 0 -1px;
    }
    </style>
    
 </head>
<body>
<!--Layout-->
<div id="layout1" class="mini-layout" style="width:100%;height:100%;">
    <div title="center" region="center" bodyStyle="overflow:hidden;" style="border:0;">
        <!--Splitter-->
        	<div class="mini-toolbar" style="border-bottom:0;padding:0px;">
	            <table style="width:100%;">
	                <tr>
	                    <td style="width:100%;">
	                        <a class="mini-button" iconCls="icon-add" onclick="newRow()">增加</a>
	                    </td>
	                </tr>
	            </table>           
        	</div>
        	<div class="mini-fit" >
	           <div id="grid" class="mini-datagrid" style="width:100%;height:100%;" url="/admin/dict/getListByType/${type}" allowAlternating="true" showPager="false">
		        <div property="columns">            
		        	<div name="action"  headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">
		        		操作
		        	</div>
		            <div field="id" width="120" headerAlign="center" allowSort="true">
		             	编号
		            </div>                
		            <div field="name" width="120" headerAlign="center" allowSort="true">
		             	名称
		             	<input property="editor" class="mini-textbox" style="width:100%;"/>
		            </div>                
		            <div field="dict_desc" width="100" allowSort="true" renderer="onGenderRenderer" align="center" headerAlign="center">
		            	描述
		            	<input property="editor" class="mini-textbox" style="width:100%;"/>
		            </div>            
		        </div>
	        </div>
   	 </div>      
    </div>
</div>

    

</body>
</html>
<script type="text/javascript">
        mini.parse();

		var grid = mini.get("grid");
		grid.load();

        function onActionRenderer(e) {
	        var grid = e.sender;
	        var record = e.record;
	        var uid = record._uid;
	        var rowIndex = e.rowIndex;
	
	        var s = '<a   href="javascript:editRow(\'' + uid + '\')" >编辑</a>'
	                + ' <a   href="javascript:delRow(\'' + uid + '\')">删除</a>';
	
	        if (grid.isEditingRow(record)) {
	            s = '<a class="Update_Button" href="javascript:updateRow(\'' + uid + '\')">【确定】</a> '
	                + '    <a class="Cancel_Button" href="javascript:cancelRow(\'' + uid + '\')">【取消】</a>'
	        }
	        return s;
    	}
        
	    function newRow() {            
	        var row = {};
	        grid.addRow(row, 0);
	
	        grid.cancelEdit();
	        grid.beginEditRow(row);
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
	            url: "/admin/dict/save",
	            data: { data: json , dict_type:'${type}'},
	            success: function (text) {
	                grid.reload();
	            },
	            error: function (jqXHR, textStatus, errorThrown) {
	                alert(jqXHR.responseText);
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
		                        url: "/admin/dict/delete/" + row.id,
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
