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
package nbm.center.module;

import com.google.common.io.ByteStreams;
import nbm.center.RepositoryTest;
import org.hibernate.SessionFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class ModuleRepositoryTest extends RepositoryTest {
    private ModuleRepository repository;

    @Override
    protected void initRepository(SessionFactory sessionFactory) {
        repository = new ModuleRepository(sessionFactory);
    }

    @Test
    public void save_updatesExistingRecords() throws IOException {
        repository.save(module("code-name", "data"));
        repository.save(module("code-name", "updated-data"));

        assertEquals("updated-data", new String(ByteStreams.toByteArray(repository.findBinaryById(1))));
    }

    @Test
    public void findBinaryById_noMatchingModule() throws IOException {
        assertNull(repository.findBinaryById(-1));
    }

    @Test
    public void findBinaryById() throws IOException {
        repository.save(module("x", "data"));
        Integer id = (Integer) session.createQuery("select m.id from Module m").list().get(0);

        InputStream inputStream = repository.findBinaryById(id);

        assertEquals("data", new String(ByteStreams.toByteArray(inputStream)));
    }

    private Module module(String codenamebase, String data) {
        Module module = new Module();
        module.setFileContents(data.getBytes());
        module.setCodenamebase(codenamebase);
        module.setInfoXml("info.xml");
        return module;
    }
}
