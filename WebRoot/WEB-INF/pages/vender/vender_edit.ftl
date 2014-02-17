<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?key=&v=1.4&services=true"></script>
    <script src="/admin/js/swfupload/swfupload.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/js/fancybox/jquery.fancybox.css" type="text/css" media="screen" />
    <script type="text/javascript" src="/js/fancybox/jquery.fancybox.pack.js"></script>
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
        <div title="商家信息" showCloseButton="false">
            <form id="form1" method="post">
                <input id="id" name="id" class="mini-hidden" value="${vender.id!}"/>
            	<table style="table-layout:fixed;">
                    <tr>
                        <td style="width:70px;">地区
                        </td>
                        <td style="width:150px;">
                            <input id="vender_province" class="mini-textbox" onfocus="onSelectProvince" emptyText="省/直辖市" required="false" value="${vender.vender_province_name!}"/>
                            <input id="vender_province_id" name="vender_province" class="mini-hidden" value="${vender.vender_province!}"/>
                        </td>
                        <td style="width:150px;">
                            <input id="vender_city" class="mini-textbox" onfocus="onSelectCity" emptyText="地市"  value="${vender.vender_city_name!}"/>
                            <input id="vender_city_id" name="vender_city" class="mini-hidden" value="${vender.vender_city!}"/>
                        </td>
                        <td style="width:150px;">
                            <input id="vender_district" class="mini-textbox" onfocus="onSelectDistrict" emptyText="区县" value="${vender.vender_district_name!}"/>
                            <input id="vender_district_id" name="vender_district" class="mini-hidden" value="${vender.vender_district!}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:100px;"><font color="red">*</font>商家名称：</td>
                        <td>
                            <input name="vender_name" class="mini-textbox" required="true" value="${vender.vender_name!}"/>
                        </td>
                        <td style="width:100px;"><font color="red">*</font>商品名称：</td>
                        <td>
                            <input name="goods_name" class="mini-textbox" required="true" value="${vender.goods_name!}"/>
                        </td>
                    </tr>
                    <tr>
                        <td><font color="red">*</font>商家地址：</td>
                        <td >
                            <input name="vender_address" class="mini-textbox" required="true" value="${vender.vender_address!}"/>
                        </td>
                        <td><font color="red">*</font>联系人：</td>
                        <td>
                            <input name="contact" class="mini-textbox" required="true" value="${vender.contact!}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>联系手机：</td>
                        <td>
                            <input name="vender_mobile" class="mini-textbox" required="false" value="${vender.vender_mobile!}"/>
                        </td>
                        <td><font color="red">*</font>联系电话：</td>
                        <td>
                            <input name="vender_phone" class="mini-textbox" required="true" value="${vender.vender_phone!}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>正常价格：</td>
                        <td>
                            <input name="normal_price"  class="mini-spinner"  minValue="0" maxValue="2000000" required="true" value="${vender.normal_price!}"/>
                        </td>
                        <td>会员特惠：</td>
                        <td>
                            <input name="member_price" class="mini-spinner"  minValue="0" maxValue="2000000" required="true" value="${vender.member_price!}"/>
                        </td>
                        <input class="mini-hidden" id="longitude" name="longitude" value="${vender.longitude!"116.403119"}">
    					<input class="mini-hidden" id="latitude" name="latitude" value="${vender.latitude!"39.915156"}">
                    </tr>   
                    <tr>
                        <td>分类：</td>
                        <td>
                            <input id="category" name="category" class="mini-combobox" style="" textField="name" valueField="id" emptyText="请选择类别" url="/admin/dict/getListByType/vender_category" showNullItem="false" value="${vender.category!}"/>
                        </td>
                        <td>其他：</td>
                        <td>
                            <input name="other"  class="mini-textbox"  required="true" value="${vender.other!}"/>
                        </td>
                    </tr>   
                </table>
                <!--地图-->
                <div id="mapContainer" style="width:100%;height:340px;"></div>
                <input id="photo1" name="photo1" class="mini-hidden"/>
                <input id="photo2" name="photo2" class="mini-hidden"/>
                <input id="photo3" name="photo3" class="mini-hidden"/>
                <input id="photo4" name="photo4" class="mini-hidden"/>
                <input id="photo5" name="photo5" class="mini-hidden"/>
        		<div style="text-align:center;padding:10px;">               
                    <a class="mini-button" onclick="onOk" style="width:60px;margin-right:20px;">确定</a>       
                    <a class="mini-button" onclick="onCancel" style="width:60px;">取消</a>       
                </div>  
            </form>
        </div>
        <div title="商家图片" showCloseButton="false">
            <input id="fileupload1" class="mini-fileupload" name="Fdata" limitType="*.jpg;*.png;*.gif" 
                flashUrl="/admin/js/swfupload/swfupload.swf"
                uploadUrl="/service/upload/"
                onuploadsuccess="onUploadSuccess"
                onuploaderror="onUploadError" onfileselect="onFileSelect"
            /><input type="button" value="上传" onclick="startUpload()"/>
            <br/>
            <ul id="imgs">
                <#if vender.photo1?has_content>
                <li><a class="fancybox" rel="venderimg" href="${vender.photo1}" ><img src="${vender.photo1}" style="width:64px;height:64px;" alt="商家照片" /></a><a href="javascript:;" class="btnRemoveImg">删除</a>
                </li>
                </#if>
                <#if vender.photo2?has_content>
                <li><a class="fancybox" rel="venderimg" href="${vender.photo2}" ><img src="${vender.photo2}" style="width:64px;height:64px;" alt="商家照片" /></a><a href="javascript:;" class="btnRemoveImg">删除</a>
                </li>
                </#if>
                <#if vender.photo3?has_content>
                <li><a class="fancybox" rel="venderimg" href="${vender.photo3}" ><img src="${vender.photo3}" style="width:64px;height:64px;" alt="商家照片" /></a><a href="javascript:;" class="btnRemoveImg">删除</a>
                </li>
                </#if>
                <#if vender.photo4?has_content>
                <li><a class="fancybox" rel="venderimg" href="${vender.photo4}" ><img src="${vender.photo4}" style="width:64px;height:64px;" alt="商家照片" /></a><a href="javascript:;" class="btnRemoveImg">删除</a>
                </li>
                </#if>
                <#if vender.photo5?has_content>
                <li><a class="fancybox" rel="venderimg" href="${vender.photo5}" ><img src="${vender.photo5}" style="width:64px;height:64px;" alt="商家照片" /></a><a href="javascript:;" class="btnRemoveImg">删除</a>
                </li>
                </#if>
            </ul>
        </div>
    </div>
    <div id="areaWindow" class="mini-window" title="地区选择" style="width:430px;height:320px;" showFooter="true" 
    showModal="true" allowResize="false" allowDrag="false">
        <ul class="area">
        </ul>
        <div property="footer" style="text-align:right;padding:5px;padding-right:15px;">
            <input type="button" value='清除选择' onclick="clearArea()" style='vertical-align:middle;'/>
            <input type='button' value='关闭' onclick="hideWindow()" style='vertical-align:middle;'/>
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
            url: "/admin/vender/update",
            type: "POST",
            data: { data: json },
            cache: false,
            success: function (text) {
            	var o = mini.decode(text);
            	if(!o.success){
            		mini.alert(o.data);
            	}else{
                    CloseWindow("update");
            	}
            },
            error: function (jqXHR, textStatus, errorThrown) {
                mini.alert(jqXHR.responseText);
                CloseWindow();
            }
        });
    }

    function GetData() {
    	setDefaultAreaId();
        $("#imgs li").each(function(index,item){
            console.debug(arguments);
            var url = $(this).find("img").attr("src");
            console.debug(url);
            mini.get("photo" + (index+1)).setValue(url);
        });
        var o = form.getData();
        console.debug(o);
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
    
    
    //创建和初始化地图函数：
    function initMap(){
        createMap();//创建地图
        setMapEvent();//设置地图事件
        addMapControl();//向地图添加控件
    }

    //创建地图函数：
    function createMap(){
        var map = new BMap.Map("mapContainer");//在百度地图容器中创建一个地图
        window.map = map;//将map变量存储在全局
		var point = new BMap.Point(100,20);//定义一个中心点坐标 
        map.centerAndZoom(point,15);//设定地图的中心点和坐标并将地图显示在地图容器中
       
    }
    //地图事件设置函数：
    function setMapEvent(){
        map.enableDragging();//启用地图拖拽事件，默认启用(可不写)
        map.enableScrollWheelZoom();//启用地图滚轮放大缩小
        map.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写)
        map.enableKeyboard();//启用键盘上下左右键移动地图
    }

    //地图控件添加函数：
    function addMapControl(){
	    //向地图中添加缩放控件
		var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_LEFT,type:BMAP_NAVIGATION_CONTROL_LARGE});
		map.addControl(ctrl_nav);
	    //向地图中添加缩略图控件
		var ctrl_ove = new BMap.OverviewMapControl({anchor:BMAP_ANCHOR_BOTTOM_RIGHT,isOpen:1});
		//map.addControl(ctrl_ove);
	    //向地图中添加比例尺控件
		var ctrl_sca = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT});
		map.addControl(ctrl_sca);
    }

      //初始化标点
    function initAllMarker(){
    	//	map.clearOverlays();
	    	var point = new BMap.Point(${vender.longitude!"116.403119"}, ${vender.latitude!"39.915156"});
			//创建一个标点
			var marker = new BMap.Marker(point, {
    			enableDragging: true,
   			 	raiseOnDrag: true
			});
			marker.addEventListener('dragend', function(e){
				mini.get("longitude").setValue(e.point.lng);
				mini.get("latitude").setValue(e.point.lat);
			})

			 map.centerAndZoom(point,15);
			//在地图上打点
			map.addOverlay(marker);
			//marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
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
        
        
	

    	initMap();
	    initAllMarker();

        //初始化商家图片
        $(".fancybox").fancybox({
            maxWidth: 500,
            maxHeight: 500
        });
        $(".btnRemoveImg").click(function(){
            $(this).closest("li").remove();
        });
	    
    });

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

</script>
</html>
