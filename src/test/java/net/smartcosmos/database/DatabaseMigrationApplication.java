package net.smartcosmos.database;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

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

}
