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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompressedCatalogResourceTest {
    @Mock
    private CatalogFactory factory;
    @Mock
    private CatalogRepository repository;
    @InjectMocks
    private CompressedCatalogResource resource;

    @Before
    public void setUp() throws Exception {
        resource.setFactory(factory);

    }

    @Test
    public void getCompressedCatalog() throws Exception {
        List<CatalogEntry> entries = Arrays.asList(new CatalogEntry());
        when(repository.findAllEntries()).thenReturn(entries);
        when(factory.build(entries)).thenReturn("catalog-content");

        Response response = resource.getCompressedCatalog();

        assertEquals(200, response.getStatus());
        assertEquals("catalog-content", new String(readCompressedBytesFrom(response)));
    }

    private byte[] readCompressedBytesFrom(Response response) throws IOException {
        return ByteStreams.toByteArray(new GZIPInputStream((InputStream) response.getEntity()));
    }
}
