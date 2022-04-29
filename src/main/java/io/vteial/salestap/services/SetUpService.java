package io.vteial.salestap.services;

import io.vteial.salestap.dtos.SetUpDto;
import io.vteial.salestap.models.Shop;
import io.vteial.salestap.models.User;

import javax.validation.Valid;

public interface SetUpService {

    SetUpDto getCurrentState();

    boolean auth(String password);

    SetUpDto registerOwner(@Valid User item);

    SetUpDto createShop(@Valid Shop item);

    void markRegisterOwnerCompleted();

    void markCreateShopCompleted();

    void markSummaryCompleted();
}
