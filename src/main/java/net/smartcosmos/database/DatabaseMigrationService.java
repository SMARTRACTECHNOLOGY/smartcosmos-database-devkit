package net.smartcosmos.database;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.hibernate.tool.hbm2ddl.SchemaUpdateScript;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

/**
 * @author voor
 */
@Component
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class DatabaseMigrationService implements ApplicationContextAware, ApplicationRunner {

    @Autowired
    DataSource dataSource;
    private ConfigurableApplicationContext context;
    private Configuration configuration;
    private SessionFactory sessionFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.context = (ConfigurableApplicationContext) applicationContext;
        }
    }

    @PostConstruct
    public void init() {
        Object object = this.context.getBean("&sessionFactory");
        if (object instanceof org.springframework.orm.hibernate3.LocalSessionFactoryBean) {
            log.info("Hibernate 3 session factory...");
            configuration = ((org.springframework.orm.hibernate3.LocalSessionFactoryBean) object).getConfiguration();
            sessionFactory = ((org.springframework.orm.hibernate3.LocalSessionFactoryBean) object).getObject();
        } else if (object instanceof org.springframework.orm.hibernate4.LocalSessionFactoryBean) {
            log.info("Hibernate 4 session factory...");
            configuration = ((org.springframework.orm.hibernate4.LocalSessionFactoryBean) object).getConfiguration();
            sessionFactory = ((org.springframework.orm.hibernate4.LocalSessionFactoryBean) object).getObject();
        } else if (object instanceof org.springframework.orm.hibernate5.LocalSessionFactoryBean) {
            log.info("Hibernate 5 session factory...");
            configuration = ((org.springframework.orm.hibernate5.LocalSessionFactoryBean) object).getConfiguration();
            sessionFactory = ((org.springframework.orm.hibernate5.LocalSessionFactoryBean) object).getObject();
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        
        HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
        hibernateTemplate.setFlushMode(HibernateTemplate.FLUSH_NEVER);
        hibernateTemplate.execute(
            (session) -> {

                final Dialect dialect = ((SessionFactoryImplementor) sessionFactory).getDialect();
                DatabaseMetadata metadata = new DatabaseMetadata(dataSource.getConnection(), dialect, configuration);

                List<SchemaUpdateScript> schemaUpdateScripts = configuration.generateSchemaUpdateScriptList(dialect, metadata);

                for (SchemaUpdateScript script : schemaUpdateScripts) {
                    String formatted = FormatStyle.DDL.getFormatter().format(script.getScript());

                    // Replace with writing to file:
                    System.out.println(formatted + ";");
                }

                log.info("Shutting down...");

                new Thread(() -> {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException ex) {
                        // Swallow exception and continue
                    }
                    DatabaseMigrationService.this.context.close();
                }).start();

                return null;
            }
        );

    }
}
