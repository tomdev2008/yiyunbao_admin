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
                    	<a class="mini-button" iconCls="icon-add" onclick="showWindow();">发布公告</a>
                    </td>
                </tr>
            </table>           
        </div>
		<div id="datagrid" class="mini-datagrid" style="width:100%;height:100%;" 
        url="/admin/msg/list" allowResize="true" idField="id" multiSelect="true" allowAlternating="true" pageSize="20"> 
        <div property="columns">
        	 <div name="action"  headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">操作</div>
            <div field="msg_title" allowSort="true" renderer="onGenderRenderer" align="center" headerAlign="center">
            	标题
            </div>            
            <div field="msg_content" >
            	内容
            </div>
            <div field="msg_time"  allowSort="true" dateFormat="yyyy-MM-dd">
            	发布时间
            </div>     
        </div>
		</div> 
	</div>
	<div id="win1" class="mini-window" title="发布公告" style="width:530px;height:360px;display:none;">
	<table border="0">
		<tr>
			<td width="">标题：</td>
			<td>
			<input id="msg_title" name="msg_title" class="mini-textbox" emptyText="标题不能超过50字符，内容不能超过500字符" style="width:450px;"	 />
			</td>
		</tr>
		 <tr>
		 	<td>内容：</td>
		 	<td>
		 	<textarea id="ke" name="content" style="width:100%;height:250px;"></textarea>
		 	</td>
		 </tr>
		 </table>
        <div style="text-align:center;padding:8px;">
        <a class="mini-button" onclick="save()" style="width:60px;">发布</a>
        </div>
    </div>
</body>
<script src="http://www.kindsoft.net/ke4/kindeditor-min.js?20120115.js" type="text/javascript"></script>
<script>
    mini.parse();
    var grid = mini.get("datagrid");
    grid.load();
    
    
    var editorId = "ke";
    function showWindow() {
        var win = mini.get("win1");
        win.show();
    }


    //-------------------------------------------------------------
    var editor = null;
    setTimeout(function () {
        editor = KindEditor.create('#' + editorId, {
            resizeType: 1,
            uploadJson: 'kindeditor/upload_json.ashx',
            fileManagerJson: 'kindeditor/file_manager_json.ashx',
            allowPreviewEmoticons: false,
            allowImageUpload: true,
            items: [
		    'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
		    'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
		    'insertunorderedlist', '|', 'emoticons', 'image', 'link']
        });
    }, 1);
    
    function save() {
    	var msg_title = mini.get("msg_title").getValue();
    	var msg_content = editor.html();
    	if(null == msg_title || "" == msg_title){
    		mini.alert("请输入标题.");
    		return;
    	}
    	if(null == msg_content || "" == msg_content){
    		mini.alert("请输入内容.");
    		return;
    	}
    	var json1 = [{"msg_title":msg_title,"msg_content":msg_content}];
    	var json2 = mini.encode(json1);
        $.ajax({
            url: "/admin/msg/save",
            data: { data: json2 },
            success: function (text) {
            	var o = mini.decode(text);
            	if(!o.success){
            		mini.alert(o.data);
            	}else{
            		window.location.reload();
            	}
            },
            error: function () {
            	mini.alert("发布失败，请查看是否字数超出限制");
            	grid.unmask();
            }
        });
    }
    
    
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
    
     function delRow(row_uid) {
             var row = grid.getRowByUID(row_uid);
            if (row) {
            	 mini.confirm("确定删除记录？", "确定？",
		            function (action) {            
		                if (action == "ok") {
		                     grid.loading("删除中，请稍候......");
		                    $.ajax({
		                        url: "/admin/msg/delete/" + row.id,
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
