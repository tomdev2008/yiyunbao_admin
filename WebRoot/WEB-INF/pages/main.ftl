<html>
 <head>
 	<title>易运宝管理后台</title>
    <script src="/admin/js/boot.js"></script>
    <SCRIPT type="text/javascript" src="/admin/js/jquery-1.6.2.min.js"></SCRIPT>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
    .header
    {
        background:url(/admin/images/header.gif) repeat-x 0 -1px;
    }
    </style>
    <script>
    function getNewOrderCount(){
		$.get("/admin/order/getNewOrderCount",
		 { },
		function(response,status){
			if(response.data > 0 ){
				$("#sound").html('<embed id = "soundControl1"  src="http://www.1yunbao.com/admin/sound.mp3"  mastersound hidden = "true"  loop ="false"  autostart = "true"></embed>');
			}
		});
	}
	jQuery(function($){
		setInterval(getNewOrderCount,60000);
	});
    </script>
 </head>
<body>
<div id="sound"></div>
<!--Layout-->
<div id="layout1" class="mini-layout" style="width:100%;height:100%;">
    <div class="header" region="north" height="73" showSplit="false" showHeader="false">
    	
        <h1 style="margin:0;padding:15px;cursor:default;font-family:'Trebuchet MS',Arial,sans-serif;"><img src="/admin/images/logo.png" width="70" height="36"/>后台管理系统</h1>
    	<div style="position:absolute;top:38px;right:180px;">
    		欢迎您，<font color="red">${userName!}</font>
    	</div>
        <div style="position:absolute;top:18px;right:10px;">
            <a class="mini-button mini-button-iconTop" iconCls="icon-edit" onclick="newPWD"  plain="true" >修改密码</a>        
            <a class="mini-button mini-button-iconTop" iconCls="icon-close" href="/admin/logout"  plain="true" >退出登录</a>        
            
        </div>
    </div>
    <div title="south" region="south" showSplit="false" showHeader="false" height="30" >
        <div style="line-height:28px;text-align:center;cursor:default">Copyright © 易运科技 版权所有 </div>
    </div>
    <div region="west" title="菜单导航" showHeader="true" bodyStyle="padding-left:1px;" width="230" > 
    	<ul id="leftTree" class="mini-tree" url="/admin/userPrivilege" style="width:100%;height:95%;" 
                    showTreeIcon="true" textField="privilege_name" idField="id" resultAsTree="false"  
                    onnodeclick="onNodeSelect">        
        </ul>
    </div>
    <div title="center" region="center" bodyStyle="overflow:hidden;" style="border:0;">
        <!--Splitter-->
            <div showCollapseButton="false" style="border:0px;" >
                    <iframe  src="/admin/welcome.html" id="mainframe" frameborder="0" name="main" style="width:100%;height:100%;" border="0"></iframe>

        	</div>
    </div>
</div>

    

</body>
</html>
<script type="text/javascript">
        mini.parse();

        var tree = mini.get("leftTree");

        function onNodeSelect(e) {
            var node = e.node;
            var isLeaf = e.isLeaf;
            if (isLeaf) {
	        	document.getElementById("mainframe").src=node.url;
            }
        }
        
        function newPWD() {
	        mini.open({
                url: "/admin/newPWD",
                title: "修改密码", 
                width: 300, 
                height: 200
            });
        }
    </script>
