package com.cblue.store.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cblue.store.pojo.TbItem;
import com.cblue.store.pojo.TbItemCat;
import com.cblue.store.pojo.TbItemDesc;
import com.cblue.store.service.ItemCatService;
import com.cblue.store.service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("item/{itemId}")
	public String showItem(@PathVariable Long itemId,Model model){
		//查询商品信息
		TbItem tbItem = itemService.getItemById(itemId);
		
		//查询商品描述信息
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
		
		//把这些信息保存到Model
		model.addAttribute("item", tbItem);
		model.addAttribute("itemDesc", tbItemDesc);
		
		//返回到显示页面
		return "item";
		
	}

}
