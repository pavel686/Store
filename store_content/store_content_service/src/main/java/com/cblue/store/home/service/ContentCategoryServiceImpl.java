package com.cblue.store.home.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cblue.store.common.pojo.EasyUITreeNode;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.mapper.TbContentCategoryMapper;
import com.cblue.store.pojo.TbContentCategory;
import com.cblue.store.pojo.TbContentCategoryExample;
import com.cblue.store.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getAllContentCategory(long parentId) {
		// TODO Auto-generated method stub
		TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
		Criteria criteria = tbContentCategoryExample.createCriteria();
		//parentId= ?
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
		
		List<EasyUITreeNode> result = new ArrayList<EasyUITreeNode>();
		
		for(TbContentCategory tbContentCategory:list){
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			result.add(node);
		}
		
		return result;
	}

	@Override
	public ResponseResult addContentCategory(long parentId, String name) {
		// TODO Auto-generated method stub
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setName(name);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setIsParent(false);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		tbContentCategoryMapper.insertSelective(tbContentCategory);
		
		//更新父节点的属性is_parent=true;
		TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKey(parent);
		}
		return ResponseResult.ok(tbContentCategory);
	}

}
