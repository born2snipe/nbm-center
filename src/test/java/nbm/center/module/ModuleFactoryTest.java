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

import static junit.framework.Assert.*;

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
        nbmBuilder.codebasename("code-base").distributionPath("null/file.nbm");

        Module module = factory.build(nbmBuilder.toInputStream());

        assertEquals("code-base", module.getCodenamebase());
        assertTrue(module.getInfoXml().contains("module/{id}"));
        assertFalse(module.getInfoXml().contains("null/file.nbm"));
    }
}
