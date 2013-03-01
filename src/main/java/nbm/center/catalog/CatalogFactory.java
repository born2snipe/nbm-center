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
        DateTime timestamp = findLatestUpdateDate(catalogEntries);
        xmlBuilder.append(makeCatalogXmlBeginning(timestamp)).append("\n");
        xmlBuilder.append(makeCatalogXmlEntries(catalogEntries));
        xmlBuilder.append(makeCatalogXmlEnding());
        return xmlBuilder.toString();
    }

    private DateTime findLatestUpdateDate(List<CatalogEntry> catalogEntries) {
        if (catalogEntries.isEmpty())
            return new DateTime();

        DateTime latestUpdate = catalogEntries.get(0).getUpdateDate();
        for (int i = 1; i < catalogEntries.size(); i++) {
            DateTime current = catalogEntries.get(i).getUpdateDate();
            if (latestUpdate.isBefore(current)) {
                latestUpdate = current;
            }
        }
        return latestUpdate;
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

    private String makeCatalogXmlBeginning(DateTime timestamp) {
        String catalogTimestamp = makeCatalogTimestampFrom(timestamp);
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
