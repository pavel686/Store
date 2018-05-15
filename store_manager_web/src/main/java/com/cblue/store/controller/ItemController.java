package com.cblue.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cblue.store.common.pojo.EasyUIDataGrid;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.pojo.TbItem;
import com.cblue.store.service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
		
	@RequestMapping("/item/{itemId}")//restful写法  http://localhost:8080/web/item/1000
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		TbItem item = itemService.getItemById(itemId);
		return item;
	}
	
	@RequestMapping("/item/list")//restful写法  http://localhost:8080/web/item/1000
	@ResponseBody
	public EasyUIDataGrid getItemList(Integer page,Integer rows){
		   return itemService.getItemListByPage(page, rows);
	}
	
	@RequestMapping("/item/save")
	@ResponseBody
	public ResponseResult addItem(TbItem tbItem,String desc){
		  return itemService.addItem(tbItem, desc);
	}

}
