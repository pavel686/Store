package com.cblue.store.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cblue.store.common.util.FastDFSClient;
import com.cblue.store.common.util.JsonUtils;

@Controller
public class UploadImageController {
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	//设定我们的内容的返回类型是文本类型
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadImage(MultipartFile uploadFile){
		//实现上传
		try {
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			
			//获得图片名字
			String fileName = uploadFile.getOriginalFilename();
			//获得后缀 abc.jpg
			String extName = fileName.substring(fileName.indexOf(".")+1);
			//上传图片
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(),extName);
			
			String allurl = IMAGE_SERVER_URL+url;
			
			System.out.println("allurl="+allurl);
			
			Map map = new HashMap();
			map.put("error", 0);
			map.put("url", allurl);
			
			return JsonUtils.objectToJson(map);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Map map = new HashMap();
			map.put("error", 1);
			map.put("message", "图片上传失败");
			return JsonUtils.objectToJson(map);
		}
		
	}

}
