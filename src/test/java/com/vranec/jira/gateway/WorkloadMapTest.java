package com.vranec.jira.gateway;

import static java.time.LocalDate.now;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vranec.configuration.SpringContextConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringContextConfiguration.class)
public class WorkloadMapTest {

	@Autowired
	WorkloadMap wlMap;
	
	@Test
	public void test() {
		wlMap.addWl(new Date(), "task", "sum", "user", 15);
		assertEquals("", wlMap.getTasks(new Date().getDay(), "user"));
	}

}
