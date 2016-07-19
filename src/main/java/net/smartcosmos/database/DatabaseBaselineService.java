package net.smartcosmos.database;

import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author voor
 */
@Component
@Profile("baseline")
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class DatabaseBaselineService implements ApplicationContextAware, ApplicationRunner {

    @Value("${spring.datasource.name}")
    String databaseName;
    @Value("${spring.datasource.hostname}")
    String databaseHostname;
    @Value("${spring.datasource.username}")
    String databaseUser;
    @Value("${spring.datasource.password}")
    String databasePassword;
    private ConfigurableApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.context = (ConfigurableApplicationContext) applicationContext;
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


        long version = 0L;
        Path backup;
        do  {
            version++;
            backup = Paths.get(String.format("src/main/resources/db/migration/V%d__migration.sql",version));
        } while (backup.toFile().exists());

        ProcessBuilder processBuilder = new ProcessBuilder("mysqldump",
                                                           "-u", databaseUser,
                                                           "-p" + databasePassword, "-r",
                                                           backup.toAbsolutePath().normalize().toString(),
                                                           "--add-locks",
                                                           "--comments",
                                                           "-h",
                                                           databaseHostname,
                                                           databaseName);
        processBuilder.inheritIO();
        Process process = processBuilder.start();

        int processComplete = process.waitFor();
        if (processComplete == 0) {
            log.info("Backup taken successfully");
        } else {
            log.info("Could not take mysql backup");
        }

        log.info("Shutting down...");

        new Thread(() -> {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException ex) {
                // Swallow exception and continue
            }
            DatabaseBaselineService.this.context.close();
        }).start();

    }
}
