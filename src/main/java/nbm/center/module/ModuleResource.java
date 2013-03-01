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

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.yammer.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.InputStream;

@Path("/module")
public class ModuleResource {
    private final ModuleRepository repository;
    private ModuleFactory factory = new ModuleFactory();

    public ModuleResource(ModuleRepository repository) {
        this.repository = repository;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    public Response upload(@FormDataParam("file") InputStream fileContents, @FormDataParam("file") FormDataContentDisposition disposition) {
        repository.save(factory.build(fileContents));
        return Response.ok().build();
    }

    @Path("/{id}.nbm")
    @GET
    @Consumes
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @UnitOfWork(transactional = false, readOnly = true)
    public StreamingOutput getById(@PathParam("id") final int id) {
        System.out.println("ModuleResource.getById");
        InputStream inputStream = repository.findBinaryById(id);
        if (inputStream == null) {
            return null;
        }
        return new InputStreamOutput(inputStream);
    }

    public void setFactory(ModuleFactory factory) {
        this.factory = factory;
    }
}
