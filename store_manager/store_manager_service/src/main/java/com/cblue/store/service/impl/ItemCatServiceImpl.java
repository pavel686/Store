package com.cblue.store.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cblue.store.common.pojo.EasyUITreeNode;
import com.cblue.store.mapper.TbItemCatMapper;
import com.cblue.store.pojo.TbItemCat;
import com.cblue.store.pojo.TbItemCatExample;
import com.cblue.store.pojo.TbItemCatExample.Criteria;
import com.cblue.store.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private TbItemCatMapper tbItemCatMapper;

	//parentid=2
	@Override
	public List<EasyUITreeNode> getItemCatList(Long parentId) {
		// TODO Auto-generated method stub
		TbItemCatExample tbItemCatExample = new TbItemCatExample();
		Criteria criteria = tbItemCatExample.createCriteria();
		//添加条件  where parend_id=parentId   SELECT * FROM tb_item_cat WHERE parent_id=0
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> taItemCatList = tbItemCatMapper.selectByExample(tbItemCatExample);
		//把查询的结果，转化成List<EasyUITreeNode>
		List<EasyUITreeNode> easyUITreeNodeList = new ArrayList<EasyUITreeNode>();
		for(TbItemCat tabItemCat:taItemCatList){
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(tabItemCat.getId());
			easyUITreeNode.setText(tabItemCat.getName());
			//注意: tabItemCat.getIsParent()代表是否是父目录：closed，否则是 "open"
			easyUITreeNode.setState(tabItemCat.getIsParent()?"closed":"open");
			//把结果设置到list中
			easyUITreeNodeList.add(easyUITreeNode);
		}
		return easyUITreeNodeList;
	}



}
