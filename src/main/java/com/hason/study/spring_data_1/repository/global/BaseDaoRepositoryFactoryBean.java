package com.hason.study.spring_data_1.repository.global;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;



/**
 * 3、定义JpaRepositoryFactoryBean的实现类，使其生成（1）中定义的接口的对象。
 * 
 * 
 * @author hason
 *
 */
public class BaseDaoRepositoryFactoryBean<R extends Repository<S, ID>, S, ID extends Serializable>
		extends JpaRepositoryFactoryBean<R, S, ID> {

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new BaseDaoFactory(entityManager);
	}
	
	// 定义内部类
	private static class BaseDaoFactory<S, ID extends Serializable>
			extends JpaRepositoryFactory {
		
		private final EntityManager entityManager;

		public BaseDaoFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}
		
		// 获取自定义全局接口的对象
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new BaseDaoImpl<>(metadata.getDomainType(), entityManager);
		}
		
		/* 网络上的代码是重写该方法，JpaRepositoryFactory 中已经不调用该方法了。改用上面的
		protected <T, ID extends Serializable> JpaRepository<?, ?> getTargetRepository(
				RepositoryMetadata metadata, EntityManager entityManager) {
			return new BaseDaoImpl<>(metadata.getDomainType(), entityManager);
		}
		*/
		
		// 获取自定义的全局Repository的类型
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return BaseDao.class;
		}
	}
	
}
