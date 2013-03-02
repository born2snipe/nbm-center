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
package nbm.center.catalog;

import nbm.center.RepositoryTest;
import nbm.center.module.Module;
import nbm.center.module.ModuleRepository;
import org.hibernate.SessionFactory;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class CatalogRepositoryTest extends RepositoryTest {
    private CatalogRepository catalogRepository;
    private ModuleRepository moduleRepository;

    @Override
    protected void initRepository(SessionFactory sessionFactory) {
        catalogRepository = new CatalogRepository(sessionFactory);
        moduleRepository = new ModuleRepository(sessionFactory);
    }

    @Test
    public void findAllEntries() {
        module("1");
        module("2");

        List<CatalogEntry> entries = catalogRepository.findAllEntries();

        assertEquals(2, entries.size());
    }

    private Module module(String infoXml) {
        Module module = new Module();
        module.setCodenamebase(infoXml);
        module.setInfoXml(infoXml);
        module.setFileContents(new byte[0]);
        module.setOriginalFilename("original");
        Module savedModule = moduleRepository.save(module);
        session.flush();
        return savedModule;
    }
}
