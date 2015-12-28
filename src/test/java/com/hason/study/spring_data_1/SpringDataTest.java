package com.hason.study.spring_data_1;

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
	
	// 测试Spring Data Repository
	@Test
	public void testEntityFactory() {
		Person person = personRepository.getByLastName("hason1");
		System.out.println(person);
	}
}
