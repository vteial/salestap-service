package io.vteial.salestap.controllers;

import io.vteial.salestap.dtos.ResponseDto;
import io.vteial.salestap.dtos.SetUpDto;
import io.vteial.salestap.dtos.UserDto;
import io.vteial.salestap.models.Shop;
import io.vteial.salestap.models.User;
import io.vteial.salestap.services.SetUpService;
import io.vteial.salestap.utils.Helper;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;

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
            response.setData(setUpService.getCurrentState());
        } else {
            response.setType(ResponseDto.ERROR);
            response.setMessage("Invalid Password");
        }
        return response;
    }

    @POST
    @Path("/register-owner")
    public ResponseDto registerOwner(User item) {
        ResponseDto response = ResponseDto.builder().build();
        if (checkAuthentication()) {
            response.setType(ResponseDto.ERROR);
            response.setMessage("You are not authorized to use this end point.");
            return response;
        }
        item.setPassword(item.getToken());
        item.setToken(null);
        try {
            response.setData(setUpService.registerOwner(item));
            response.setType(ResponseDto.SUCCESS);
        } catch (ConstraintViolationException cve) {
            response.setType(ResponseDto.ERROR);
            response.setMessage("Owner should have valid values...");
            response.setData(Helper.covertCVException(cve));
        }
        return response;
    }

    @POST
    @Path("/create-shop")
    public ResponseDto createShop(Shop item) {
        ResponseDto response = ResponseDto.builder().build();
        if (checkAuthentication()) {
            response.setType(ResponseDto.ERROR);
            response.setMessage("You are not authorized to use this end point.");
            return response;
        }
        try {
            response.setData(setUpService.createShop(item));
            response.setType(ResponseDto.SUCCESS);
        } catch (ConstraintViolationException cve) {
            response.setType(ResponseDto.ERROR);
            response.setMessage("Shop should have valid values...");
            response.setData(Helper.covertCVException(cve));
        }
        return response;
    }

    @GET
    @Path("/accept-terms-and-conditions")
    public ResponseDto findById(@QueryParam("atc") Boolean atc) {
        ResponseDto response = ResponseDto.builder().build();
        if (checkAuthentication()) {
            response.setType(ResponseDto.ERROR);
            response.setMessage("You are not authorized to use this end point.");
            return response;
        }
        if (atc) {
            setUpService.markSummaryCompleted();
            response.setType(ResponseDto.SUCCESS);
            response.setData(setUpService.getCurrentState());
        } else {
            response.setType(ResponseDto.ERROR);
            response.setMessage("You must accept the terms and conditions...");
        }
        return response;
    }

    private boolean checkAuthentication() {
        return !setUpService.getCurrentState().isAuthenticated();
    }
}