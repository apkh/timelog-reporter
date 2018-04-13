package com.vranec.jpa;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.vranec.jpa" }, transactionManagerRef = "jpaTransactionManager")
@EnableJpaAuditing
//@PropertySource({ "classpath:persistence-mysql.properties" })
@ComponentScan({ "com.vranec.jpa" })
public class TestConfig {
}
