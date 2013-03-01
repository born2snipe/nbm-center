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

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class CatalogFactoryTest {
    private CatalogFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new CatalogFactory();
    }

    @Test
    public void build_updatesTheModulesDistributionPath() {
        CatalogEntry entry = new CatalogEntry();
        entry.setId(100);
        entry.setInfoXml("<infoXml distribution=\"123\" />");

        String catalogXml = factory.build(Arrays.asList(entry));

        assertCatalogContainsEntry("<infoXml distribution=\"module/100.nbm\" />", catalogXml);
    }

    @Test
    public void build_entries() {
        CatalogEntry entry = new CatalogEntry();
        entry.setInfoXml("<infoXml/>");

        CatalogEntry otherEntry = new CatalogEntry();
        otherEntry.setInfoXml("<infoXml-2/>");

        String catalogXml = factory.build(Arrays.asList(entry, otherEntry));

        assertCatalogContainsEntry("<infoXml/>", catalogXml);
        assertCatalogContainsEntry("<infoXml-2/>", catalogXml);
    }

    @Test
    public void build_emptyCatalog() {
        String catalogTimestamp = makeCatalogTimestampFrom(new DateTime());

        String catalogXml = factory.build(new ArrayList());

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE module_updates PUBLIC \"-//NetBeans//DTD Autoupdate Catalog 2.7//EN\" \"http://www.netbeans.org/dtds/autoupdate-catalog-2_7.dtd\">\n" +
                "<module_updates timestamp=\"" + catalogTimestamp + "\">" +
                "\n</module_updates>",
                catalogXml);
    }

    private void assertCatalogContainsEntry(String expectedXml, String catalogXml) {
        assertTrue("could not locate module's xml", catalogXml.contains(expectedXml));
        assertTrue("module's xml should be within the module_updates",
                catalogXml.indexOf("<module_updates") < catalogXml.indexOf(expectedXml));
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
