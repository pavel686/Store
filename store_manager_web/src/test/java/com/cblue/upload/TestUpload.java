package com.cblue.upload;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.cblue.store.common.util.FastDFSClient;

public class TestUpload {
	
	
	/**
	 * 使用客户端实现上传
	 * @throws MyException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testUpload01() throws Exception{
		
		//初始化上传的路径
		ClientGlobal.init("E:\\Workspaces_1706B\\store_manager_web\\src\\main\\resources\\conf\\client.conf");
		
		//创建tracker客户端
		TrackerClient trackerClient = new TrackerClient();
		
		//通过客户端去连接服务端
		TrackerServer trackerServer = trackerClient.getConnection();
		
		//创建storage服务端引用
		StorageServer storageServer = null;
		
		//创建storage客户端
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		
		//完成上传
		String [] str = storageClient.upload_appender_file("E:\\tomcat.jpg","jpg",null);
		
		for(String s:str){
			System.out.println(s);
		}
			
	}
	
	/**
	 * 使用工具类实现上传
	 */
	@Test
	public void testUpload02()throws Exception{
		FastDFSClient fastDFSClient = new FastDFSClient("E:\\Workspaces_1706B\\store_manager_web\\src\\main\\resources\\conf\\client.conf");
		String result = fastDFSClient.uploadFile("E:\\tomcat.jpg","jpg");
		System.out.println(result);
		
	}
	
	

}
