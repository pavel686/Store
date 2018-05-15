package com.cblue.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.home.service.ContentService;
import com.cblue.store.pojo.TbContent;

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/save")
	@ResponseBody
	public ResponseResult addContent(TbContent tbContent){
		 return contentService.addContent(tbContent);
	}

}
