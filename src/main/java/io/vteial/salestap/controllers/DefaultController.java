package io.vteial.salestap.controllers;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.vteial.salestap.services.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static java.util.Objects.requireNonNull;

@Slf4j
@Path("/")
public class DefaultController {

    private final Template index;

    @ConfigProperty(name = "app.name")
    private String appName = "-";

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
        log.debug("serving ping...");
        return "Ping Pong!!!";
    }

    @Inject
    private TaskService taskService;

    @GET
    @Path("testCounter")
    @Produces(MediaType.TEXT_PLAIN)
    public String testCounter() {
        return String.valueOf(taskService.getTestCounter());
    }

}
