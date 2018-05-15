package com.cblue.store.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cblue.search.service.SearchService;
import com.cblue.store.common.pojo.SearchResult;

@Controller
public class SearchController {

	
	@Autowired
	private SearchService searchService;
	
	@RequestMapping("/search")
	public String searchItem(String keyword,@RequestParam(defaultValue="1") Integer page,Model model){
		  try {
			keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		   System.out.println("keyword="+keyword);
		   
		  /* int i= 1/0;*/
		   
		  SearchResult searchResult = searchService.search(keyword, page, 20);
		  
		  //把执行的结果传递给页面
		  model.addAttribute("query", keyword);
		  model.addAttribute("totalPages", searchResult.getTotalPages());
		  model.addAttribute("page", page);
		  model.addAttribute("recordCount", searchResult.getRecordCount());
		  model.addAttribute("itemList", searchResult.getItemList());
		  System.out.println(searchResult.getItemList().get(0).getImage());
		  
		  return "search";
		  
	}
	
	
	
	
	
	
	
	
	
	
}
