/**
 * @filename AdController.java
 */
package com.maogousoft.wuliu.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.domain.Ad;
import com.maogousoft.wuliu.service.FileInfo;
import com.maogousoft.wuliu.service.ImageService;

/**
 * @description 广告位
 * @author shevliu
 * @email shevliu@gmail.com
 * May 18, 2013 11:58:08 PM
 */
public class AdController extends BaseController{

	private static final Log log = LogFactory.getLog(AdController.class);

	public void index(){
		render("ad.ftl");
	}

	public void list(){
		String from = "from logistics_ad where status=0 order by id desc" ;
		Page<Ad> page = Ad.dao.paginate(getPageIndex(), getPageSize(),
				"select * ", from );
		renderText(JSONUtils.toPagedGridJSONString(page, "id|ad_location|ad_title|ad_img|ad_link|create_time"));
	}

	public void add(){
		render("ad_add.ftl");
	}

	public void save(){
		try{
			UploadFile uf = getFile("ad_img");
			FileInfo fileInfo = ImageService.saveFile(uf.getFileName(), uf.getFile());
			String ad_location = getPara("ad_location");
			String ad_title = getPara("ad_title");
			String ad_link = getPara("ad_link");
			String ad_img = fileInfo.getVirtualUrl();
			Ad ad = new Ad();
			ad.set("ad_location", ad_location);
			ad.set("ad_title", ad_title);
			ad.set("ad_link", ad_link);
			ad.set("ad_img", ad_img);
			ad.set("create_time",  new Date());
			ad.set("status", 0);
			ad.save();
			renderHtml("发布广告位成功");
		}catch(RuntimeException re){
			log.error("发布广告位失败" , re);
			renderHtml("发布广告位失败");
		}
	}

	public void edit(){
		int id = getParaToInt();
		Ad ad = Ad.dao.findById(id);
		setAttr("ad", ad);
		render("ad_edit.ftl");
	}

	public void update(){
		try{
			UploadFile uf = getFile("ad_img");
			int id = getParaToInt("id");
			String ad_location = getPara("ad_location");
			String ad_title = getPara("ad_title");
			String ad_link = getPara("ad_link");

			Ad ad = Ad.dao.findById(id);
			if(uf != null){
				FileInfo fileInfo = ImageService.saveFile(uf.getFileName(), uf.getFile());
				String ad_img = fileInfo.getVirtualUrl();
				ad.set("ad_img", ad_img);
			}
			else{
				ad.remove("ad_img");
			}
			ad.set("ad_location", ad_location);
			ad.set("ad_title", ad_title);
			ad.set("ad_link", ad_link);
			ad.update();
			renderHtml("修改广告位成功");
		}catch(RuntimeException re){
			log.error("修改广告位失败" , re);
			renderHtml("修改广告位失败");
		}
	}

	public void delete(){
		int id = getParaToInt();
		Db.update("update logistics_ad set status = -1 where id = ? " , id);
	}
}
