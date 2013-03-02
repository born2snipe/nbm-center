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

import com.sun.jersey.core.header.FormDataContentDisposition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModuleResourceTest {
    @Mock
    private FormDataContentDisposition contentDisposition;
    @Mock
    private ModuleFactory factory;
    @Mock
    private ModuleRepository repository;
    @InjectMocks
    private ModuleResource resource;

    @Before
    public void setUp() throws Exception {
        resource.setFactory(factory);
    }


    @Test
    public void getById_noModuleFound() throws IOException {
        when(repository.findBinaryById(100)).thenReturn(null);

        assertNull(resource.getById(100));
    }

    @Test
    public void getById() throws IOException {
        MockInputStream input = new MockInputStream("result".getBytes());
        when(repository.findBinaryById(100)).thenReturn(input);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        resource.getById(100).write(output);

        assertEquals("result", new String(output.toByteArray()));
        assertTrue("should close the input stream", input.closed);
    }

    private class MockInputStream extends ByteArrayInputStream {
        private boolean closed = false;

        public MockInputStream(byte[] buf) {
            super(buf);
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }
    }
}
