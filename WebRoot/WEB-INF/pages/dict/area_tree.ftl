<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
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
	<div class="mini-fit">
        <ul id="tree1" class="mini-tree"  style="width:100%;height:95%;" url="/admin/dict/getArea"
                    showTreeIcon="true" textField="name" idField="id" resultAsTree="true"  
                     onnodedblclick="onNodeDblClick" expandOnDblClick="false">    
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
	
	function GetData() {
        var node = tree.getSelectedNode();
        return node;
    }
	
	
	function onNodeDblClick(e) {
        onOk();
    }
    function CloseWindow(action) {
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();
    }

    function onOk() {
        var node = tree.getSelectedNode();
        CloseWindow("ok");        
    }
    function onCancel() {
        CloseWindow("cancel");
    }
	
</script>
