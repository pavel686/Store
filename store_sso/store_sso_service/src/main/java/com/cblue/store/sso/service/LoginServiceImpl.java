package com.cblue.store.sso.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cblue.store.common.jedis.JedisClient;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.util.JsonUtils;
import com.cblue.store.mapper.TbUserMapper;
import com.cblue.store.pojo.TbUser;
import com.cblue.store.pojo.TbUserExample;
import com.cblue.store.pojo.TbUserExample.Criteria;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public ResponseResult login(String username, String password) {
		// TODO Auto-generated method stub
		//1 获得用户名和密码，进行数据库查询
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);
		//2如果错误，返回到登录页面
		System.out.println(tbUsers.size()+"---"+tbUsers);
		if(tbUsers.size()==0||tbUsers==null){
			//返回失败
			return ResponseResult.build(500, "用户名或密码错误");
		}
		//获得用户信息
		TbUser tbUser = tbUsers.get(0);
		//校验密码
		System.out.println("原密码:"+password);
		System.out.println("加密后的:"+DigestUtils.md5Hex(password));
		System.out.println("数据库查的密码"+tbUser.getPassword());
		if(!DigestUtils.md5Hex(password).equals(tbUser.getPassword())){
			//返回失败
			return ResponseResult.build(500, "用户名或密码错误");
		}
		
		//3生成token， sessionid ---key---》uuid
		String token = UUID.randomUUID().toString();
		
		//4 把token和对象，保存到redis中，并设定超时时间
		tbUser.setPassword(null);
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(tbUser));
		jedisClient.expire("SESSION:"+token, 1800);
		//5 如果成功，返回ResponseResult
		
		return ResponseResult.ok(token);
	}

}
