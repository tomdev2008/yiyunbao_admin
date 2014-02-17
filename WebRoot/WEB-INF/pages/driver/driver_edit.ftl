<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?key=&v=1.4&services=true"></script>
    <script src="/admin/js/swfupload/swfupload.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/js/fancybox/jquery.fancybox.css" type="text/css" media="screen" />
    <script type="text/javascript" src="/js/fancybox/jquery.fancybox.pack.js"></script>
    <script type="text/javascript" src="/admin/js/jquery.upload2.js"></script>
    <style type="text/css">
		html, body
	    {
	        font-size:12px;
	        padding:0;
	        margin:0;
	        border:0;
	        height:100%;
	        overflow:hidden;
	    }
        .area {
            padding:5px;
        }
        .area li{
            list-style: none;
            float: left;
            margin: 3px;
        }
        /*去除ie10下的placeholder重影*/
        input:-ms-input-placeholder {
            color:#fff;
        }
	</style>
 </head>
<body>
    <div id="tabs1" class="mini-tabs" activeIndex="0" style="width:100%;height:600px;" plain="false">
        <div title="司机信息" showCloseButton="false">
            <form id="form1" method="post">
            	<input class="mini-hidden" value="${driver.id}" id="id" name="id" >
            	<table style="table-layout:fixed;">
                    <tr>
                        <td style="width:70px;">主营线路
                        </td>
                        <td style="width:150px;">
                            <input id="start_province" name="start_province" class="mini-combobox" emptyText="省/直辖市" 
                            textField="name"  valueField="id" onvaluechanged="onStartProvinceChanged" url="/admin/dict/getArea?id=0" showNullItem="false" required="true" value="${driver.start_province!""}"/>
                        </td>
                        <td style="width:150px;">
                            <input id="start_city" name="start_city" class="mini-combobox"  emptyText="地市" url="/admin/dict/getArea?id=${driver.start_province!""}"  textField="name"  valueField="id" required="true" value="${driver.start_city!""}"/>
                        </td>
                        <td style="width:150px;">
                        	至
                            <input id="end_province" name="end_province" class="mini-combobox" emptyText="省/直辖市" 
                            textField="name"  valueField="id" onvaluechanged="onEndProvinceChanged" url="/admin/dict/getArea?id=0" showNullItem="false" required="true" value="${driver.end_province!""}"/>
                        </td>
                        <td style="width:150px;">
                            <input id="end_city" name="end_city" class="mini-combobox"  emptyText="地市" url="/admin/dict/getArea?id=${driver.end_province!""}" textField="name"  valueField="id" required="true" value="${driver.end_city!""}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:70px;">主营线路2
                        </td>
                        <td style="width:150px;">
                            <input id="start_province2" name="start_province2" class="mini-combobox" emptyText="省/直辖市"  required="true"
                            textField="name"  valueField="id" onvaluechanged="onStartProvinceChanged2" url="/admin/dict/getArea?id=0" showNullItem="false" value="${driver.start_province2!""}"/>
                        </td>
                        <td style="width:150px;">
                            <input id="start_city2" name="start_city2" class="mini-combobox"  emptyText="地市" url="/admin/dict/getArea?id=${driver.start_province2!""}" textField="name"  valueField="id" required="true" value="${driver.start_city2!""}"/>
                        </td>
                        <td style="width:150px;">
                        	至
                            <input id="end_province2" name="end_province2" class="mini-combobox" emptyText="省/直辖市"  required="true"
                            textField="name"  valueField="id" onvaluechanged="onEndProvinceChanged2" url="/admin/dict/getArea?id=0" showNullItem="false" value="${driver.end_province2!""}"/>
                        </td>
                        <td style="width:150px;">
                            <input id="end_city2"  name="end_city2" class="mini-combobox"  emptyText="地市" url="/admin/dict/getArea?id=${driver.end_province2!""}" textField="name"  valueField="id" required="true" value="${driver.end_city2!""}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:70px;">主营线路3
                        </td>
                        <td style="width:150px;">
                            <input id="start_province3" name="start_province3" class="mini-combobox" emptyText="省/直辖市" required="true"
                            textField="name"  valueField="id" onvaluechanged="onStartProvinceChanged3" url="/admin/dict/getArea?id=0" showNullItem="false" value="${driver.start_province3!""}"/>
                        </td>
                        <td style="width:150px;">
                            <input id="start_city3" name="start_city3" class="mini-combobox"  emptyText="地市" url="/admin/dict/getArea?id=${driver.start_province3!""}" textField="name"  valueField="id" required="true" value="${driver.start_city3!""}"/>
                        </td>
                        <td style="width:150px;">
                        	至
                            <input id="end_province3" name="end_province3" class="mini-combobox" emptyText="省/直辖市" required="true"
                            textField="name"  valueField="id" onvaluechanged="onEndProvinceChanged3" url="/admin/dict/getArea?id=0" showNullItem="false"  value="${driver.end_province3!""}"/>
                        </td>
                        <td style="width:150px;">
                            <input id="end_city3"  name="end_city3" class="mini-combobox"  emptyText="地市" textField="name"  url="/admin/dict/getArea?id=${driver.end_province3!""}" valueField="id" required="true" value="${driver.end_city3!""}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:100px;">注册电话：</td>
                        <td>
                           ${driver.phone!""}
                        </td>
                         <td style="width:100px;">姓名：</td>
                        <td>
                            <input name="name" class="mini-textbox" required="true"  value="${driver.name!""}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>随车手机：</td>
                        <td>
                            <input name="car_phone" class="mini-textbox" required="true" value="${driver.car_phone!""}"/>
                        </td>
                        <td>身份证：</td>
                        <td>
                            <input name="id_card" class="mini-textbox" required="true" value="${driver.id_card!""}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>车牌号：</td>
                        <td>
                            <input name="plate_number" class="mini-textbox" required="true" value="${driver.plate_number!""}"/>
                        </td>
                        <td>车型：</td>
                        <td>
                        	<input id="car_type" name="car_type" class="mini-combobox" style="" textField="name" valueField="id" emptyText="请选择" url="/admin/dict/getAllCarType" showNullItem="false" value="${driver.car_type!""}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>车长：</td>
                        <td>
                            <input name="car_length"  class="mini-spinner"  minValue="0" maxValue="2000000" required="true" value="${driver.car_length!""}"/>
                        </td>
                        <td>载重：</td>
                        <td>
                            <input name="car_weight" class="mini-spinner"  minValue="0" maxValue="2000000" required="true" value="${driver.car_weight!""}"/>
                        </td>
                    </tr>  
                    <tr>
                        <td>身份证照片：</td>
                        <td>
                        	<input class="mini-hidden" id="id_card_photo" name="id_card_photo" value="${driver.id_card_photo!""}"/>
				          	<img id="img_id_card_photo" src="${driver.id_card_photo!""}" width="50" height="50"/>
				          	<a class="mini-button" onclick="uploadIdCarad();" style="width:80px;margin-right:20px;">选择图片</a>  
                        </td>
                        <td>驾驶证照片：</td>
                        <td>
                        	<input class="mini-hidden" id="license_photo" name="license_photo" value="${driver.license_photo!""}"/>
				          	<img id="img_license_photo" src="${driver.license_photo!""}" width="50" height="50"/>
				          	<a class="mini-button" onclick="uploadLicense();" style="width:80px;margin-right:20px;">选择图片</a>  
                        </td>
                    </tr>
                     <tr>
                        <td>行驶证照片：</td>
                        <td>
                        	<input class="mini-hidden" id="registration_photo" name="registration_photo" value="${driver.registration_photo!""}"/>
				          	<img id="img_registration_photo" src="${driver.registration_photo!""}" width="50" height="50"/>
				          	<a class="mini-button" onclick="uploadRegistration();" style="width:80px;margin-right:20px;">选择图片</a>  
                        </td>
                    </tr> 
                </table>
        		<div style="text-align:center;padding:10px;">               
                    <a class="mini-button" onclick="onOk" style="width:60px;margin-right:20px;">确定</a>       
                    <a class="mini-button" onclick="onCancel" style="width:60px;">取消</a>       
                </div>
            </form>
        </div>
    </div>
</body>
<script>
    mini.parse();
    var form = new mini.Form("form1");

    function SaveData() {
    	var o = GetData();

        form.validate();
        if (form.isValid() == false) return;

        var json = mini.encode([o]);
        $.ajax({
            url: "/admin/driver/update",
            type: "POST",
            data: { data: json },
            cache: false,
            success: function (text) {
            	var o = mini.decode(text);
            	if(!o.success){
            		mini.alert(o.data);
            	}else{
                    CloseWindow("save");
            	}
            },
            error: function (jqXHR, textStatus, errorThrown) {
                mini.alert(jqXHR.responseText);
                CloseWindow();
            }
        });
    }

    function GetData() {
        $("#imgs li").each(function(index,item){
            //console.debug(arguments);
            var url = $(this).find("img").attr("src");
            //console.debug(url);
            mini.get("photo" + (index+1)).setValue(url);
        });
        var o = form.getData();
        //console.debug(o);
        return o;
    }

    function SetData(data){

    }
    
    function CloseWindow(action) {            
        if (action == "close" && form.isChanged()) {
            if (confirm("数据被修改了，是否先保存？")) {
                return false;
            }
        }
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();            
    }
    
    function onOk(e) {
        SaveData();
    }
    
    function onCancel(e) {
        CloseWindow("cancel");
    }


    function hideWindow(){
        areaWindow.hide();
    }

//动态设置url
//    var fileupload = mini.get("fileupload1");
//    fileupload.setUploadUrl("upload.aspx");
    
    function onFileSelect(e) {
        //alert("选择文件");
    }
    function onUploadSuccess(e) {
        var serverData = mini.decode(e.serverData);
        if(serverData.status != 0){
            mini.alert(serverData.message || "上传错误.");
            return;
        }

        //mini.alert("上传成功：" + e.serverData);

        this.setText("");
        var imgurl= serverData.item.url;
        var li = $('<li></li>');
        var img = $('<a rel="venderimg" href="' + imgurl + '" ><img src="' + imgurl + '" style="width:64px;height:64px;" alt="商家图片" /></a>');
        li.append(img);
        var btn = $('<a href="javascript:;">删除</a>');
        li.append(btn);
        btn.click(function(){
            $(this).closest("li").remove();
        });
        $("#imgs").append(li);
        img.fancybox({
            'transitionIn'  : 'elastic',
            'transitionOut' : 'elastic',
            maxWidth: 500,
            maxHeight: 500
        });
    }
    function onUploadError(e) {
        mini.alert("上传失败：" + e.serverData);
    }

    function startUpload() {
        var imgs = $("#imgs");
        if(imgs.find("li").size() == 5){
            mini.alert("最多只能上传5张图片.");
            return;
        }
        var fileupload = mini.get("fileupload1");
        fileupload.startUpload();
    }

	var start_province = mini.get("start_province");
    var start_city = mini.get("start_city");
    function onStartProvinceChanged(e) {
        var id = start_province.getValue();
        start_city.setValue("");
        var url = "/admin/dict/getArea?id=" + id
        start_city.setUrl(url);
        start_city.select(0);
    }
    
	var end_province = mini.get("end_province");
    var end_city = mini.get("end_city");
    function onEndProvinceChanged(e) {
        var id = end_province.getValue();
        end_city.setValue("");
        var url = "/admin/dict/getArea?id=" + id
        end_city.setUrl(url);
        end_city.select(0);
    }
    
	var start_province2 = mini.get("start_province2");
    var start_city2 = mini.get("start_city2");
    function onStartProvinceChanged2(e) {
        var id = start_province2.getValue();
        start_city2.setValue("");
        var url = "/admin/dict/getArea?id=" + id
        start_city2.setUrl(url);
        start_city2.select(0);
    }
    
	var end_province2 = mini.get("end_province2");
    var end_city2 = mini.get("end_city2");
    function onEndProvinceChanged2(e) {
        var id = end_province2.getValue();
        end_city2.setValue("");
        var url = "/admin/dict/getArea?id=" + id
        end_city2.setUrl(url);
        end_city2.select(0);
    }
    
	var start_province3 = mini.get("start_province3");
    var start_city3 = mini.get("start_city3");
    function onStartProvinceChanged3(e) {
        var id = start_province3.getValue();
        start_city3.setValue("");
        var url = "/admin/dict/getArea?id=" + id
        start_city3.setUrl(url);
        start_city3.select(0);
    }
    
	var end_province3 = mini.get("end_province3");
    var end_city3 = mini.get("end_city3");
    function onEndProvinceChanged3(e) {
        var id = end_province3.getValue();
        end_city3.setValue("");
        var url = "/admin/dict/getArea?id=" + id
        end_city3.setUrl(url);
        end_city3.select(0);
    }
        
    function uploadIdCarad(){
		 mini.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '上传中...'
        });
		$.upload({
			// 上传地址
			url: '/service/upload', 
			// 文件域名字
			fileName: 'main_img', 
			// 其他表单数据
			// 上传完成后, 返回json, text
			dataType: 'json',
			// 上传之前回调,return true表示可继续上传
			onSend: function() {
					return true;
			},
			// 上传之后回调
			onComplate: function(serverData) {
				mini.unmask(document.body);
				var data = mini.decode(serverData);
				if(data.status == 0){
					$("#img_id_card_photo").attr("src" ,data.item.url);
					mini.get("id_card_photo").setValue(data.item.url);
				}
				else{
					mini.alert("上传失败，请稍后重试");
				}
			}
		});
	}  
	
	function uploadLicense(){
		 mini.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '上传中...'
        });
		$.upload({
			// 上传地址
			url: '/service/upload', 
			// 文件域名字
			fileName: 'main_img', 
			// 其他表单数据
			// 上传完成后, 返回json, text
			dataType: 'json',
			// 上传之前回调,return true表示可继续上传
			onSend: function() {
					return true;
			},
			// 上传之后回调
			onComplate: function(serverData) {
				mini.unmask(document.body);
				var data = mini.decode(serverData);
				if(data.status == 0){
					$("#img_license_photo").attr("src" ,data.item.url);
					mini.get("license_photo").setValue(data.item.url);
				}
				else{
					mini.alert("上传失败，请稍后重试");
				}
			}
		});
	} 
	 
	function uploadRegistration(){
		 mini.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '上传中...'
        });
		$.upload({
			// 上传地址
			url: '/service/upload', 
			// 文件域名字
			fileName: 'main_img', 
			// 其他表单数据
			// 上传完成后, 返回json, text
			dataType: 'json',
			// 上传之前回调,return true表示可继续上传
			onSend: function() {
					return true;
			},
			// 上传之后回调
			onComplate: function(serverData) {
				mini.unmask(document.body);
				var data = mini.decode(serverData);
				if(data.status == 0){
					$("#img_registration_photo").attr("src" ,data.item.url);
					mini.get("registration_photo").setValue(data.item.url);
				}
				else{
					mini.alert("上传失败，请稍后重试");
				}
			}
		});
	}  
</script>
</html>
