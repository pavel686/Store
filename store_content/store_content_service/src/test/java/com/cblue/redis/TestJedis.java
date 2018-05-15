package com.cblue.redis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {/*
	
	
	*//**
	 * 单机版操作redis
	 *//*

	@Test
	public void testJedisOne(){
		
		Jedis jedis = new Jedis("192.168.0.128",6379);
		jedis.set("xxx","yyy");
		String value = jedis.get("xxx");
		System.out.println(value);
		jedis.close();
	}
	
	*//**
	 * 还可以使用连接池
	 *//*
	@Test
	public void testJedisPool(){
		JedisPool jedisPool = new JedisPool("192.168.0.128",6379);
		Jedis jedis = jedisPool.getResource();
		jedis.set("xxx","yyy");
		String value = jedis.get("xxx");
		System.out.println(value);
		jedis.close();
	}
	
	*//**
	 * 连接redis集群
	 *//*
	@Test
	public void testJedisCluster(){
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.0.128",6001));
		nodes.add(new HostAndPort("192.168.0.128",6002));
		nodes.add(new HostAndPort("192.168.0.128",6003));
		nodes.add(new HostAndPort("192.168.0.128",6004));
		nodes.add(new HostAndPort("192.168.0.128",6005));
		nodes.add(new HostAndPort("192.168.0.128",6006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("xxx","yyy");
		String value = jedisCluster.get("xxx");
		System.out.println(value);
		jedisCluster.close();
		
	}
	
*/}
