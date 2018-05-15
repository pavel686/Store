package com.cblue.store.service.impl;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.Connection;

public class TestActiviMq {

	@Test
	public void testQueuesProduct() throws Exception {
		// 创建一个ConnectionFactory对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://192.168.0.137:61616");
		// 创建Connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建一个Session对象
		// 第一个参数：表示是否开启事务，如果是true，代表开启事务，那么第二个参数无意义
		// 如果第一个参数是false，第二个参数代表应答模式 手动 自动
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		// 发送消息
		// 发送的点对点的队列名
		Queue queue = session.createQueue("myqueue");
		// 创建一个生产者
		MessageProducer messageProducer = session.createProducer(queue);
		// 创建一个message对象
		TextMessage msg = new ActiveMQTextMessage();
		msg.setText("hello activeMQ222");
		// 发送消息
		messageProducer.send(msg);

		// 关闭
		messageProducer.close();
		session.close();
		connection.close();

	}

	@Test
	public void testQueuesConsumer() throws Exception {

		// 创建一个ConnectionFactory对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://192.168.0.137:61616");
		// 创建Connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建一个Session对象
		// 第一个参数：表示是否开启事务，如果是true，代表开启事务，那么第二个参数无意义
		// 如果第一个参数是false，第二个参数代表应答模式 手动 自动
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		// 发送消息
		// 发送的点对点的队列名
		Queue queue = session.createQueue("spring-queue");
		// 创建一个消费者
		MessageConsumer messageConsumer = session.createConsumer(queue);
		// 接收消息
		messageConsumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message msg) {
				// TODO Auto-generated method stub
				try {
					TextMessage textMessage = (TextMessage) msg;
					String txt = textMessage.getText();
					System.out.println(txt);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		// 继续等待其他消息
		System.in.read();

		messageConsumer.close();
		session.close();
		connection.close();
	}

	// 发布订阅模式的发送消息
	@Test
	public void testTopicProduct() throws Exception {
		// 创建一个ConnectionFactory对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://192.168.0.137:61616");
		// 创建Connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建一个Session对象
		// 第一个参数：表示是否开启事务，如果是true，代表开启事务，那么第二个参数无意义
		// 如果第一个参数是false，第二个参数代表应答模式 手动 自动
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		// 发送消息
		// 发送的点对点的队列名
		Topic topic = session.createTopic("mytopic");
		// 创建一个生产者
		MessageProducer messageProducer = session.createProducer(topic);
		// 创建一个message对象
		TextMessage msg = new ActiveMQTextMessage();
		msg.setText("hello activeMQ topic");
		// 发送消息
		messageProducer.send(msg);
		// 关闭
		messageProducer.close();
		session.close();
		connection.close();
	}

	@Test
	public void testTopicResumer() throws Exception {
		// 创建一个ConnectionFactory对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://192.168.0.137:61616");
		// 创建Connection对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建一个Session对象
		// 第一个参数：表示是否开启事务，如果是true，代表开启事务，那么第二个参数无意义
		// 如果第一个参数是false，第二个参数代表应答模式 手动 自动
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		// 发送消息
		// 发送的点对点的队列名
		Topic topic = session.createTopic("mytopic");
		// 创建一个消费者
		MessageConsumer messageConsumer = session.createConsumer(topic);
		// 接收消息
		messageConsumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message msg) {
				// TODO Auto-generated method stub
				try {
					TextMessage textMessage = (TextMessage) msg;
					String txt = textMessage.getText();
					System.out.println(txt);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		System.out.println("我是消费者3");
		// 继续等待其他消息
		System.in.read();

		messageConsumer.close();
		session.close();
		connection.close();

	}

}
