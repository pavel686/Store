package com.cblue.store.item.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Controller
public class CreateHtmlController {

	@Autowired
	private FreeMarkerConfigurer freemarkerConfig;
	
	@RequestMapping("/createhtml")
	@ResponseBody
	public String creatHtml(){
	  try {
		Configuration configuration = freemarkerConfig.createConfiguration();
		//获得模板对象
		Template template = configuration.getTemplate("hello.fm");
		//创建数据集
		Map map = new HashMap();
		map.put("name", "二狗");
        Writer out = new FileWriter("D:/aaa.html");
		template.process(map, out);
		out.close();
		
		return "ok";

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (TemplateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return null;
		
	}
}
