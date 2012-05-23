package com.example.mvc.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.mvc.entity.Person;
import com.example.mvc.repository.PersonRepository;
import com.example.mvc.service.PersonService;

@ContextConfiguration(locations = "classpath:test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PersonServiceImplTest {
    @Inject
    PersonRepository personRepository;
    @Inject
    PersonService personService;

    @Before
    public void setUp() throws Exception {
        for (int i = 1; i <= 20; i++) {
            Person p = new Person();
            p.setAge(i % 100);
            p.setName("name" + i);
            personRepository.save(p);
        }
        personRepository.flush();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFindAll00() {
        Page<Person> p = personService.findAll(0, 5);
        assertNotNull(p);
        List<Person> persons = p.getContent();
        assertNotNull(persons);
        assertEquals(5, persons.size());
        assertEquals(5, p.getNumberOfElements());
        assertEquals(0, p.getNumber());
        assertEquals(5, p.getSize());
        assertEquals(4, p.getTotalPages());
        assertEquals(20, p.getTotalElements());
    }

    @Test
    public void testFindAll01() {
        Page<Person> p = personService.findAll(1, 5);
        assertNotNull(p);
        List<Person> persons = p.getContent();
        assertNotNull(persons);
        assertEquals(5, persons.size());
        assertEquals(5, p.getNumberOfElements());
        assertEquals(1, p.getNumber());
        assertEquals(5, p.getSize());
        assertEquals(4, p.getTotalPages());
        assertEquals(20, p.getTotalElements());
    }
    
    @Test
    public void testFindAllWithSort() {
        Sort sort = new Sort(Direction.DESC, "age");
        Page<Person> page = personService.findAll(sort, 0, 10);
        List<Person> persons = page.getContent();
        assertNotNull(persons);
        assertEquals(10, persons.size());
        assertEquals(10, page.getNumberOfElements());
        assertEquals(0, page.getNumber());
        assertEquals(10, page.getSize());
        assertEquals(2, page.getTotalPages());
        assertEquals(20, page.getTotalElements());
        
        int lastAge = Integer.MAX_VALUE;
        for (Person p : persons) {
            assertTrue(p.getAge() < lastAge);
            lastAge = p.getAge();
        }
    }

    @Test
    public void testFindByNameLike() throws Exception {
        Page<Person> p = personService.findByNameLike("name1", 0, 5);
        System.out.println(p.getContent());
        assertNotNull(p);
        assertEquals(5, p.getNumberOfElements());
        assertEquals(0, p.getNumber());
        assertEquals(5, p.getSize());
        assertEquals(3, p.getTotalPages());
        assertEquals(11, p.getTotalElements());
    }

    @Test
    public void testFindById() {
        Person lastOne = personService.findAll(0, 1).getContent().get(0);
        Integer id = lastOne.getId();
        Person p = personService.findById(id);
        assertEquals(id, p.getId());
        assertEquals(lastOne.getName(), p.getName());
        assertEquals(lastOne.getAge(), p.getAge());
    }

    @Test
    public void testInsert() {
        Person lastOne = personService.findAll(
                new Sort(Direction.DESC, "id"), 0, 1).getContent().get(0);

        Person p = new Person();
        p.setAge(20);
        p.setName("noname");

        Person result = personService.insert(p);
        personRepository.flush();
        assertEquals(Integer.valueOf(lastOne.getId() + 1), result.getId());
    }

    @Test
    public void testUpdate() {
        Person lastOne = personService.findAll(0, 1).getContent().get(0);
        {
            Person p = personService.findById(lastOne.getId());
            p.setAge(30);
            p.setName("hoge");
            personService.update(p);
        }
        personRepository.flush();
        {
            Person p = personService.findById(lastOne.getId());
            assertEquals(Integer.valueOf(30), p.getAge());
            assertEquals("hoge", p.getName());
        }
    }

    @Test
    public void testDeleteById() {
        Person lastOne = personService.findAll(0, 1).getContent().get(0);
        personService.deleteById(lastOne.getId());
        personRepository.flush();
        Person p = personService.findById(lastOne.getId());
        assertNull(p);
    }

}
