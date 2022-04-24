package io.vteial.salestap.controllers;

import io.vteial.salestap.dtos.ResponseDto;
import io.vteial.salestap.dtos.SetUpDto;
import io.vteial.salestap.dtos.UserDto;
import io.vteial.salestap.models.Shop;
import io.vteial.salestap.models.User;
import io.vteial.salestap.services.SetUpService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
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
    @Path("/auth")
    public ResponseDto auth(UserDto item) {
        ResponseDto response = ResponseDto.builder().build();
        if (setUpService.auth(item.getPassword())) {
            response.setType(ResponseDto.SUCCESS);
        } else {
            response.setType(ResponseDto.ERROR);
            response.setMessage("Invalid Password");
        }
        return response;
    }

    @POST
    @Path("/register-owner")
    public ResponseDto registerOwner(User item) {
        ResponseDto response = ResponseDto.builder().type(ResponseDto.SUCCESS).build();
        item.setPassword(item.getToken());
        item.setToken(null);
        item = setUpService.registerOwner(item);
        log.info("{}", item);
        setUpService.markRegisterOwnerCompleted();
        response.setData(Map.of("setUpInfo", setUpService.getCurrentState(), "ownerInfo", item));
        return response;
    }

    @POST
    @Path("/create-shop")
    public ResponseDto createShop(Shop item) {
        ResponseDto response = ResponseDto.builder().type(ResponseDto.SUCCESS).build();
        item = setUpService.createShop(item);
        log.info("{}", item);
        setUpService.markCreateShopCompleted();
        response.setData(Map.of("setUpInfo", setUpService.getCurrentState(), "shopInfo", item));
        return response;
    }

    @GET
    @Path("/accept-terms-and-conditions")
    public ResponseDto findById(@QueryParam("atc") Boolean atc) {
        ResponseDto response = ResponseDto.builder().build();
        if(atc) {
            response.setType(ResponseDto.SUCCESS);
            response.setData(setUpService.getCurrentState());
        }
        else {
            response.setType(ResponseDto.ERROR);
            response.setMessage("You must accept the terms and conditions...");
        }
        return response;
    }
}