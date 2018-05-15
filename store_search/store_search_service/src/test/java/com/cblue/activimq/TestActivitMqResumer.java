package com.cblue.activimq;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestActivitMqResumer {
	
	
	@Test
	public void testResumer() throws IOException{
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-activemq.xml");
		System.in.read();
		
	}

}
