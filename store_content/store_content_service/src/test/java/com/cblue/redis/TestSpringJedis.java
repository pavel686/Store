package com.cblue.redis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cblue.store.common.jedis.JedisClientCluster;
import com.cblue.store.common.jedis.JedisClientPool;

public class TestSpringJedis {/*
	
	@Test
	public void testSpringJedisOne(){
		
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClientPool jdClientPool = (JedisClientPool)context.getBean(JedisClientPool.class);
		jdClientPool.set("111", "222");
	
	}
	
	@Test
	public void testSpringJedisCluster(){
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClientCluster jdClientCluster = (JedisClientCluster)context.getBean(JedisClientCluster.class);
		jdClientCluster.set("cluster", "hello");
	}

*/}
