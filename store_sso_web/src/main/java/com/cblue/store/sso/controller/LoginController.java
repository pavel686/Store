package com.cblue.store.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.util.CookieUtils;
import com.cblue.store.sso.service.LoginService;

@Controller
public class LoginController {

	@RequestMapping("/page/login")
	public String goLogin(String redirect,Model model) {
		model.addAttribute("redirect", redirect);
		return "login";
	}

	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult login(String username, String password,
			HttpServletRequest request, HttpServletResponse response) {

		System.out.println("login-----");
		ResponseResult responseResult = loginService.login(username, password);
		// 如果登录成功
		if (responseResult.getStatus() == 200) {
			// 获得token
			String token = responseResult.getData().toString();
			// 把token写到cookie里
			CookieUtils.setCookie(request, response, "token", token);
		}
		
		return responseResult;
	}

}
