package com.cblue.store.home.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cblue.store.common.jedis.JedisClientPool;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.util.JsonUtils;
import com.cblue.store.mapper.TbContentMapper;
import com.cblue.store.pojo.TbContent;
import com.cblue.store.pojo.TbContentExample;
import com.cblue.store.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper tbContentMapper;
	
	@Autowired
	private  JedisClientPool jedisClientPool;
	
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	@Override
	public ResponseResult addContent(TbContent tbContent) {
		// TODO Auto-generated method stub
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		tbContentMapper.insert(tbContent);
		jedisClientPool.hdel(CONTENT_LIST,tbContent.getCategoryId().toString());
		return ResponseResult.ok();
	}

	@Override
	public List<TbContent> getContentByCategoryId(long categoryId) {
		// TODO Auto-generated method stub
		//select * from tb_content where categoryid=?
		List<TbContent> list = null;
		//获得缓存
		try {
			String jsonStr = jedisClientPool.hget(CONTENT_LIST, categoryId+"");
			//jsonStr不为空，返回json字符串
			if(StringUtils.isNoneBlank(jsonStr)){
				list = JsonUtils.jsonToList(jsonStr, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		TbContentExample tbContentExample = new TbContentExample();
		Criteria criteria = tbContentExample.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		list = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);
	
		//添加缓存
		try {
			//hash   hset hash1 key1 value1   get hash1  key1
			jedisClientPool.hset(CONTENT_LIST, categoryId+"", JsonUtils.objectToJson(list));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return list;
		
		
	}

}
