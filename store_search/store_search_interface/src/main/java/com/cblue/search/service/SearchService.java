package com.cblue.search.service;

import java.util.List;

import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.pojo.SearchItem;
import com.cblue.store.common.pojo.SearchResult;

public interface SearchService {

	 //把从数据库中导入所需要的数据添加到索引库中
	 ResponseResult  importAllSearchItem() throws Exception;
	 
	 //根据关键字搜索
	 SearchResult search(String keyword,int page,int rows);
	 
	 

}
