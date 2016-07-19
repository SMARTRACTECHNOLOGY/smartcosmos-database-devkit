package net.smartcosmos.database;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Simple application that bootstraps in the database migration by creating a database based on the entities found in the DevKit DAO implementations.
 *
 * Please note that this is meant to be run from the Command Line, and is not designed for any production use.
 */
@SpringBootApplication
@Slf4j
public class DatabaseBaselineApplication  {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DatabaseBaselineApplication.class)
            .profiles("baseline")
            .bannerMode(Banner.Mode.OFF)
            .run(args);
    }
}
