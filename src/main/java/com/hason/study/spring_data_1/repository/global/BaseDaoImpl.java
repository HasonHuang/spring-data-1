package com.hason.study.spring_data_1.repository.global;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 2、提供（1）中声明的接口的实现类。该类继承 SimpleJpaRepository 并提供方法的实现。
 * 
 * 注意：全局的扩展实现类不要用RepositoryImp作为后缀名,
 *      或为全局扩展接口添加@NoRepositoryBean 注解告知 Spring Data: 该实现类不是一个 Repository 
 * 
 * @author hason
 *
 */
@NoRepositoryBean
public class BaseDaoImpl<T, ID extends Serializable>
		extends SimpleJpaRepository<T, Serializable>
		implements BaseDao<T, Serializable>{
	
	private EntityManager entityManager;

	public BaseDaoImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityManager = em;
	}

	@Override
	public void testGlobalRepositoryCustom() {
		// 使用entityManager 做一些事
		System.out.println("我是自定义的全局Repository，entityManager:" + entityManager);
	}

}
