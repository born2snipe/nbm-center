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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class NbmFileBuilder {
    private String codebasename = "";
    private String distributionPath = "";

    public NbmFileBuilder codebasename(String name) {
        this.codebasename = name;
        return this;
    }

    public NbmFileBuilder distributionPath(String distributionPath) {
        this.distributionPath = distributionPath;
        return this;
    }

    public InputStream toInputStream() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            ZipOutputStream zip = new ZipOutputStream(output);
            zip.putNextEntry(new ZipEntry("Info/info.xml"));
            zip.write(infoXml());
            zip.closeEntry();
            zip.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(output.toByteArray());
    }

    private byte[] infoXml() {
        String xml = "<module " +
                "codenamebase=\"" + codebasename + "\" " +
                "distribution=\"" + distributionPath + "\" " +
                "downloadsize=\"442004\" " +
                "targetcluster=\"extra\">\n" +
                "    <manifest AutoUpdate-Show-In-Client=\"false\" " +
                "       OpenIDE-Module=\"com.atlassian.connector.eclipse.jira.core\" " +
                "       OpenIDE-Module-Module-Dependencies=\"javax.xml.rpc &gt; 1.1.0, javax.xml.soap &gt; 1.2.0, org.apache.axis &gt; 1.4.0, org.apache.commons.io, org.eclipse.core.runtime, org.eclipse.mylyn.commons.core &gt; 3.0.0, org.eclipse.mylyn.commons.net &gt; 3.0.0, org.eclipse.mylyn.commons.soap &gt; 3.0.0, org.eclipse.mylyn.tasks.core &gt; 3.0.0, com.atlassian.connector.eclipse.commons.core\" " +
                "       OpenIDE-Module-Name=\"Atlassian Connector for Eclipse Jira Core\" " +
                "       OpenIDE-Module-Specification-Version=\"3.0.4\"/>\n" +
                "</module>";

        return xml.getBytes();
    }
}
