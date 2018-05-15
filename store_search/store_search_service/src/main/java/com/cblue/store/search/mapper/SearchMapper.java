package com.cblue.store.search.mapper;

import java.util.List;

import com.cblue.store.common.pojo.SearchItem;

public interface SearchMapper {

	public List<SearchItem> getSearchItemList();
	
	public SearchItem getSearchItemById(long itemId);
}
