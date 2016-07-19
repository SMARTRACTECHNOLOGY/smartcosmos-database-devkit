package net.smartcosmos.database;

import javax.persistence.ValidationMode;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import net.smartcosmos.extension.tenant.TenantRdao;

/**
 * Conducts the actual database migration, creating the necessary schema hash.
 *
 * This is a two step process:
 * 1) First, Flyway is run to provide the current database schema.
 * 2) Hibernate generates update scripts that are provided as SQL files for validation and analysis, this "automated" process is designed to
 * require human interaction before being pushed to production.
 *
 * This application is the one designed to be used in a production environment to properly conduct any database changes required.
 */
@SpringBootApplication(exclude = TenantRdao.class)
@Slf4j
public class DatabaseMigrationApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(DatabaseMigrationApplication.class)
            .bannerMode(Banner.Mode.OFF)
            .run(args);
    }


    @Autowired
    DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean smartcosmosEntityManagerFactory(
        EntityManagerFactoryBuilder builder) {

        builder.setCallback((factoryBean) -> {
            log.info("Hello! {}", factoryBean.getPersistenceUnitName());

            factoryBean.setValidationMode(ValidationMode.CALLBACK);
        });
        return builder
            .dataSource(dataSource)
            .packages("net.smartcosmos")
            .persistenceUnit("smartcosmos")
            .build();
    }

}
