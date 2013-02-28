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

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.migrations.MigrationsBundle;
import nbm.center.module.Module;
import nbm.center.module.ModuleRepository;
import nbm.center.module.ModuleResource;

public class NbmCenterService extends Service<NbmCenterConfiguration> {
    private final HibernateBundle<NbmCenterConfiguration> hibernate = new HibernateBundle<NbmCenterConfiguration>(Module.class) {
        public DatabaseConfiguration getDatabaseConfiguration(NbmCenterConfiguration configuration) {
            return configuration.getDatabaseConfiguration();
        }
    };

    @Override
    public void initialize(Bootstrap<NbmCenterConfiguration> bootstrap) {
        bootstrap.setName("nbm-center");
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(new MigrationsBundle<NbmCenterConfiguration>() {
            public DatabaseConfiguration getDatabaseConfiguration(NbmCenterConfiguration configuration) {
                return configuration.getDatabaseConfiguration();
            }
        });
    }

    @Override
    public void run(NbmCenterConfiguration configuration, Environment environment) throws Exception {
        environment.addResource(new ModuleResource(new ModuleRepository(hibernate.getSessionFactory())));
    }

    public static void main(String[] args) throws Exception {
        new NbmCenterService().run(args);
    }
}
