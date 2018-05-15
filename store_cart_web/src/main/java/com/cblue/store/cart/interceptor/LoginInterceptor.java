package com.cblue.store.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.util.CookieUtils;
import com.cblue.store.pojo.TbUser;
import com.cblue.store.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {

	//从cookie中，获得token
	//如果token不存在，说明之前没有登录，我们把购物车信息保存到cookie中
	//如果token存在，去redis中查询，发现用户过期，我们把购物车信息保存到cookie中
	//在redis中，用户是存在的，购物车信息保存到redis中
	
	
	@Autowired
	private TokenService tokenService;
	
	//在拦截的Controller之前执行
	//boolean是否放行
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj) throws Exception {
		//从cookie中，获得token
		String token = CookieUtils.getCookieValue(request, "token");
		//判断token是否存在,直接放行
		if(StringUtils.isBlank(token)){
			return true;
		}
		//如果token存在，去redis中取数据。如果返回非200，没有获取的用户信息，直接放行
		ResponseResult responseResult = tokenService.getUserByToken(token);
		if(responseResult.getStatus()!=200){
			return true;
		}
		//如果获得对象
		TbUser tbUser = (TbUser)responseResult.getData();
		//把这个对象放入域中
		request.setAttribute("user", tbUser);
		return true;
	}

	//在拦截的Controller之后执行
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}
	
	//在postHandle方法之后，执行
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception exp)
			throws Exception {
		// TODO Auto-generated method stub

	}

	
}
