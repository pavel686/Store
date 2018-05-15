package com.cblue.store.service;

import java.util.List;

import com.cblue.store.common.pojo.EasyUITreeNode;
import com.cblue.store.pojo.TbItemCat;

public interface ItemCatService {
	
	List<EasyUITreeNode> getItemCatList(Long parentId);
	

}
