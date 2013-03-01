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
package nbm.center.catalog;

import liquibase.util.StringUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class CatalogFactory {
    public String build(List<CatalogEntry> catalogEntries) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append(makeCatalogXmlBeginning()).append("\n");
        xmlBuilder.append(makeCatalogXmlEntries(catalogEntries));
        xmlBuilder.append(makeCatalogXmlEnding());
        return xmlBuilder.toString();
    }

    private String makeCatalogXmlEntries(List<CatalogEntry> catalogEntries) {
        ArrayList<String> moduleXmlTags = new ArrayList<String>();
        for (CatalogEntry catalogEntry : catalogEntries) {
            moduleXmlTags.add(makeModuleXmlFor(catalogEntry));
        }
        return StringUtils.join(moduleXmlTags, "\n");
    }

    private String makeModuleXmlFor(CatalogEntry catalogEntry) {
        return catalogEntry.getInfoXml().replaceAll("distribution=\".+?\"", "distribution=\"module/" + catalogEntry.getId() + ".nbm\"");
    }

    private String makeCatalogXmlEnding() {
        return "</module_updates>";
    }

    private String makeCatalogXmlBeginning() {
        String catalogTimestamp = makeCatalogTimestampFrom(new DateTime());
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE module_updates PUBLIC \"-//NetBeans//DTD Autoupdate Catalog 2.7//EN\" \"http://www.netbeans.org/dtds/autoupdate-catalog-2_7.dtd\">\n" +
                "<module_updates timestamp=\"" + catalogTimestamp + "\">";
    }

    private String makeCatalogTimestampFrom(DateTime today) {
        String catalogTimestamp = "";
        catalogTimestamp += "00" + "/";
        catalogTimestamp += today.getMinuteOfHour() + "/";
        catalogTimestamp += today.getHourOfDay() + "/";
        catalogTimestamp += today.getDayOfMonth() + "/";
        catalogTimestamp += today.getMonthOfYear() + "/";
        catalogTimestamp += today.getYear();
        return catalogTimestamp;
    }
}
