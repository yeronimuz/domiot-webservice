package org.domiot.webservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiDocumentationConfig {

    private static final String DEFAULT_SENTINEL = "##default";
    private static final String APPLICATION_JSON = "application/json";

    @Bean
    public OpenApiCustomizer requestBodySchemaCustomizer() {
        return openApi -> {
            sanitizeComponentSchemas(openApi);
            customizeRequestBodies(openApi);
        };
    }

    private void sanitizeComponentSchemas(OpenAPI openApi) {
        if (openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) {
            return;
        }
        for (Schema<?> schema : openApi.getComponents().getSchemas().values()) {
            sanitizeSchema(schema);
        }
    }

    private void customizeRequestBodies(OpenAPI openApi) {
        setRequestBody(openApi, "/user", PathItem.HttpMethod.POST,
                refSchema("User"),
                exampleUser());
        setRequestBody(openApi, "/user/{userId}", PathItem.HttpMethod.PUT,
                refSchema("User"),
                exampleUser());

        setRequestBody(openApi, "/site", PathItem.HttpMethod.POST,
                refSchema("Site"),
                exampleSite());
        setRequestBody(openApi, "/site/{siteId}", PathItem.HttpMethod.PUT,
                refSchema("Site"),
                exampleSite());

        setRequestBody(openApi, "/site/{siteId}/devices", PathItem.HttpMethod.POST,
                arrayRefSchema("Device"),
                List.of(exampleDevice()));
        setRequestBody(openApi, "/site/{siteId}/devices/{deviceId}", PathItem.HttpMethod.PUT,
                refSchema("Device"),
                exampleDevice());
    }

    private void setRequestBody(OpenAPI openApi, String path, PathItem.HttpMethod method, Schema<?> schema, Object example) {
        if (openApi.getPaths() == null) {
            return;
        }
        PathItem pathItem = openApi.getPaths().get(path);
        if (pathItem == null) {
            return;
        }

        Operation operation = pathItem.readOperationsMap().get(method);
        if (operation == null || operation.getRequestBody() == null) {
            return;
        }

        Content content = operation.getRequestBody().getContent();
        if (content == null) {
            return;
        }

        MediaType mediaType = content.get(APPLICATION_JSON);
        if (mediaType == null) {
            return;
        }

        mediaType.setSchema(schema);
        mediaType.setExample(example);
        sanitizeSchema(schema);
    }

    private Schema<?> refSchema(String componentName) {
        return new Schema<>().$ref("#/components/schemas/" + componentName);
    }

    private ArraySchema arrayRefSchema(String componentName) {
        return new ArraySchema().items(refSchema(componentName));
    }

    @SuppressWarnings("unchecked")
    private void sanitizeSchema(Schema<?> schema) {
        if (schema == null) {
            return;
        }

        if (DEFAULT_SENTINEL.equals(schema.getDefault())) {
            schema.setDefault(null);
        }
        if (DEFAULT_SENTINEL.equals(schema.getExample())) {
            schema.setExample(null);
        }

        if (schema instanceof ArraySchema arraySchema) {
            sanitizeSchema(arraySchema.getItems());
        }

        if (schema.getProperties() != null) {
            for (Object property : schema.getProperties().values()) {
                if (property instanceof Schema<?> propertySchema) {
                    sanitizeSchema(propertySchema);
                }
            }
        }

        if (schema.getAdditionalProperties() instanceof Schema<?> additionalPropertiesSchema) {
            sanitizeSchema(additionalPropertiesSchema);
        }

        if (schema.getAllOf() != null) {
            schema.getAllOf().forEach(this::sanitizeSchema);
        }
        if (schema.getAnyOf() != null) {
            schema.getAnyOf().forEach(this::sanitizeSchema);
        }
        if (schema.getOneOf() != null) {
            schema.getOneOf().forEach(this::sanitizeSchema);
        }
        if (schema.getNot() != null) {
            sanitizeSchema(schema.getNot());
        }
    }

    private Map<String, Object> exampleUser() {
        Map<String, Object> example = new LinkedHashMap<>();
        example.put("username", "jane.doe");
        example.put("firstName", "Jane");
        example.put("lastName", "Doe");
        example.put("email-address", "jane.doe@example.com");
        example.put("password", "changeMe");
        example.put("status", "ACTIVE");
        return example;
    }

    private Map<String, Object> exampleSite() {
        Map<String, Object> example = new LinkedHashMap<>();
        example.put("name", "Main office");
        example.put("description", "Primary building automation site");
        return example;
    }

    private Map<String, Object> exampleDevice() {
        Map<String, Object> example = new LinkedHashMap<>();
        example.put("manufacturerId", "Acme");
        example.put("modelId", "thermostat-x1");
        example.put("firmwareVersion", "1.0.0");
        example.put("hardwareVersion", "rev-a");
        example.put("macAddress", "AA:BB:CC:DD:EE:FF");
        return example;
    }
}

