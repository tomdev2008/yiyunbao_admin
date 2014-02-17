<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
 </head>
<body>
        <div class="mini-fit" >
         <div class="mini-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                    	<a class="mini-button" iconCls="icon-add" onclick="add()">添加广告</a>
                    </td>
                </tr>
            </table>           
        </div>
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/ad/list" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20"> 
        <div property="columns">
        	 <div name="action"  headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">操作</div>
            <div field="ad_location" allowSort="true" renderer="onGenderRenderer" align="center" headerAlign="center">
            	位置
            </div>            
            <div field="ad_title" allowSort="true" renderer="onGenderRenderer" align="center" headerAlign="center">
            	标题
            </div>            
            <div field="ad_img" >
            	图片
            </div>
            <div field="ad_link" >
            	链接地址
            </div>
            <div field="create_time"  allowSort="true" >
            	发布时间
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
        if (field == "ad_location") {
        	if(value == 1){
            	e.cellHtml = "首页";
            }
            else if(value == 2){
            	e.cellHtml = "个人主页顶部";
            }
            else{
            	e.cellHtml = "未知";
            }

        }
         if (field == "ad_img") {
        	e.cellHtml = "<img width='100' height='50' src='" + value + "'>";
        }
        if (field == "ad_link") {
        	e.cellHtml = "<a target='_blank' href='" + value + "'>" + value + "</a>";
        }
    });
    
     function onActionRenderer(e) {
        var grid = e.sender;
        var record = e.record;
        var uid = record._uid;
        var rowIndex = e.rowIndex;

        var s = ' <a   href="javascript:delRow(\'' + uid + '\')">删除</a>';
        	s+= ' <a   href="javascript:edit(\'' + record.id + '\')">修改</a>'

        if (grid.isEditingRow(record)) {
            s = '<a class="Update_Button" href="javascript:updateRow(\'' + uid + '\')">确定</a> '
                + '  <a class="Cancel_Button" href="javascript:cancelRow(\'' + uid + '\')">取消</a>'
        }
        return s;
    }
    
     function add() {
            mini.open({
                url: "/admin/ad/add",
                title: "发布公告", width: 500, height: 300,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { action: "new"};
                },
                ondestroy: function (action) {
                    grid.reload();
                }
            });
        }
    
     function edit(id) {
            mini.open({
                url: "/admin/ad/edit/" + id,
                title: "修改公告", width: 500, height: 300,
                ondestroy: function (action) {
                    grid.reload();
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
		                        url: "/admin/ad/delete/" + row.id,
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
