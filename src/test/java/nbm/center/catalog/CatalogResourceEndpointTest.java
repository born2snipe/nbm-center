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

import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CatalogResourceEndpointTest extends ResourceTest {
    @Mock
    private CatalogRepository repository;
    @Mock
    private CatalogFactory factory;

    @Override
    protected void setUpResources() throws Exception {
        CatalogResource resource = new CatalogResource(repository);
        resource.setFactory(factory);

        when(repository.findAllEntries()).thenReturn(new ArrayList());
        when(factory.build(new ArrayList())).thenReturn("catalog-content");

        addResource(resource);
    }

    @Test
    public void uncompressedCatalog() {
        assertEquals("catalog-content", client().resource("/catalog.xml").get(String.class));
    }
}
