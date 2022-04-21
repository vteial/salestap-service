package io.vteial.salestap.controllers;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import io.vteial.salestap.dtos.ResponseDto;
import io.vteial.salestap.models.AppConfig;
import io.vteial.salestap.services.AppConfigService;


@Slf4j
@Path("/app-config")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
public class AppConfigController {

    @Inject
    AppConfigService appConfigService;

    @GET
    public List<AppConfig> list() {
        return appConfigService.listAll();
    }

    @GET
    @Path("{key}")
    public Map<String, String> getValue(@PathParam("key") String key) {
        return Map.of(key, appConfigService.getValue(key));
    }

    @POST
    public Response setValue(Map<String, String> map) {
        appConfigService.setValue(map.get("key"), map.get("val"));
        return Response.ok().status(200).build();
    }

    @DELETE
    @Path("{key}")
    public Response remove(@PathParam("key") String key) {
        ResponseDto responseDto = ResponseDto.builder()
                .type(ResponseDto.WARNING)
                .message("Not yet implemented")
                .build();
        return Response.ok(responseDto).status(200).build();
    }

}
