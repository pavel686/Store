package com.cblue.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cblue.store.common.pojo.EasyUITreeNode;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.home.service.ContentCategoryService;

@Controller
public class ContentCatController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> contentCatoryList(@RequestParam(name="id",defaultValue="0") long parentId){
		List<EasyUITreeNode>  list = contentCategoryService.getAllContentCategory(parentId);
		return list;
	}
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public ResponseResult addContentCatory(long parentId,String name){
		return contentCategoryService.addContentCategory(parentId, name);
	}
	
	

}
