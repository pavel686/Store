package com.cblue.store.seach.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.cblue.store.common.pojo.SearchItem;
import com.cblue.store.search.mapper.SearchMapper;

public class AddItemMessageListener implements MessageListener {
	
	@Autowired
	private SearchMapper searchMapper;
	
	@Autowired
	private SolrServer solrServer;

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			//获得商品的id
			TextMessage textMessage = (TextMessage) message;
			long itemId = new Long(textMessage.getText());
			
			//休息10毫秒
			Thread.sleep(10);
			//根据id获得SearchItem对象
			SearchItem searchItem = searchMapper.getSearchItemById(itemId);
			
			//把SearchItem对象保存到索引库
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
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
