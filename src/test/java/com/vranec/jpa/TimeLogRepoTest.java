package com.vranec.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.persistence.EntityManager;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = { TimeLog.class }, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
//@DatabaseSetup("todo-entries.xml")
@DataJpaTest
public class TimeLogRepoTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TimeLogRepository repo;

    @Test
    public void test_repo() {
        TimeLog tLog = TimeLog.builder().id(2l).build();
        entityManager.persist(tLog);
        entityManager.flush();
        repo.save(TimeLog.builder().id(3l).build());
        System.out.println(repo.findAll());
        Assert.assertTrue(repo.existsById(2l));
    }
}
