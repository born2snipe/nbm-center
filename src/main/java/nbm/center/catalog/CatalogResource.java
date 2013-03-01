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


import com.yammer.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/catalog.xml")
public class CatalogResource {
    private CatalogFactory factory = new CatalogFactory();
    private final CatalogRepository repository;

    public CatalogResource(CatalogRepository repository) {
        this.repository = repository;
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @UnitOfWork
    public Response getCatalog() {
        return Response
                .ok(factory.build(repository.findAllEntries()))
                .build();
    }

    public void setFactory(CatalogFactory factory) {
        this.factory = factory;
    }
}
