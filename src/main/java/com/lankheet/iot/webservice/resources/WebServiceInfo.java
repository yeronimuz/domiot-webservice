package com.lankheet.iot.webservice.resources;

import java.io.IOException;
import java.util.jar.Manifest;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WebServiceInfo {
    Manifest manifest;

    /**
     * Constructor.<BR>
     * When running from Eclipse http://localhost:8080/info will return the name and version of the JRE.
     * 
     * @throws IOException
     */
    public WebServiceInfo() throws IOException {
        manifest = new Manifest();
        manifest.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
    }

    @JsonProperty
    public String getApplicationTitle() {
        return manifest.getMainAttributes().getValue("Implementation-Title");
    }

    @JsonProperty
    public String getVersion() {
        return manifest.getMainAttributes().getValue("Implementation-Version");
    }
}
