package com.cblue.store.service.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestActiviMqSpring {
	
	
	@Test
	public void testSend()throws Exception{
	 //初始化spring容器
	 ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-activemq.xml");	
		
     //发送queue消息，还是发送topic消息，都需要一个发送者 jmsTemplate
	  JmsTemplate jmsTemplate = (JmsTemplate)context.getBean(JmsTemplate.class);
	 
	 
     //当你发送的是queue消息是时候，你用queueDestination
	  Destination destination1 = (Destination) context.getBean("queueDestination");
	  jmsTemplate.send(destination1,new MessageCreator() {
		
		@Override
		public Message createMessage(Session session) throws JMSException {
			// TODO Auto-generated method stub
			return session.createTextMessage("spring queue message");
		}
	});
	  
		
	// 当你发送topic消息的时候，你用topicDestination	
	/*  Destination destination2 = (Destination) context.getBean("topicDestination");
	  jmsTemplate.send(destination2,new MessageCreator() {
		
		@Override
		public Message createMessage(Session session) throws JMSException {
			// TODO Auto-generated method stub
			return session.createTextMessage("spring topic message");
		}
	});*/
		
	}
	
	

}
