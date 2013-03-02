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
import nbm.center.NbmFileBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModuleFactoryTest {
    @Mock
    private FormDataContentDisposition contentDisposition;
    private ModuleFactory factory;
    private NbmFileBuilder nbmBuilder;

    @Before
    public void setUp() throws Exception {
        factory = new ModuleFactory();
        nbmBuilder = new NbmFileBuilder();
    }

    @Test
    public void build() {
        nbmBuilder.codebasename("code-base");
        when(contentDisposition.getFileName()).thenReturn("file-name");

        Module module = factory.build(nbmBuilder.toInputStream(), contentDisposition);

        assertEquals("file-name", module.getOriginalFilename());
        assertEquals("code-base", module.getCodenamebase());
        assertFalse(module.getInfoXml().contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertFalse(module.getInfoXml().contains("<!DOCTYPE module PUBLIC \"-//NetBeans//DTD Autoupdate Module Info 2.4//EN\" \"http://www.netbeans.org/dtds/autoupdate-info-2_4.dtd\">"));
    }
}
