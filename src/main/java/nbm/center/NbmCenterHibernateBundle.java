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

import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import nbm.center.catalog.CatalogEntry;
import nbm.center.module.Module;

public class NbmCenterHibernateBundle extends HibernateBundle<NbmCenterConfiguration> {
    public NbmCenterHibernateBundle() {
        super(Module.class, CatalogEntry.class);
    }

    @Override
    public DatabaseConfiguration getDatabaseConfiguration(NbmCenterConfiguration configuration) {
        return configuration.getDatabaseConfiguration();
    }
}
