package com.cblue.store.sso.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cblue.store.common.jedis.JedisClient;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.util.JsonUtils;
import com.cblue.store.pojo.TbUser;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired	
    private JedisClient jedisClient;
	
	/**
	 * 根据token去redis中查询
	 */
	@Override
	public ResponseResult getUserByToken(String token) {
		// TODO Auto-generated method stub
		//获得User对象的json字符串
		String jsonStr = jedisClient.get("SESSION:"+token);
		//jsonStr为空，说明用户过期了
		if(StringUtils.isBlank(jsonStr)){
			//重新登录
			return ResponseResult.build(201, "用户已过期，请重新登录");
		}
		//如果jsonStr不为空，更新一个redis的超时时间
		jedisClient.expire("SESSION:"+token, 1800);
		//如果用户没有过期，我们就把用户信息返回
		TbUser tbUser = JsonUtils.jsonToPojo(jsonStr, TbUser.class);
		return ResponseResult.ok(tbUser);
	}

}
