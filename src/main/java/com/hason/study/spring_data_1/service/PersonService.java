package com.hason.study.spring_data_1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hason.study.spring_data_1.entity.Person;
import com.hason.study.spring_data_1.repository.PersonRepository;

@Service
@Transactional
public class PersonService {

	@Autowired
	private PersonRepository personRepository;
	
	/**
	 * 保存多个对象
	 */
	public Iterable<Person> save(List<Person> list) {
		return personRepository.save(list);
	}
}
