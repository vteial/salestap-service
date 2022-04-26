package io.vteial.salestap.services;

import io.vteial.salestap.dtos.SetUpDto;
import io.vteial.salestap.models.Shop;
import io.vteial.salestap.models.User;

public interface SetUpService {

    SetUpDto getCurrentState();

    boolean auth(String password);

    SetUpDto registerOwner(User item);

    SetUpDto createShop(Shop item);

    void markRegisterOwnerCompleted();

    void markCreateShopCompleted();

    void markSummaryCompleted();
}
