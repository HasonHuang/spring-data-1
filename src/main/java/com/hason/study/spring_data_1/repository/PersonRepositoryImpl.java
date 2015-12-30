package com.hason.study.spring_data_1.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hason.study.spring_data_1.entity.Person;

public class PersonRepositoryImpl implements CustomRepository {
	
	@PersistenceContext
	private EntityManager entityManager;

	public void findAllPerson() {
		String sql = "select p from Person p";
		Query query = entityManager.createQuery(sql);
		List<Person> list = query.getResultList();
		System.out.println(list);
	}

}
