package net.smartcosmos.database;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import javax.persistence.Entity;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.hbm2ddl.Target;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${flyway.enabled}")
    boolean migrate;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    private Configuration configuration;

    private ConfigurableApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.context = (ConfigurableApplicationContext) applicationContext;
        }
    }

    @PostConstruct
    public void init() {

        configuration = new org.hibernate.cfg.Configuration();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        for (BeanDefinition bd : scanner.findCandidateComponents("net.smartcosmos")) {
            String name = bd.getBeanClassName();
            try {
                System.out.println("Added annotated entity class " + bd.getBeanClassName());
                configuration.addAnnotatedClass(Class.forName(name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.format_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.ejb.naming_strategy", "org.hibernate.cfg.EJB3NamingStrategy");

        configuration.setProperty("hibernate.connection.url", dataSourceProperties.getUrl());
        configuration.setProperty("hibernate.connection.username", dataSourceProperties.getUsername());
        configuration.setProperty("hibernate.connection.password", dataSourceProperties.getPassword());
        configuration.setProperty("hibernate.connection.driver", dataSourceProperties.getDriverClassName());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        final SchemaExport export = new SchemaExport(configuration);
        final SchemaUpdate update = new SchemaUpdate(configuration);
        if (!migrate) {
            export.setDelimiter(";");
            export.setHaltOnError(true);
            export.setFormat(true);
        } else {
            update.setDelimiter(";");
            update.setFormat(true);
            update.setHaltOnError(true);
        }

        long version = 0L;
        Path backup;
        do {
            version++;
            backup = Paths.get(String.format("src/main/resources/db/migration/V%d__migration.sql", version));
        } while (backup.toFile().exists());

        final String filename = backup.toAbsolutePath().normalize().toString();
        if (!migrate) {
            export.setOutputFile(filename);

            export.create(Target.NONE);
        } else {
            update.setOutputFile(filename);

            update.execute(Target.NONE);
        }

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
