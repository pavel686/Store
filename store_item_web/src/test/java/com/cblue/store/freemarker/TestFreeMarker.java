package com.cblue.store.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreeMarker {
	
	/**
	 * 1.需要一个模板文件
	 * 2.创建一个FreeMarker的Configuration配置对象
	 *    配置模板的路径
	 *    配置模板的编码方式
	 *    配置模板名
	 *    放入要替换的数据  name  aaa
	 *    指定生成文件的位置
	 * 3.生成文件
	 */
	@Test
	public void testFreeMarker()throws Exception{
		Configuration configuration = new Configuration(Configuration.getVersion());
		
		configuration.setDirectoryForTemplateLoading(new File("E:/Workspaces_1706B/store_item_web/src/main/webapp/WEB-INF/fm"));
		
		configuration.setDefaultEncoding("UTF-8");
		
//		Template template = configuration.getTemplate("hello.fm");
//		Template template = configuration.getTemplate("show.fm");
		Template template = configuration.getTemplate("test.fm");
		
		Map map = new HashMap();
		map.put("name", "zhangsan");
		
		Student student = new Student(1,"lisi",20);
		map.put("stu", student);
		
		List<Student> list = new ArrayList<Student>();
		for(int i=0;i<10;i++){
		  Student student1 = new Student(1,"lisi"+i,20);
		  list.add(student1);
		}
		map.put("list", list);
		
		map.put("date",new Date());
		
		map.put("abc", "aaa");
		

		
		
//		Writer out = new FileWriter("D:/hello.html");
//		Writer out = new FileWriter("D:/show.html");
		Writer out = new FileWriter("D:/test.html");
		
		template.process(map, out);
		
		out.close();
		
		
		
	}

}
