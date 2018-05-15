package com.cblue.store.service;

import com.cblue.store.common.pojo.EasyUIDataGrid;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.pojo.TbItem;
import com.cblue.store.pojo.TbItemDesc;

public interface ItemService {
	
	TbItem getItemById(Long id);
	
	EasyUIDataGrid getItemListByPage(int currentPage,int pageSize);
	
	ResponseResult addItem(TbItem tbItem,String desc);
	
	TbItemDesc getItemDescById(Long id);
}
