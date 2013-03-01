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

import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ModuleResourceEndpointTest extends ResourceTest {
    @Mock
    private ModuleRepository repository;
    @Mock
    private ModuleFactory factory;
    private ModuleResource resource;

    @Override
    protected void setUpResources() throws Exception {
        resource = new ModuleResource(repository);
        resource.setFactory(factory);

        addResource(resource);
    }

    @Test
    public void delete() {
        client().resource("/module/1.nbm").delete();

        verify(repository).delete(1);
    }
}
