package com.hason.study.spring_data_1.repository;

import org.springframework.data.repository.Repository;

import com.hason.study.spring_data_1.entity.Person;

/**
 * 1、Repository是一个标记性接口，它的内容是空的。
 * 
 * 2、若接口继承了Repository，该接口会被IOC容器识别为一个Repository Bean。
 * 纳入到IOC容器中，进而可以在该接口中定义满足一定规范的方法。
 * 
 * 3、实际上，也可以通过 @RepositoryDefinition 注解代替 继承Repository接口
 *
 */
public interface PersonRepository extends Repository<Person, Integer>{

	/**
	 * 根据lastName，获取用户
	 * @param lastName
	 * @return
	 */
	public Person getByLastName(String lastName);
}
