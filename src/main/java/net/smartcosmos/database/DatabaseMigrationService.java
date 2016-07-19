package net.smartcosmos.database;

import java.nio.file.Paths;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.Target;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

/**
 * @author voor
 */
@Component
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class DatabaseMigrationService implements ApplicationContextAware, ApplicationRunner {

    @Autowired
    DataSourceProperties dataSourceProperties;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    Configuration configuration;

    private ConfigurableApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.context = (ConfigurableApplicationContext) applicationContext;
        }
    }

    public org.hibernate.cfg.Configuration getConfiguration() {

        if ( entityManagerFactory instanceof HibernateEntityManagerFactory) {
            ((HibernateEntityManagerFactory)entityManagerFactory).getSessionFactory();
        }
        org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        for (BeanDefinition bd : scanner.findCandidateComponents("net.smartcosmos")) {
            String name = bd.getBeanClassName();
            try {
                System.out.println("Added annotated entity class " + bd.getBeanClassName());
                cfg.addAnnotatedClass(Class.forName(name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cfg.setProperty("hibernate.dialect", MySQLDialect.getDialect().toString());
        cfg.setProperty("hibernate.show_sql", "true");
        cfg.setProperty("hibernate.format_sql", "true");
        cfg.setProperty("hibernate.hbm2ddl.auto", "update");
        cfg.setProperty("hibernate.ejb.naming_strategy", "oorg.hibernate.cfg.EJB3NamingStrategy");

        cfg.setProperty("hibernate.connection.url", dataSourceProperties.getUrl());
        cfg.setProperty("hibernate.connection.username", dataSourceProperties.getUsername());
        cfg.setProperty("hibernate.connection.password", dataSourceProperties.getPassword());
        cfg.setProperty("hibernate.connection.driver", dataSourceProperties.getDriverClassName());
        return cfg;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SchemaExport export = new SchemaExport(getConfiguration());
        export.setDelimiter(";");
        export.setHaltOnError(true);
        export.setFormat(true);

        export.setOutputFile(Paths.get("./file.txt").toAbsolutePath().normalize().toString());

        export.create(Target.NONE);

        new Thread(() -> {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException ex) {
                // Swallow exception and continue
            }
            DatabaseMigrationService.this.context.close();
        }).start();

    }
}
