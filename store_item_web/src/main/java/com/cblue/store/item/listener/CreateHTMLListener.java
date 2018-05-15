package com.cblue.store.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.cblue.store.pojo.TbItem;
import com.cblue.store.pojo.TbItemDesc;
import com.cblue.store.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class CreateHTMLListener implements MessageListener {

	/**
	 * 接收消息，商品的id
	 * 根据商品的id查询商品信息
	 * 使用freemarker生成静态页面
	 */
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FreeMarkerConfigurer freemarkerConfig;
	
	@Value("${HTML_CREATE_PATH}")
	private String HTML_CREATE_PATH;
	
	@Override
	public void onMessage(Message message){
		// TODO Auto-generated method stub
		try {
			//获得商品的id
			TextMessage textMessage = (TextMessage) message;
			long itemId = new Long(textMessage.getText());
			//查询商品的基本信息
			TbItem tbItem = itemService.getItemById(itemId);
			//查询商品的描述
			TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
			//把查询到的数据放到数据源
			Map map  = new HashMap();
			map.put("item", tbItem);
			map.put("itemDesc", tbItemDesc);
			//获得Configuration对象
			Configuration configuration = freemarkerConfig.createConfiguration();
			//获得模板对象
			Template template = configuration.getTemplate("item.fml");
			System.out.println(HTML_CREATE_PATH+File.separator+itemId+".html");
			Writer out = new FileWriter(HTML_CREATE_PATH+File.separator+itemId+".html");
			template.process(map, out);
			out.close();
				
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}

}
