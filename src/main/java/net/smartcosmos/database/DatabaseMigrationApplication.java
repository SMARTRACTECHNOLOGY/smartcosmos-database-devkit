package net.smartcosmos.database;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Conducts the actual database migration, creating the necessary schema hash.
 *
 * This application is the one designed to be used in a production environment to properly conduct any database changes required.
 */
@SpringBootApplication
public class DatabaseMigrationApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(DatabaseBaselineApplication.class)
            .profiles("migration")
            .bannerMode(Banner.Mode.OFF)
            .run(args);
    }
}
