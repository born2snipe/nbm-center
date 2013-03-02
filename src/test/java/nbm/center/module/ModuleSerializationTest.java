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

import com.yammer.dropwizard.testing.JsonHelpers;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class ModuleSerializationTest {
    @Test
    public void serialize() throws IOException {
        Module module = new Module();
        module.setOriginalFilename("original_filename");
        module.setFileContents("file-contents".getBytes());
        module.setCodenamebase("codenamebase");
        module.setInfoXml("info-xml");
        module.setId(10);
        module.setUpdateDate(new DateTime(1234));

        assertEquals("{\"id\":10,\"codenamebase\":\"codenamebase\",\"updateDate\":1234,\"originalFilename\":\"original_filename\"}",
                JsonHelpers.asJson(module));
    }
}
