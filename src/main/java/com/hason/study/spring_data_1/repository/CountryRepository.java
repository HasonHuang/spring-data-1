package com.hason.study.spring_data_1.repository;

import com.hason.study.spring_data_1.entity.Country;
import com.hason.study.spring_data_1.repository.global.BaseDao;

/**
 * 继承自定义的全局Repository，测试
 * 
 * @author hason
 *
 */
public interface CountryRepository extends BaseDao<Country, Integer> {

}
