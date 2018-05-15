package com.cblue.store.home.service;

import java.util.List;

import com.cblue.store.common.pojo.EasyUITreeNode;
import com.cblue.store.common.pojo.ResponseResult;

public interface ContentCategoryService {
	
	List<EasyUITreeNode> getAllContentCategory(long parentId);
	
	ResponseResult addContentCategory(long parentId,String name);

}
