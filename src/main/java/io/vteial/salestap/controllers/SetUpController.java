package io.vteial.salestap.controllers;

import io.vteial.salestap.dtos.SetUpDto;
import io.vteial.salestap.dtos.UserDto;
import io.vteial.salestap.models.User;
import io.vteial.salestap.services.SetUpService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Map;

@Slf4j
@Path("/api/set-up")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
public class SetUpController {

    @Inject
    SetUpService setUpService;

    @GET
    @Path("/current-state")
    public SetUpDto currentState() {
        return setUpService.getCurrentState();
    }

    @POST
    @Path("/register-owner")
    public Response register(User item) {
        item = setUpService.registerOwner(item);
        return Response.ok(item).status(201).build();
    }
}
