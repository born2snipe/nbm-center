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

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void getAll() throws IOException {
        when(repository.findAll()).thenReturn(Arrays.asList(new Module(), new Module()));

        assertEquals(2, client().resource("/module/all").get(List.class).size());
    }

    @Test
    public void upload() throws IOException {
        Module moduleBuilt = new Module();
        when(factory.build(any(InputStream.class), any(FormDataContentDisposition.class))).thenReturn(moduleBuilt);

        FormDataMultiPart form = buildFileUploadForm();

        ClientResponse response = client().resource("/module").type(MediaType.MULTIPART_FORM_DATA).post(ClientResponse.class, form);

        assertEquals(200, response.getStatus());
        verify(repository).save(moduleBuilt);
    }

    @Test
    public void delete() {
        client().resource("/module/1.nbm").delete();

        verify(repository).delete(1);
    }

    private FormDataMultiPart buildFileUploadForm() throws IOException {
        File file = File.createTempFile("test", ".nbm");
        file.deleteOnExit();
        FormDataMultiPart form = new FormDataMultiPart();
        form.bodyPart(new FileDataBodyPart("file", file, MediaType.MULTIPART_FORM_DATA_TYPE));
        return form;
    }
}
