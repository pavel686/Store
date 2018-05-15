package com.cblue.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrj {

	 @Test
	 public void add()throws Exception{
		 
		 SolrServer solrServer = new HttpSolrServer("http://192.168.0.137:8010/solr/collection1");
		 //solr的文档流对象
		 SolrInputDocument document = new SolrInputDocument();
		 document.addField("id", "doc1"); //在索引库中，我们的id都是String类型
		 document.addField("item_title", "mytitle");
		 document.addField("item_price", 1000);
		 
		 //把数据保存到索引库
		 solrServer.add(document);
		 solrServer.commit(); 
		 
	 }
	 
	 //更新和添加是一样的。saveOrUpdate
	 
	 @Test
	 public void delete()throws Exception{
		 SolrServer solrServer = new HttpSolrServer("http://192.168.0.137:8010/solr/collection1");
		 //solrServer.deleteById("doc1");
		 solrServer.deleteByQuery("*:*");
		 solrServer.commit();
	 }
	 
	 
	 //实现简单的搜索功能
	 @Test
	 public void select01()throws Exception{
		 SolrServer solrServer = new HttpSolrServer("http://192.168.0.137:8010/solr/collection1");
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
	 
	 //稍微复杂的搜索
	 @Test
	 public void select02()throws Exception{
		 SolrServer solrServer = new HttpSolrServer("http://192.168.0.137:8010/solr/collection1");
		 SolrQuery solrQuery = new SolrQuery();
		 solrQuery.setQuery("手机");
		 solrQuery.setStart(0);
		 solrQuery.setRows(20);
		 solrQuery.set("df", "item_keywords");//搜索域
		 solrQuery.setHighlight(true);//hightlight 高亮
		 solrQuery.addHighlightField("item_title");  //设置需要高亮的字段
		 solrQuery.setHighlightSimplePre("<font color='red'>"); //设置高亮的前缀
		 solrQuery.setHighlightSimplePost("</font>");//设置高亮的后缀
		 QueryResponse  queryResponse = solrServer.query(solrQuery);
		 SolrDocumentList solrDocumentList = queryResponse.getResults();
		 System.out.println("获得所有的记录数:"+solrDocumentList.getNumFound());
		 
		 //遍历内容
		    //获取高亮内容
		 Map<String,Map<String,List<String>>> hightlight = queryResponse.getHighlighting();
		 //小米4 白色 联通3G<font color='red'>手机</font>
		 
		 for(SolrDocument solrDocument:solrDocumentList){
			 System.out.println(solrDocument.getFieldValues("id"));
			 
			 List<String> h = hightlight.get(solrDocument.get("id")).get("item_title");
			 String title = null;
			 if(h.size()>0&&h!=null){
				 title = h.get(0);
			 }else{
				 title =(String) solrDocument.get("item_title");
			 }
			 System.out.println(title);
			 
			 //System.out.println(solrDocument.getFieldValues("item_title"));
			 System.out.println(solrDocument.getFieldValues("item_price"));
			 
		 }
		 
		 
		 
		 
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
