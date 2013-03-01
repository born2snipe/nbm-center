/**
 *
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package nbm.center;

import com.google.common.collect.ImmutableList;
import com.yammer.dropwizard.config.ConfigurationFactory;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.SessionFactoryFactory;
import com.yammer.dropwizard.json.ObjectMapperFactory;
import com.yammer.dropwizard.migrations.ManagedLiquibase;
import com.yammer.dropwizard.validation.Validator;
import nbm.center.catalog.CatalogEntry;
import nbm.center.module.Module;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.net.URL;

public abstract class RepositoryTest {
    private static SessionFactory sessionFactory;
    protected Session session;
    private Transaction transaction;

    @BeforeClass
    public static void init() {
        sessionFactory = sessionFactory(
                NbmCenterConfiguration.class,
                "test.yml",
                Module.class, CatalogEntry.class
        );
    }

    @AfterClass
    public static void done() {
        sessionFactory.close();
    }

    @Before
    public void setUp() {
        session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        transaction = session.beginTransaction();
        initRepository(sessionFactory);
    }

    protected abstract void initRepository(SessionFactory sessionFactory);

    @After
    public void tearDown() {
        transaction.rollback();
        session.close();
        ManagedSessionContext.unbind(sessionFactory);
    }


    private static SessionFactory sessionFactory(Class<NbmCenterConfiguration> configClass, String configFilename, Class<?>... entities) {
        try {
            NbmCenterConfiguration config = loadConfiguration(configClass, configFilename);
            DatabaseConfiguration databaseConfiguration = config.getDatabaseConfiguration();

            applyTheMigration(databaseConfiguration);

            SessionFactoryFactory sessionFactoryFactory = new SessionFactoryFactory();
            return sessionFactoryFactory.build(
                    new Environment("test", config, new ObjectMapperFactory(), new Validator()),
                    databaseConfiguration,
                    ImmutableList.copyOf(entities)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void applyTheMigration(DatabaseConfiguration databaseConfiguration) throws Exception {
        ManagedLiquibase managedLiquibase = new ManagedLiquibase(databaseConfiguration);
        managedLiquibase.start();
        managedLiquibase.update("");
        managedLiquibase.stop();
    }

    private static <T> T loadConfiguration(Class<T> configClass, String configFilename) {
        try {
            ConfigurationFactory configurationFactory = ConfigurationFactory.forClass(
                    configClass,
                    new Validator(),
                    new ObjectMapperFactory()
            );

            URL resource = Thread.currentThread().getContextClassLoader().getResource(configFilename);
            return (T) configurationFactory.build(new File(resource.toURI()));
        } catch (Exception e) {
            throw new RuntimeException("A problem loading the configuration file", e);
        }
    }
}
