package com.hason.study.spring_data_1;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hason.study.spring_data_1.entity.Person;
import com.hason.study.spring_data_1.repository.PersonRepository;

public class SpringDataTest {
	
	private ApplicationContext context;
	
	private PersonRepository personRepository;
	
	{
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		personRepository = context.getBean(PersonRepository.class);
	}
	
	// 测试数据源
	@Test
	public void testDataSource() {
		DataSource dataSource = context.getBean(DataSource.class);
		System.out.println(dataSource);
	}
	
	/**
	 * 根据查询关键词，自定义方法
	 */
	@Test
	public void testCustomMethod1() {  // 比较字符串 =
		List<Person> person = personRepository.getByLastName("hason1");
		System.out.println(person);
	}
	
	@Test
	public void testCustomMethod2() {  // 比较字符串 =
		List<Person> person = personRepository.getByEmailIs("1@qq.com");
		System.out.println(person);
	}
	
	@Test
	public void testCustomMethod3() {  // 比较时间
		List<Person> person = personRepository.getByBirthdayBefore(new Date());
		System.out.println(person);
	}
	
	@Test
	public void testCustomMethod4() { // 测试联表查询
		List<Person> person = personRepository.getByCountry_Id(1);
		System.out.println(person);
	}
	/**
	 * end 
	 */
	
	
	
	
	
	/**
	 * 测试 @Query 注解，使用JPQL和原生SQL
	 */
	
	@Test
	public void testAnnotation1() {  // 自定义sql
		Person person = personRepository.annotationMaxId();
		System.out.println(person);
	}
	
	@Test
	public void testAnnotation2() {  // 占位符
		List<Person> person = personRepository.annotationLastNameAndEmail1("hason1", "1@qq.com");
		System.out.println(person);
	}
	
	@Test
	public void testAnnotation3() {  // 命名参数
		List<Person> person = personRepository.annotationLastNameAndEmail2("hason1", "1@qq.com");
		System.out.println(person);
	}
	
	@Test
	public void testAnnotation4() {  // 原生SQL
		Long count = personRepository.annotationCount();
		System.out.println(count);
	}
	
	@Test
	public void testAnnotation5() {  // 原生SQL + 自动映射
		List<Person> person = personRepository.annotationFindAll();
		System.out.println(person);
	}
	
	@Test
	public void testAnnotation6() {  // update 操作
		int result = personRepository.annotationUpdatePerson(1, "1@qq.com.update");
		System.out.println(result);
	}
	
	/**
	 * end
	 */
}
