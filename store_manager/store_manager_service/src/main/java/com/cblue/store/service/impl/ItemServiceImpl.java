package com.cblue.store.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.cblue.store.common.jedis.JedisClientPool;
import com.cblue.store.common.pojo.EasyUIDataGrid;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.util.IDUtils;
import com.cblue.store.common.util.JsonUtils;
import com.cblue.store.mapper.TbItemDescMapper;
import com.cblue.store.mapper.TbItemMapper;
import com.cblue.store.pojo.TbItem;
import com.cblue.store.pojo.TbItemDesc;
import com.cblue.store.pojo.TbItemExample;
import com.cblue.store.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapper;

	@Autowired
	private TbItemDescMapper tbItemDescMapper;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Resource
	private Destination queueDestination;
	
	@Resource
	private Destination topicDestination;

	@Autowired
	private JedisClientPool jedisClientPool;

	@Value("${ITEM_REDIS_BASE}")
	private String ITEM_REDIS_BASE;

	@Value("${ITEM_REDIS_EXPRIE}")
	private int ITEM_REDIS_EXPRIE;

	@Override
	public TbItem getItemById(Long id) {
		// TODO Auto-generated method stub
		// 查询缓存
		try {
			String jsonStr = jedisClientPool.get(ITEM_REDIS_BASE + id);
			if (StringUtils.isNoneBlank(jsonStr)) {
				TbItem tbItem = JsonUtils.jsonToPojo(jsonStr, TbItem.class);
				if (tbItem != null) {
					return tbItem;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
		// 添加缓存
		try {
			// REDIS_ITEM_PRE:BASE:1231490 json格式对象
			jedisClientPool.set(ITEM_REDIS_BASE + id,
					JsonUtils.objectToJson(tbItem));
			jedisClientPool.expire(ITEM_REDIS_BASE + id, ITEM_REDIS_EXPRIE);
		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
		}
		return tbItem;
	}

	@Override
	public TbItemDesc getItemDescById(Long id) {
		// TODO Auto-generated method stub
		// 查询缓存

		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);

		// 添加缓存

		return tbItemDesc;

	}

	// 实现分页
	@Override
	public EasyUIDataGrid getItemListByPage(int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(currentPage, pageSize);

		TbItemExample tbItemExample = new TbItemExample();
		// Page
		List<TbItem> list = tbItemMapper.selectByExample(tbItemExample);
		// 创建一个返回对象
		EasyUIDataGrid easyUIDataGrid = new EasyUIDataGrid();
		easyUIDataGrid.setRows(list);

		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		easyUIDataGrid.setTotal(pageInfo.getTotal());

		return easyUIDataGrid;
	}

	public ResponseResult addItem(TbItem tbItem, String desc) {
		// TODO Auto-generated method stub
		// 生成商品ID
		final long itemId = IDUtils.genItemId();
		tbItem.setId(itemId);
		// 设置商品的状态 1是正常 2下架 3删除
		tbItem.setStatus((byte) 1);
		// 设置时间
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		tbItemMapper.insert(tbItem);

		// 添加描述
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		tbItemDescMapper.insert(tbItemDesc);

		// 发送消息
		jmsTemplate.send(queueDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage(itemId + "");
			}
		});

		// 发送消息
		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage(itemId + "");
			}
		});

		return ResponseResult.ok();
	}

}
