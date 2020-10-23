package com.akman.springbootdemo.rest_api.config;

import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@PropertySource(value = {"classpath:application.yml"})
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.akman.springbootdemo.repository"}
)
@RequiredArgsConstructor
public class DBConfig {/*
    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    */@Value("${spring.jpa.database-platform}")
    private String dialect;

    /*javax.sql*/
    /*@Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }*/

    private final DataSource dataSource;


    /*org.springframework.orm.hibernate5*/
    @Bean(name = "entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setHibernateProperties(getHibernateProperties());
        factory.setPackagesToScan("com.akman.springbootdemo.model");
        return factory;
    }

    /*java.util*/
    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        return properties;
    }

    @DependsOn("entityManagerFactory")
    @Bean
    @Autowired
    public EntityManager entityManager(LocalSessionFactoryBean localSessionFactoryBean) {
        EntityManager em = Objects.requireNonNull(localSessionFactoryBean.getObject()).createEntityManager();
        em.setFlushMode(FlushModeType.AUTO);
        return em;
    }

    @DependsOn("entityManagerFactory")
    @Bean
    @Autowired
    public JpaTransactionManager transactionManager(LocalSessionFactoryBean localSessionFactoryBean) throws Exception {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(localSessionFactoryBean.getObject());
        // The below line would generate javax.persistence.TransactionRequiredException: no transaction is in progress
        // transactionManager.setEntityManagerFactory(emf.getNativeEntityManagerFactory());
        return transactionManager;
    }
}

