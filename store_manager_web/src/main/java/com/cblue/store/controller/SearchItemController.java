package com.cblue.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cblue.search.service.SearchService;
import com.cblue.store.common.pojo.ResponseResult;

@Controller
public class SearchItemController {

	@Autowired
	private SearchService searchService;
	
	@RequestMapping("index/item/import")
	@ResponseBody
	public ResponseResult importAllItem() throws Exception{
		ResponseResult responseResult = searchService.importAllSearchItem();
		return responseResult;
	}
}
