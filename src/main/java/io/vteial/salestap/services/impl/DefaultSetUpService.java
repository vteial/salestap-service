package io.vteial.salestap.services.impl;

import io.vteial.salestap.dtos.SetUpDto;
import io.vteial.salestap.models.AppConfig;
import io.vteial.salestap.models.User;
import io.vteial.salestap.models.constants.UserStatus;
import io.vteial.salestap.models.constants.UserType;
import io.vteial.salestap.repos.AppConfigRepository;
import io.vteial.salestap.services.AppConfigService;
import io.vteial.salestap.services.SetUpService;
import io.vteial.salestap.services.UserService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Slf4j
@ApplicationScoped
public class DefaultSetUpService implements SetUpService {

    @Inject
    AppConfigRepository appConfigRepository;

    @Inject
    UserService userService;

    @PostConstruct
    public void init() {
    }

    @Override
    public SetUpDto getCurrentState() {
        SetUpDto setUpDto = SetUpDto.builder().build();
        AppConfig appConfig = appConfigRepository.find("key", "setUpState").firstResult();
        setUpDto.setState(appConfig == null ? SetUpDto.STATE_NEW : appConfig.getValue());
        return setUpDto;
    }

    @Transactional
    @Override
    public User registerOwner(User item) {
        item.setType(UserType.OWNER);
        item.setStatus(UserStatus.ACTIVE);
        return userService.create(item);
    }
}
