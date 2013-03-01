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

import nbm.center.NbmFileBuilder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class ModuleFactoryTest {

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

        Module module = factory.build(nbmBuilder.toInputStream());

        assertEquals("code-base", module.getCodenamebase());
        assertFalse(module.getInfoXml().contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertFalse(module.getInfoXml().contains("<!DOCTYPE module PUBLIC \"-//NetBeans//DTD Autoupdate Module Info 2.4//EN\" \"http://www.netbeans.org/dtds/autoupdate-info-2_4.dtd\">"));
    }
}
