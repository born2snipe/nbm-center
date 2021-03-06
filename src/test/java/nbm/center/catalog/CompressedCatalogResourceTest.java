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

import com.google.common.io.ByteStreams;
import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompressedCatalogResourceTest extends ResourceTest {
    @Mock
    private CatalogXmlFactory factory;
    private CompressedCatalogResource resource;

    @Override
    protected void setUpResources() throws Exception {
        resource = new CompressedCatalogResource(factory);

        when(factory.build()).thenReturn("catalog-content");

        addResource(resource);
    }

    @Test
    public void getCompressedCatalog() throws IOException {
        InputStream inputStream = client().resource("/catalog.xml.gz").get(InputStream.class);

        assertNotNull(inputStream);
        assertEquals("catalog-content", new String(ByteStreams.toByteArray(new GZIPInputStream(inputStream))));
    }
}
