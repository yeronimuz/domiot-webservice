package com.lankheet.iot.webservice.resources;

import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.codahale.metrics.annotation.Timed;

@Path("/info")
@Produces(MediaType.APPLICATION_JSON)
public class WebServiceInfoResource {
    private WebServiceInfo webServiceInfo;

    public WebServiceInfoResource(WebServiceInfo webServiceInfo) {
        this.webServiceInfo = webServiceInfo;
    }

    /**
     * Returns application and version info of the WebService.
     * 
     * @return WebService info; version and description
     * @throws IOException Manifest of this JAR could not be read
     */
    @GET
    @Timed
    public WebServiceInfo webServiceInfo() throws IOException {
        return webServiceInfo;
    }
}
