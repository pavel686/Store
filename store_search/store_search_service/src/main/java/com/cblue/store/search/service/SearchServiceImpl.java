package com.cblue.store.search.service;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cblue.search.service.SearchService;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.pojo.SearchItem;
import com.cblue.store.common.pojo.SearchResult;
import com.cblue.store.search.dao.SearchDao;
import com.cblue.store.search.mapper.SearchMapper;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchMapper searchMapper;
	
	@Autowired
	private SolrServer solrServer;
	
	@Autowired
	private SearchDao searchDao;
	
	@Override
	public ResponseResult importAllSearchItem() throws Exception{
		// TODO Auto-generated method stub
	
			List<SearchItem> searchItems = searchMapper.getSearchItemList();
			//导入索引库
			for(SearchItem searchItem:searchItems){
				 //solr的文档流对象
				 SolrInputDocument document = new SolrInputDocument();
				 document.addField("id", searchItem.getId()); //在索引库中，我们的id都是String类型
				 document.addField("item_title",searchItem.getTitle());
				 document.addField("item_sell_point", searchItem.getSellponit());
				 document.addField("item_price", searchItem.getPrice());
				 document.addField("item_image", searchItem.getImage());
				 document.addField("item_category_name", searchItem.getCategoryname());
				 
				 //把数据保存到索引库
				 solrServer.add(document);
				 solrServer.commit(); 
			}
			return ResponseResult.ok();
	}

	/**
	 * @param page 是当前页
	 */
	@Override
	public SearchResult search(String keyword, int page, int rows) {
		// TODO Auto-generated method stub
		try {
			SolrQuery solrQuery = new SolrQuery();
			//设置搜索关键字
			solrQuery.setQuery(keyword);
			//设置分页
			if(page<=0) page=1;
			solrQuery.setStart((page-1)*rows); 
			solrQuery.setRows(rows);
			
			//设置搜索域
			 solrQuery.set("df","item_title");//搜索域
			 
			 SearchResult searchResult = searchDao.query(solrQuery);
			 //总页数设置值
			 long searchCount = searchResult.getRecordCount();
			 int totalPages = (int)(searchCount/rows);
			 if(searchCount%rows>0){ //不能整除
				 totalPages++;
			 }
			 searchResult.setTotalPages(totalPages);
			 
			 return searchResult;
			 
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
		
		
	}

}
