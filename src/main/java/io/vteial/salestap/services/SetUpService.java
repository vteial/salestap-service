package io.vteial.salestap.services;

import io.vteial.salestap.dtos.SetUpDto;
import io.vteial.salestap.models.User;

public interface SetUpService {

    SetUpDto getCurrentState();

    User registerOwner(User item);

}
