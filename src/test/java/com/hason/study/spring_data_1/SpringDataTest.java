package com.hason.study.spring_data_1;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import com.hason.study.spring_data_1.entity.Person;
import com.hason.study.spring_data_1.repository.PersonRepository;
import com.hason.study.spring_data_1.service.PersonService;

public class SpringDataTest {
	
	private ApplicationContext context;
	
	private PersonRepository personRepository;
	
	private PersonService personService;
	
	{
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		personRepository = context.getBean(PersonRepository.class);
		personService = context.getBean(PersonService.class);
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
	
	
	
	
	
	
	/**
	 * CrudRepository
	 */
	@Test
	public void testCrudRepository() {
		List<Person> list = new ArrayList<Person>();
		for(int i = 'a'; i <= 'z'; i++) {
			Person person = new Person();
			person.setLastName("hason_" + (char) i);
			person.setBirthday(new Date());
			person.setEmail("" + (char) i + (char) i + "@qq.com");
			list.add(person);
		}
		List<Person> iterator = (List<Person>) personService.save(list);
		System.out.println(iterator);
	}
	
	@Test
	public void testPagingAndSortRepository() {
		// 分页
		int page = 2;  // 页码由0开始
		int size = 5;
		
		// 排序：字段是按实体对象的属性名；另一种方式：new Sort(o1).and(o2).and....
		Order order1 = new Order(Direction.DESC, "id");
		Order order2 = new Order(Direction.ASC, "lastName");
		Sort sort = new Sort(order1, order2);
		
		// Pageable通常使用 PageRequest 实现类，其封装了分页的信息
		Pageable pageable = new PageRequest(page, size, sort);
		Page<Person> result = personRepository.findAll(pageable);
		System.out.println("总记录数：" + result.getTotalElements());
		System.out.println("总页数：" + result.getTotalPages());
		System.out.println("当前页：" + result.getNumber());
		System.out.println("当前页的记录数：" + result.getNumberOfElements());
		System.out.println("当前页的内容：" + result.getContent());
	}
	
	/**
	 * JpaRepository
	 * CrudRepository的save()：把对象直接变成受控对象
	 * JpaRepository的saveAndFlush()：与merge()类似，复制出新的对象，新对象变成受控对象，持久化后返回游离对象
	 * 注意：断点查看save()和saveAndFlush()后，person的区别
	 */
	@Test
	public void testJpaRepository() {
		Person person = new Person();
		person.setBirthday(new Date());
		person.setLastName("hason_ins_1");
		person.setId(81);
		Person person2 = personRepository.saveAndFlush(person);
		System.out.println(person == person2);
	}
	
	
	
	/**
	 * 目标：实现带查询条件的分页，id > 5
	 * 调用 JpaSpecificationExecutor 的Page<T> findAll(Specification<T> spec, Pageable pageable)
	 * Specification：封装了JPA Criteria Query 的查询条件
	 * Pageable：封装了请求分页的信息，例如：page, size, sort
	 */
	@Test
	public void testJpaSpecificationExecutor() {
		// 分页
		int page = 2;
		int size = 5;
		Pageable pageable = new PageRequest(page, size);
		
		// 查询条件。通常使用 Specification 的匿名内部类 来初始化 Specification
		Specification<Person> specification = new Specification<Person>() {
			
			/**
			 * 目标：id > 5 的查询语句
			 * @param root：代表查询的实体类
			 * @param query：可以从中得到Root对象，即告知 JPA Criteria Query 要查询哪一个实体类，还可以
			 *               添加查询条件，还可以结合 EntityManager 对象得到最终的 TypedQuery 对象。
			 * @param cb：CriteriaBuilder对象。用于创建Criteria相关对象的工厂类；当然可以从中获取到 Predicate 对象
			 * 
			 * @return Predicate 代表一个查询
			 */
			public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Path path = root.get("id");  // 此处为实体类的属性名
				Predicate predicate = cb.gt(path, 5);
				return predicate;
			}
			
		};
		
		Page<Person> result = personRepository.findAll(specification, pageable);
		System.out.println("总记录数：" + result.getTotalElements());
		System.out.println("总页数：" + result.getTotalPages());
		System.out.println("当前页：" + (result.getNumber() + 1));
		System.out.println("当前页的记录数：" + result.getNumberOfElements());
		System.out.println("当前页的内容：" + result.getContent());
	}
	
	
	
	
	
	
	/**
	 * 自定义Repository方法。（实际是一个适配器模式实现）
	 * 1、定义一个自定义接口类
	 * 2、被扩展的Repository继承（1）中的接口。（例如被扩展接口为PersonRepository）
	 * 3、定义一个实现类。名字必须为 被扩展接口 + Impl，否则不能运行 ，如：PersonRepositoryImpl
	 * 注意：默认情况下，Spring Data 会在base-package中查找"接口名Impl"作为实现类；
	 *      <jpa:repositories > 提供了一个 repository-impl-postfix 属性，用以指定实现类的后缀
	 */
	@Test
	public void testCustomRepository() {
		// 实际上会调用 PersonRepositoryImpl.findAllPerson();
		// PersonRepositoryImpl是具有特有行为的被适配对象
		// CustomRepository是适配器
		// PersonRepository是具有通用行为的待适配对象
		// client -> PersonRepository -> CustomRepository -> PersonRepositoryImpl
		personRepository.findAllPerson();
	}
}
