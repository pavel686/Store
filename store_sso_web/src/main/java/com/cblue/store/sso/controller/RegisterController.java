package com.cblue.store.sso.controller;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.pojo.TbUser;
import com.cblue.store.sso.service.RegisterService;

@Controller
public class RegisterController {

	@RequestMapping("/page/register")
	public String goRegister() {
		System.out.println("111");
		return "register";
	}

	@Autowired
	private RegisterService registerService;
	

	// 数据校验
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public ResponseResult check(@PathVariable String param,
			@PathVariable int type) {
		return registerService.checkData(param, type);
	}

	// 注册
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult register(TbUser tbUser) {
		  return registerService.register(tbUser);
	}

}
