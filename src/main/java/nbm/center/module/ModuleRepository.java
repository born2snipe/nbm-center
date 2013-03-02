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

import com.yammer.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class ModuleRepository extends AbstractDAO<Module> {
    public ModuleRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public InputStream findBinaryById(int id) {
        Query query = currentSession().createQuery("select m from Module m where m.id = ?");
        query.setParameter(0, id);

        List<Module> modules = list(query);
        if (modules.isEmpty()) {
            return null;
        }
        return new ByteArrayInputStream(modules.get(0).getFileContents());
    }

    public Module save(Module module) {
        Module existingModule = findByCodenamebase(module.getCodenamebase());
        if (existingModule != null) {
            existingModule.setFileContents(module.getFileContents());
            existingModule.setUpdateDate(module.getUpdateDate());
        } else {
            existingModule = module;
        }
        return persist(existingModule);
    }

    public void delete(int id) {
        Query query = currentSession().createQuery("delete from Module m where m.id = ?");
        query.setParameter(0, id);
        query.executeUpdate();
    }

    private Module findByCodenamebase(String codenamebase) {
        Query query = currentSession().createQuery("select m from Module m where m.codenamebase = ?");
        query.setParameter(0, codenamebase);
        return uniqueResult(query);
    }

    public List<Module> findAll() {
        return list(currentSession().createQuery("select m from Module m"));
    }
}
