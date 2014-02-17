<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
 </head>
<body>
	<div class="mini-fit">
		<a class="mini-button" iconCls="icon-ok" style="width:60px;" onclick="checkAll()">全选</a>
		<a class="mini-button" iconCls="icon-cancel" style="width:70px;" onclick="unCheckAll()">全不选</a>
        <ul id="tree1" class="mini-tree"  style="width:100%;height:95%;" url="/admin/sys/privilege/queryByUser/${userId}"
                    showTreeIcon="true" textField="privilege_name" idField="id" resultAsTree="false"  
                     onnodedblclick="onNodeDblClick" expandOnDblClick="false" showCheckBox="true" >    
        </ul>
    </div>                
    <div class="mini-toolbar" style="text-align:center;padding-top:8px;padding-bottom:8px;" 
        borderStyle="border-left:0;border-bottom:0;border-right:0;">
        <a class="mini-button" style="width:60px;" onclick="onOk()">确定</a>
        <span style="display:inline-block;width:25px;"></span>
        <a class="mini-button" style="width:60px;" onclick="onCancel()">取消</a>
    </div>

</body>
</html>
<script type="text/javascript">
    mini.parse();

    var tree = mini.get("tree1");
	
    function CloseWindow(action) {
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();
    }

    function onOk() {
        var value = tree.getValue();
        $.ajax({
			type:'post',
			url:'/admin/sys/privilege/updateUserPrivilege',
			data:{userId:${userId} , privilege:value},
			dataType:'text',
			success:function(msg){
			   CloseWindow("ok"); 
			},
			error:function(){
				mini.alert("修改权限失败，请稍候再试");
		 	}
		});
    }
    function onCancel() {
        CloseWindow("cancel");
    }
    
    function checkAll(){
    	var nodes = tree.getAllChildNodes(tree.getRootNode());
        tree.checkNodes(nodes);	
    }
    
    function unCheckAll(){
    	 var nodes = tree.getAllChildNodes(tree.getRootNode());
        tree.uncheckNodes(nodes);
    }
	
</script>
