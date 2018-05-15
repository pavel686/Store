package com.cblue.store.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cblue.store.cart.service.CartService;
import com.cblue.store.common.pojo.ResponseResult;
import com.cblue.store.common.util.CookieUtils;
import com.cblue.store.common.util.JsonUtils;
import com.cblue.store.pojo.TbItem;
import com.cblue.store.pojo.TbUser;
import com.cblue.store.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {

	
	//首先需要判断用户是否登录，如果没有登录，跳转到登录页面。如果登录，跳转到订单页面
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private CartService cartService;
	 
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj) throws Exception {
		//首先需要判断用户是否登录，如果没有登录，跳转到登录页面
		  //首先找到token
		   String token = CookieUtils.getCookieValue(request,"token");
		   //判断token是否存在
		     //token不存在，跳转到登录页面
		   if(StringUtils.isBlank(token)){
			   //request.getRequestURL() 获取拦截到的页面的路径
			   response.sendRedirect("http://localhost:8090/page/login?redirect="+request.getRequestURL());
			   return false;
		   }
		   
		   //token登录，说明之前登录过
		  ResponseResult responseResult = tokenService.getUserByToken(token);
		  //说明用户登录过期，重新登录
		  if(responseResult.getStatus()!=200){
			  response.sendRedirect("http://localhost:8090/page/login?redirect="+request.getRequestURL());
			  return false;
		  }
		  TbUser tbUser = (TbUser)responseResult.getData();
		  request.setAttribute("user", tbUser);
		  //把cookie中的信息和redis中的信息进行合并
		 String jsonList =  CookieUtils.getCookieValue(request, "cart", true);
		 if(StringUtils.isNoneBlank(jsonList)){
			 cartService.mergeCart(tbUser.getId(), JsonUtils.jsonToList(jsonList, TbItem.class));
		 }
		//放行
		return true;
	}
	
	
	
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}




}
