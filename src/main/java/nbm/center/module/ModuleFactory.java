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

import com.google.common.io.ByteStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModuleFactory {
    private static final Pattern CODEBASENAME_PATTERN = Pattern.compile("codenamebase=\"(.+?)\"");

    public Module build(InputStream input) {
        byte[] fileContents = readFileContents(input);
        String infoXml = readModuleInfoFrom(fileContents);
        infoXml = removeNoLongerNeededXmlTags(infoXml);

        Module module = new Module();
        module.setCodenamebase(readCodebaseNameFrom(infoXml));
        module.setInfoXml(infoXml);
        module.setFileContents(fileContents);
        return module;
    }

    private String removeNoLongerNeededXmlTags(String infoXml) {
        return infoXml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
                .replace("<!DOCTYPE module PUBLIC \"-//NetBeans//DTD Autoupdate Module Info 2.4//EN\" \"http://www.netbeans.org/dtds/autoupdate-info-2_4.dtd\">", "");
    }

    private String readModuleInfoFrom(byte[] fileContents) {
        ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(fileContents));
        try {
            ZipEntry entry = null;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().endsWith("Info/info.xml")) {
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length = -1;
                    while ((length = zip.read(buffer, 0, buffer.length)) != -1) {
                        output.write(buffer, 0, length);
                    }
                    return new String(output.toByteArray());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("A problem occurred when trying to extract the info.xml");
        } finally {
            try {
                zip.close();
            } catch (IOException e) {

            }
        }

        throw new IllegalStateException("No info.xml file found in the NBM");
    }

    private String readCodebaseNameFrom(String fileContents) {
        Matcher matcher = CODEBASENAME_PATTERN.matcher(removeLineEndings(fileContents));
        matcher.find();
        return matcher.group(1);
    }

    private String removeLineEndings(String fileContents) {
        return fileContents.replaceAll("\r\n|\n", "");
    }

    private byte[] readFileContents(InputStream input) {
        try {
            return ByteStreams.toByteArray(input);
        } catch (IOException e) {
            throw new RuntimeException("A problem occurred while trying to read the file contents", e);
        }
    }
}
