package com.example.mvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.example.mvc.entity.Person;

public interface PersonService {
    long count();
    
    Page<Person> findAll(int page, int size);
    
    Page<Person> findAll(Sort sort, int page, int pageSize);

    Page<Person> findByNameLike(String name, int page, int size);

    Person findById(Integer id);

    Person insert(Person person);

    Person update(Person person);

    void deleteById(Integer id);

}
