package com.cblue.store.search.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

public class GlobleExceptionResolver implements HandlerExceptionResolver {

	//日志对象
	private static final Logger logger = LoggerFactory.getLogger(GlobleExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception exception) {
		//处理全局异常
		//1 可以把异常信息打印到控制台
		exception.printStackTrace();
		//2 把错误信息保存到日志中
		logger.info("你的程序出错了 info");
		logger.error("你的程序出错了 error");
		//3 发邮件，发短信
		//4 跳转到错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");

		return modelAndView;
	}

}
