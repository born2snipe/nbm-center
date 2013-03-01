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


import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Module {
    @Id
    @GeneratedValue
    private int id;

    private byte[] fileContents;
    private String codenamebase;
    private String infoXml;
    private DateTime updateDate = new DateTime();
    private int fileSize;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFileContents(byte[] fileContents) {
        this.fileContents = fileContents;
        this.fileSize = fileContents.length;
    }

    public byte[] getFileContents() {
        return fileContents;
    }

    public void setCodenamebase(String codenamebase) {
        this.codenamebase = codenamebase;
    }

    public String getCodenamebase() {
        return codenamebase;
    }

    public String getInfoXml() {
        return infoXml;
    }

    public void setInfoXml(String infoXml) {
        this.infoXml = infoXml;
    }

    public void setUpdateDate(DateTime updateDate) {
        this.updateDate = updateDate;
    }

    public DateTime getUpdateDate() {
        return updateDate;
    }

    public int getFileSize() {
        return fileSize;
    }
}
