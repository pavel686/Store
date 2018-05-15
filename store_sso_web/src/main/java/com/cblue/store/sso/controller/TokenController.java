package com.cblue.store.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.util.JsonUtils;
import com.cblue.store.sso.service.TokenService;

@Controller
public class TokenController {
	
	
	@Autowired
	private TokenService tokenService;
	
	//jsonp格式的返回值  规定返回的是json格式，并是utf-8的值
	@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback){
		//callback参数不为空，说明有callback参数，说明访问的是jsonp请求
		ResponseResult responseResult = tokenService.getUserByToken(token);
		if(StringUtils.isNotBlank(callback)){
			return  callback+"("+JsonUtils.objectToJson(responseResult)+" )";  //test(我请求的数据)
		}
		return JsonUtils.objectToJson(responseResult);
	}

}
