package net.smartcosmos.database;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Just runs the migration scripts on an embedded database.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { DatabaseMigrationTest.DatabaseMigrationTestApplication.class })
@ActiveProfiles("test")
@WebAppConfiguration
@IntegrationTest({ "spring.cloud.config.enabled=false", "eureka.client.enabled:false", "jpa.hibernate.ddl-auto:validate", "flyway.enabled:true" })
public class DatabaseMigrationTest {

    @SpringBootApplication
    public static class DatabaseMigrationTestApplication {

    }

    @Test
    public void thatDatabaseIsMigrated() {

    }
}
