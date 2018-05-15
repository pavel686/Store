package com.cblue.store.pay.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cblue.store.order.pojo.OrderInfo;
import com.cblue.store.order.service.OrderService;
import com.cblue.store.pay.util.PaymentUtil;
import com.cblue.store.pojo.TbOrder;


@Controller
@RequestMapping("/pay")
public class PayController {
	
	@Autowired
	private OrderService orderservice;
	
	@Value("${p1_MerId}")
	private String p1_MerId; //商户编号
	@Value("${keyValue}")
	private String keyValue; //商户验证码
	
	
	/**
	 * 跳转到支付页面
	 */
	@RequestMapping("/goPay")
	public String goPay(HttpServletRequest request,String pay_price,String orderId){
		request.setAttribute("pay_price", pay_price);
		request.setAttribute("orderId", orderId);
		return "pay";
	}
	
	
	/**
	 * 订单支付
	 * @throws IOException 
	 */
	@RequestMapping("/pay")
	public void orderPay(String orderid,String money,HttpServletRequest request,HttpServletResponse response) throws IOException{

		/***
		 * 更新订单信息
		 */
	  // TbOrder order = orderservice.(o.getOrderId());
	/*	order.setAddr(request.getParameter("addr"));
		order.setName(request.getParameter("name"));
		order.setPhone(request.getParameter("phone"));*/
		
		//更新order的信息
		//orderservice.updateOrder(order);

		/*******支付订单*****/
		/*
		 * 准备13参数
		 */
		String p0_Cmd = "Buy"; //业务类型， 固定值“Buy” . 
		//String p1_MerId = "10001126856"; //商户编号
		//String p2_Order =String.valueOf(order.getOid()); //订单号
		String p2_Order = orderid;
		String p3_Amt = "0.01"; //支付金额
		String p4_Cur = "CNY"; //交易币种
		String p5_Pid = ""; //商品名称
		String p6_Pcat = ""; //商品种类
		String p7_Pdesc = "";	//商品描述
		String p8_Url = "http://localhost:8888/pay/back.html"; //商户接收支付成功数据的地址
		String p9_SAF = ""; //送货地址
		String pa_MP = "";
		String pd_FrpId = request.getParameter("pd_FrpId"); //支付通道编码(银行)
		String pr_NeedResponse = "1";
		/*
		 * 计算hmac
		 */
		
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
		/*
		 * 连接易宝的网址和13+1个参数
		 */
		StringBuilder url = new StringBuilder("https://www.yeepay.com/app-merchant-proxy/node");
		url.append("?p0_Cmd=").append(p0_Cmd);
		url.append("&p1_MerId=").append(p1_MerId);
		url.append("&p2_Order=").append(p2_Order);
		url.append("&p3_Amt=").append(p3_Amt);
		url.append("&p4_Cur=").append(p4_Cur);
		url.append("&p5_Pid=").append(p5_Pid);
		url.append("&p6_Pcat=").append(p6_Pcat);
		url.append("&p7_Pdesc=").append(p7_Pdesc);
		url.append("&p8_Url=").append(p8_Url);
		url.append("&p9_SAF=").append(p9_SAF);
		url.append("&pa_MP=").append(pa_MP);
		url.append("&pd_FrpId=").append(pd_FrpId);
		url.append("&pr_NeedResponse=").append(pr_NeedResponse);
		url.append("&hmac=").append(hmac);
		/*
		 * 重定向到易宝
		 */
		response.sendRedirect(url.toString());
		
	}
	/**
	 * 接收支付结果信息
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/back")
	public String back(HttpServletRequest request,Model model) throws IOException{
		/*
		 * 易宝会提供一系列的结果参数，我们获取其中4个参数即可
		 * 获取支付结果：r1_Code表示支付成功
		 * 获取支付金额：r3_Amt
		 * 获取电商的订单号：r6_Order
		 * 获取结果返回类型：r9_BType 1表示重定向返回，2表示点对点返回，
		 *     但点对点我们收不到，因为我们的ip都是局域网ip
		 */
		String r1_Code = request.getParameter("r1_Code");
		String r3_Amt = request.getParameter("r3_Amt");
		String r6_Order = request.getParameter("r6_Order");
		String r9_BType = request.getParameter("r9_BType");
		
		if(r1_Code.equals("1")) {  //判断支付结果是否支付成功
			if(r9_BType.equals("1")) {//判断支付结果回调方式是否为重定向
				//Order order = orderservice.findOrderById(Integer.parseInt(r6_Order));
				//order.setState(2);
				//orderservice.updateOrder(order);
				model.addAttribute("msg", "您的订单:"+r6_Order+"已成功支付"+r3_Amt+"元！");
			}
		}else{
			model.addAttribute("msg", "对不起，您的订单支付失败，请稍候重试！");
		}
		return "paymsg";
	}

}
