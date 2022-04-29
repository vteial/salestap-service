package io.vteial.salestap.controllers;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.vteial.salestap.services.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@Slf4j
@Path("/")
@ApplicationScoped
public class DefaultController {

    private final Template index;

    @ConfigProperty(name = "app.name")
    String appName = "-";

    public DefaultController(Template index) {
        this.index = requireNonNull(index, "index template is required");
    }

    @GET
    @Path("index")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return index.data("appName", this.appName);
    }

    @GET
    @Path("ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "Ping Pong!!!";
    }

    @GET
    @Path("api/app-info")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> appInfo() {
        Map<String, Object> appInfo = Map.of(
                "mode",io.quarkus.runtime.LaunchMode.current(),
                "name", appName
        );
        return appInfo;
    }

    @Inject
    TaskService taskService;

    @GET
    @Path("testCounter")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCounter() {
        return String.valueOf(taskService.getTestCounter());
    }

//    @GET
//    @Path("fail-with-individual-exception")
//    public Response failWithIndividualResponse() {
//        return Response.status(Response.Status.NOT_FOUND)
//                .type(MediaType.TEXT_PLAIN_TYPE)
//                .entity("Error Message")
//                .build();
//    }
//
//    @GET
//    @Path("fail-with-generic-exception")
//    public Response failWithGenericResponse() {
//        throw new NotFoundException("Not found...");
//    }

}
