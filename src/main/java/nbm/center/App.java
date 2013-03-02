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
package nbm.center;

import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class App {
    public static void main(String args[]) throws Exception {
        String configFilePath = "";
        if (args.length == 0) {
            System.out.println("Using default setup and configuration provided");
            configFilePath = extractDefaultConfiguration().getAbsolutePath();
            System.out.println("Temp config file: " + configFilePath);
            migrateAndRun(configFilePath);
        } else if (args.length == 2 && "migrateAndRun".equals(args[0])) {
            migrateAndRun(args[0]);
        } else {
            new NbmCenterService().run(args);
        }

    }

    private static void migrateAndRun(String configFilePath) throws Exception {
        NbmCenterService service = new NbmCenterService();
        service.run(new String[]{"db", "migrate", configFilePath});
        service.run(new String[]{"server", configFilePath});
    }

    private static File extractDefaultConfiguration() throws IOException {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("default.yml");
        File tempConfigFile = File.createTempFile("default", ".yml");
        tempConfigFile.deleteOnExit();
        FileOutputStream output = new FileOutputStream(tempConfigFile);
        try {
            ByteStreams.copy(input, output);
        } finally {
            output.close();
        }
        return tempConfigFile;
    }
}
