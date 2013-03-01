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

import com.google.common.io.ByteStreams;
import com.yammer.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

@Path("/catalog.xml.gz")
public class CompressedCatalogResource {
    private final CatalogRepository repository;
    private CatalogFactory factory = new CatalogFactory();

    public CompressedCatalogResource(CatalogRepository repository) {
        this.repository = repository;
    }

    @GET
    @Produces("application/gzip")
    @UnitOfWork
    public Response getCompressedCatalog() throws Exception {
        byte[] catalogXml = compress(factory.build(repository.findAllEntries()));
        return Response
                .ok(new ByteArrayInputStream(catalogXml))
                .build();
    }

    private byte[] compress(String catalogXmlContent) throws IOException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        GZIPOutputStream output = new GZIPOutputStream(data);
        ByteStreams.copy(new ByteArrayInputStream(catalogXmlContent.getBytes()), output);
        output.close();
        return data.toByteArray();
    }

    public void setFactory(CatalogFactory factory) {
        this.factory = factory;
    }
}
