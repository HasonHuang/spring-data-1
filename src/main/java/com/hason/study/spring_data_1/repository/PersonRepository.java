package com.hason.study.spring_data_1.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hason.study.spring_data_1.entity.Person;

/**
 * 1、Repository是一个标记性接口，它的内容是空的。
 * 
 * 2、若接口继承了Repository，该接口会被IOC容器识别为一个Repository Bean。
 * 纳入到IOC容器中，进而可以在该接口中定义满足一定规范的方法。
 * 
 * 3、实际上，也可以通过 @RepositoryDefinition 注解代替 继承Repository接口
 * 
 * 
 * 在Repository子接口中声明方法
 * 1、不是随便声明的，而需要符合一定的规范
 * 2、查询方法以 find | read | get 开头
 * 3、条件查询时，条件的属性用条件关键字连接
 * 4、注意的是：条件属性以首字母大写的驼峰规范
 * 5、支持联表查询。若当前类有符合条件的属性，则优先使用，而不使用级联属性（外键）
 *    若需要使用级联属性，则属性之间使用 _ 进行连接。 
 *
 *
 * @Query 注解传递参数的方式：
 * 1、使用占位符，如：?1、?2
 * 2、命名参数 + @param注解，如：:lastName、:email，方法的参数类型前加注解@Param("email") @Param("lastName")

 */
public interface PersonRepository extends Repository<Person, Integer>{

	/**
	 * 根据lastName，获取用户
	 * @param lastName
	 * @return
	 */
	public List<Person> getByLastName(String lastName);
	
	/**
	 * 获取Email等于参数的Person
	 * @param email
	 * @return
	 */
	public List<Person> getByEmailIs(String email);
	
	/**
	 * 获取date之前生日的Person
	 * @param date
	 * @return
	 */
	public List<Person> getByBirthdayBefore(Date date);
	
	/**
	 * 根据级联字段（外键）查询 Person
	 * 当级联字段与Person本身的属性重名时，优先取Person自身的属性作条件；
	 * 可以把级联属性之间使用 _ 进行连接
	 * @param countryId
	 * @return
	 */
	public List<Person> getByCountry_Id(int countryId);
	
	/**
	 * 使用 @Query 注解，JPQL
	 */
	@Query("select p1 from Person p1 where id = (select max(p2.id) from Person p2) ")
	public Person annotationMaxId();
	
	/**
	 * 使用 占位符，JPQL
	 */
	@Query(value = "select p1 from Person p1 where p1.lastName = ?1 and p1.email = ?2 ")
	public List<Person> annotationLastNameAndEmail1(String lastName, String email);
	
	/**
	 * 使用 命名参数，JPQL
	 */
	@Query(value = "select p1 from Person p1 where p1.lastName = :lastName and p1.email = :email ")
	public List<Person> annotationLastNameAndEmail2(@Param("lastName") String lastName, @Param(value = "email") String email);
	
	/**
	 * 使用 原生SQL
	 */
	@Query(value = "select count(1) from t_person", nativeQuery = true)
	public Long annotationCount();
	
	/**
	 * 使用 原生SQL + 自动映射(查询全部属性时)；
	 * 只查部分属性时返回Object[] 或 List<Object[]>
	 */
	@Query(value = "select * from t_person", nativeQuery = true)
	public List<Person> annotationFindAll();
	
	/**
	 * 可以通过自定义的 JPQL 完成 update 和 delete 操作。注意：JPQL不支持INSERT操作
	 * 1、在 @Query 中写 JPQL 语句时，必须用 @Modifying 修饰，以通知 SpringData 这是一个 UPDATE 或 DELETE 操作
	 * 2、UPDATE 或 DELETE 操作需要使用事务，可用 @Transactional （事务控制一般是在service层）
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE Person p SET p.email = :email WHERE p.id = :id")
	public int annotationUpdatePerson(@Param("id") int id, @Param("email") String email);
	
}
