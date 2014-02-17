package com.maogousoft.wuliu.service;

import java.io.File;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.maogousoft.wuliu.common.BaseConfig;
import com.maogousoft.wuliu.common.utils.TimeUtil;

/**
 * @author yangfan(kenny0x00@gmail.com) 2013-5-1 下午3:15:55
 */
public class ImageService {

	private static final Logger log = Logger.getLogger(ImageService.class);

	private static final AtomicLong counter = new AtomicLong(0);

	public static FileInfo saveFile(String fileName, File file) {
		//获取上传的根目录
		String uploadBasePath = BaseConfig.me().getProperty("upload.basepath");
		String uploadVirtualUrlRoot = BaseConfig.me().getProperty("upload.baseurl");

		return saveFileInternal(file, uploadBasePath, uploadVirtualUrlRoot);
	}

	private static FileInfo saveFileInternal(File file, String basepath, String baseurl) {
		//获取当前上传的目录
		final String uploadDirName = TimeUtil.format(new Date(), "yyyy/MM/dd");

		//创建上传目录
		File uploadDir = new File(FilenameUtils.concat(basepath, uploadDirName));
		if(!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		//文件名
		String filenameSuffix = TimeUtil.format(new Date(), "yyyyMMdd_HHmmss") + "_" + counter.getAndIncrement();
		String fileExt = FilenameUtils.getExtension(file.getName());
		final String fileName = filenameSuffix + "." + fileExt;

		String filePath = FilenameUtils.concat(uploadDir.getAbsolutePath(), fileName);
		log.debug("save temp file to " + filePath);
		file.renameTo(new File(filePath));

		//文件信息
		FileInfo info = new FileInfo();
		String virtualUrl = baseurl + "/" + uploadDirName + "/" + fileName;
		info.setVirtualUrl(virtualUrl.replaceAll("\\\\", "/"));
		info.setFilename(fileName);
		return info;
	}

}
