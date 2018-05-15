package com.cblue.store.search.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cblue.store.common.pojo.SearchItem;
import com.cblue.store.common.pojo.SearchResult;

@Repository
public class SearchDao {
	
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult query(SolrQuery solrQuery)throws Exception{
		
		 QueryResponse  queryResponse = solrServer.query(solrQuery);
		 SolrDocumentList solrDocumentList = queryResponse.getResults();
		// System.out.println("获得所有的记录数:"+solrDocumentList.getNumFound());
		 SearchResult searchResult = new SearchResult();
		 searchResult.setRecordCount(solrDocumentList.getNumFound());
		 
		 List<SearchItem> itemList = new ArrayList<SearchItem>();
		 for(SolrDocument solrDocument:solrDocumentList){
			 SearchItem searchItem = new SearchItem();
			 searchItem.setId((String)solrDocument.get("id"));
			 searchItem.setTitle((String)solrDocument.get("item_title"));
			 searchItem.setSellponit((String)solrDocument.get("item_sell_point"));
			 //searchItem.setPrice((long)solrDocument.get("item_price"));
			 searchItem.setImage((String)solrDocument.get("item_image"));
			 searchItem.setCategoryname((String)solrDocument.get("item_category_name"));
			 
			 itemList.add(searchItem);
		 }
		 searchResult.setItemList(itemList);
		return searchResult;
		
	}
	
	
	

}
