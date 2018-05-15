package com.cblue.store.home.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cblue.store.home.service.ContentService;
import com.cblue.store.pojo.TbContent;

@Controller
public class HomeController {
	
	@Autowired
	private ContentService contentService;
	
	@Value("${BIG_AD_ID}")
    private long BIG_AD_ID;
	
	@RequestMapping("index")
	public String goIndex(Model model){
		
		List<TbContent> list = contentService.getContentByCategoryId(BIG_AD_ID);
		model.addAttribute("ad1List", list);
		
		return "index";
	}

}
