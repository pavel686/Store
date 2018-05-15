package com.cblue.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
	
	
	@Test
	public void add()throws Exception{
		//创建一个solrServer对象
		CloudSolrServer solrServer = new CloudSolrServer("192.168.0.137:2185,192.168.0.137:2186,192.168.0.137:2187");
		//设置一个默认的搜索库
		solrServer.setDefaultCollection("collection2");
		 //solr的文档流对象
		 SolrInputDocument document = new SolrInputDocument();
		 document.addField("id", "doc1"); //在索引库中，我们的id都是String类型
		 document.addField("item_title", "mytitle");
		 document.addField("item_price", 1000);
		 
		 //把数据保存到索引库
		 solrServer.add(document);
		 solrServer.commit(); 
		
	}
	
	 //实现简单的搜索功能
	 @Test
	 public void select01()throws Exception{
		//创建一个solrServer对象
		CloudSolrServer solrServer = new CloudSolrServer("192.168.0.137:2185,192.168.0.137:2186,192.168.0.137:2187");
		//设置一个默认的搜索库
		solrServer.setDefaultCollection("collection2");
		 SolrQuery solrQuery = new SolrQuery();
		 solrQuery.setQuery("*:*"); //查询所有
		 QueryResponse  queryResponse = solrServer.query(solrQuery);
		 SolrDocumentList solrDocumentList = queryResponse.getResults();
		 System.out.println("获得所有的记录数:"+solrDocumentList.getNumFound());
		 for(SolrDocument solrDocument:solrDocumentList){
			 System.out.println(solrDocument.getFieldValues("id"));
			 System.out.println(solrDocument.getFieldValues("item_title"));
			 System.out.println(solrDocument.getFieldValues("item_price"));
			 
		 }
	 }

}
