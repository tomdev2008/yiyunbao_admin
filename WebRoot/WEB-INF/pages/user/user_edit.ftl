<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
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
        <div title="货主信息" showCloseButton="false">
            <form id="form1" method="post">
            	<input id="id" name="id" class="mini-hidden" value="${user.id!}"/>
            	<table style="table-layout:fixed;">
                    <tr>
                        <td style="width:100px;"><font color="red">*</font>手机号：</td>
                        <td>
                            ${user.phone!""}
                        </td>
                    </tr>
                    <tr>
                        <td><font color="red">*</font>公司名称：</td>
                        <td>
                            <input name="company_name" class="mini-textbox" required="true"  value="${user.company_name!""}"/>
                        </td>
                        <td><font color="red">*</font>联系人：</td>
                        <td>
                            <input name="name" class="mini-textbox" required="true" value="${user.name!""}" />
                        </td>
                    </tr>
                    <tr>
                        <td>推荐人：</td>
                        <td>
                        	${user.recommender!""}
                        </td>
                        <td>身份证号码：</td>
                        <td>
                            <input name="id_card" class="mini-textbox" value="${user.id_card!""}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>座机：</td>
                        <td>
                            <input name="telcom" class="mini-textbox"  value="${user.telcom!""}"/>
                        </td>
                        <td>地址：</td>
                        <td>
                            <input name="address" class="mini-textbox"  value="${user.address!""}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>营业执照：</td>
                        <td>
                        	<input class="mini-hidden" id="licence_photo" name="licence_photo" value="${user.licence_photo!""}"/>
				          	<img id="main_img_path" src="${user.licence_photo!""}" width="50" height="50"/>
				          	<a class="mini-button" onclick="upload();" style="width:80px;margin-right:20px;">选择图片</a>  
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
    var areaWindow = mini.get("areaWindow");

    function SaveData() {
    	var o = GetData();

        form.validate();
        if (form.isValid() == false) return;

        var json = mini.encode([o]);
        $.ajax({
            url: "/admin/user/update",
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

    //设置地区默认值
    function setDefaultAreaId(){
    	if(mini.get("vender_province_id").getValue() == ""){
        	mini.get("vender_province_id").setValue("-1");
    	}
    	if(mini.get("vender_city_id").getValue() == ""){
        	mini.get("vender_city_id").setValue("-1");
    	}
    	if(mini.get("vender_district_id").getValue() == ""){
        	mini.get("vender_district_id").setValue("-1");
    	}
    }

    //清除所选地区
    function clearArea(){
        //hideWindow();
        mini.get("vender_province").setValue("");
        mini.get("vender_province_id").setValue("");
        mini.get("vender_city").setValue("");
        mini.get("vender_city_id").setValue("");
        mini.get("vender_district").setValue("");
        mini.get("vender_district_id").setValue("");
    }

    /**开始地点*/
    function onSelectProvince(){
        areaWindow.showAtEl(document.getElementById("vender_province"), {
            xAlign: "center",
            yAlign: "below"
        });
        var el = areaWindow.getBodyEl();
        var ul = $(el).find("ul");
        $.ajax({
            url: "/dict/getProvinceList",
            dataType: "json",
            success: function(json){
                ul.empty();
                var str = [];
                for(var i = 0; i < json.length; i++){
                    var province = json[i];
                    str.push('<li><a class="mini-button vender_province_btn" style="width:70px;" href="javascript:;" area_id="' + province.id + '"><span class="mini-button-text">' + province.short_name + '</span></a></li>');
                }
                ul.append(str.join("\n"));
            }
        });
    }

    function onSelectCity(){
        var province_id = mini.get("vender_province_id").getValue() || 0;
        if(!province_id){
            onSelectStartProvince();
            return;
        }
        areaWindow.showAtEl(document.getElementById("vender_city"), {
            xAlign: "center",
            yAlign: "below"
        });
        var el = areaWindow.getBodyEl();
        var ul = $(el).find("ul");
        $.ajax({
            url: "/dict/getCityList",
            dataType: "json",
            data: {province_id: province_id},
            success: function(json){
                ul.empty();
                var str = [];
                for(var i = 0; i < json.length; i++){
                    var area = json[i];
                    str.push('<li><a class="mini-button vender_city_btn" href="javascript:;" area_id="' + area.id + '"><span class="mini-button-text">' + area.short_name + '</span></a></li>');
                }
                ul.append(str.join("\n"));
            }
        });
    }

    function onSelectDistrict(){
        var city_id = mini.get("vender_city_id").getValue() || 0;
        if(!city_id){
            onSelectStartCity();
            return;
        }
        areaWindow.showAtEl(document.getElementById("vender_district"), {
            xAlign: "center",
            yAlign: "below"
        });
        var el = areaWindow.getBodyEl();
        var ul = $(el).find("ul");
        $.ajax({
            url: "/dict/getDistrictList",
            dataType: "json",
            data: {city_id: city_id},
            success: function(json){
                ul.empty();
                if(json.length == 0){
                    areaWindow.hide();
                    mini.get("vender_district").setValue("");
                    mini.get("vender_district_id").setValue("");
                    return;
                }
                var str = [];
                for(var i = 0; i < json.length; i++){
                    var area = json[i];
                    str.push('<li><a class="mini-button vender_district_btn" href="javascript:;" area_id="' + area.id + '"><span class="mini-button-text">' + area.short_name + '</span></a></li>');
                }
                ul.append(str.join("\n"));
            }
        });
    }
    
    


    $(function(){
        $(".vender_province_btn").live("click",function(){
            var text = $(this).text();
            var area_id = $(this).attr("area_id");
            mini.get("vender_province").setValue(text);
            mini.get("vender_province_id").setValue(area_id);
            setTimeout(function(){
                onSelectCity();
            },100);
        });

        $(".vender_city_btn").live("click",function(){
            var text = $(this).text();
            var area_id = $(this).attr("area_id");
            mini.get("vender_city").setValue(text);
            mini.get("vender_city_id").setValue(area_id);
            setTimeout(function(){
                onSelectDistrict();
            },100);
        });

        $(".vender_district_btn").live("click",function(){
            var text = $(this).text();
            var area_id = $(this).attr("area_id");
            mini.get("vender_district").setValue(text);
            mini.get("vender_district_id").setValue(area_id);
            areaWindow.hide();
        });
        
        
	    
    });

	function upload(){
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
					$("#main_img_path").attr("src" ,data.item.url);
					mini.get("licence_photo").setValue(data.item.url);
				}
				else{
					mini.alert("上传失败，请稍后重试");
				}
			}
		});
	}

</script>
</html>
