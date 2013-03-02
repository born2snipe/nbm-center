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
import org.joda.time.DateTime;
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
    public void save_updatesExistingRecordsUpdateDate() throws IOException {
        DateTime now = new DateTime();
        repository.save(module("code-name", now.minusDays(1), "data"));
        DateTime result = repository.save(module("code-name", now, "updated-data")).getUpdateDate();

        assertEquals(now.year(), result.year());
        assertEquals(now.monthOfYear(), result.monthOfYear());
        assertEquals(now.dayOfMonth(), result.dayOfMonth());
    }

    @Test
    public void save_updatesExistingRecords() throws IOException {
        repository.save(module("code-name", "data"));
        Module result = repository.save(module("code-name", "updated-data"));

        assertEquals("updated-data", new String(result.getFileContents()));
        assertEquals("updated-data".length(), result.getFileSize());
    }

    @Test
    public void delete_successfully() throws IOException {
        Module module = module("code-name", "data");
        repository.save(module);

        repository.delete(module.getId());
        assertNull(repository.findBinaryById(module.getId()));
    }

    @Test
    public void findBinaryById_noMatchingModule() throws IOException {
        assertNull(repository.findBinaryById(-1));
    }

    @Test
    public void findBinaryById() throws IOException {
        Integer id = repository.save(module("x", "data")).getId();

        InputStream inputStream = repository.findBinaryById(id);

        assertEquals("data", new String(ByteStreams.toByteArray(inputStream)));
    }

    private Module module(String codenamebase, String data) {
        DateTime updateDate = new DateTime();
        return module(codenamebase, updateDate, data);
    }

    private Module module(String codenamebase, DateTime updateDate, String data) {
        Module module = new Module();
        module.setFileContents(data.getBytes());
        module.setCodenamebase(codenamebase);
        module.setInfoXml("info.xml");
        module.setUpdateDate(updateDate);
        module.setOriginalFilename("original");
        return module;
    }
}
