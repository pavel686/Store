package com.cblue.store.service.impl;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cblue.store.mapper.TbItemMapper;
import com.cblue.store.pojo.TbItem;
import com.cblue.store.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TestPageHelper {
	
	@Test
	public void testPageHelper(){
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		TbItemMapper tbMapper = applicationContext.getBean(TbItemMapper.class);
		
		PageHelper.startPage(1, 10);
		TbItemExample tbItemExample = new TbItemExample();
		List<TbItem> tbItemList = tbMapper.selectByExample(tbItemExample);
		
		//获得一个分页信息对象
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(tbItemList);
		
		System.out.println(pageInfo.getTotal()); //934
		System.out.println(pageInfo.getPages()); //94
		System.out.println(pageInfo.getList().size()); //10
		
		
	}

}
