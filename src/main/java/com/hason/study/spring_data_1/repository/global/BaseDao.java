package com.hason.study.spring_data_1.repository.global;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 自定义全局Repository
 * 
 * 1、需要继承Spring Data的Repository接口 或 其子接口
 * 
 * 
 * @author hason
 *
 */
@NoRepositoryBean
public interface BaseDao<T, ID extends Serializable> extends JpaRepository<T, Serializable>{

	public void testGlobalRepositoryCustom();
}
