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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputStreamOutput implements StreamingOutput {
    private final InputStream inputStream;

    public InputStreamOutput(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void write(OutputStream output) throws IOException, WebApplicationException {
        byte[] buffer = new byte[1024 * 10];
        int length = 0;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
        } finally {
            inputStream.close();
        }
    }
}
