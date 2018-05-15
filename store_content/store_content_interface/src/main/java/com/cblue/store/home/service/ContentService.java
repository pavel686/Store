package com.cblue.store.home.service;

import java.util.List;

import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.pojo.TbContent;

public interface ContentService {
	
	
	ResponseResult addContent(TbContent tbContent);
	//categoryId=89
	List<TbContent> getContentByCategoryId(long categoryId);

}
