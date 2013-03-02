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
import com.yammer.metrics.Metrics;
import com.yammer.metrics.annotation.Timed;
import com.yammer.metrics.core.Histogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.InputStream;

@Path("/module")
public class ModuleResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleResource.class);
    private final Histogram fileSizes = Metrics.newHistogram(ModuleResource.class, "file-sizes-in-bytes");
    private final ModuleRepository repository;
    private ModuleFactory factory = new ModuleFactory();

    public ModuleResource(ModuleRepository repository) {
        this.repository = repository;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    @Timed
    public Response upload(@FormDataParam("file") InputStream fileContents, @FormDataParam("file") FormDataContentDisposition disposition) {
        Module module = factory.build(fileContents, disposition);
        LOGGER.info("Uploading module: codenamebase=[" + module.getCodenamebase() + "], fileSize=" + module.getFileSize() + " byte(s)");
        fileSizes.update(module.getFileSize());
        repository.save(module);
        return Response.ok().build();
    }

    @Path("/{id}.nbm")
    @GET
    @Consumes
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @UnitOfWork(transactional = false, readOnly = true)
    @Timed
    public StreamingOutput getById(@PathParam("id") final int id) {
        InputStream inputStream = repository.findBinaryById(id);
        if (inputStream == null) {
            return null;
        }
        return new InputStreamOutput(inputStream);
    }

    @Path("/{id}.nbm")
    @DELETE
    @Timed
    public void delete(@PathParam("id") int id) {
        repository.delete(id);
    }

    public void setFactory(ModuleFactory factory) {
        this.factory = factory;
    }
}
