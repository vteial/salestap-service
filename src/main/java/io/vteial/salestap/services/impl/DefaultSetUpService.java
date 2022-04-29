package io.vteial.salestap.services.impl;

import io.vteial.salestap.dtos.SetUpDto;
import io.vteial.salestap.models.AppConfig;
import io.vteial.salestap.models.Shop;
import io.vteial.salestap.models.User;
import io.vteial.salestap.models.constants.EntityStatus;
import io.vteial.salestap.models.constants.UserStatus;
import io.vteial.salestap.models.constants.UserType;
import io.vteial.salestap.repos.AppConfigRepository;
import io.vteial.salestap.services.SetUpService;
import io.vteial.salestap.services.ShopService;
import io.vteial.salestap.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;

@Slf4j
@ApplicationScoped
public class DefaultSetUpService implements SetUpService {

    @ConfigProperty(name = "app.sadmin-password")
    String sadminPassword = "-";

    @Inject
    AppConfigRepository appConfigRepository;

    @Inject
    UserService userService;

    @Inject
    ShopService shopService;

    private SetUpDto setUpDto;

    @PostConstruct
    public void init() {
        setUpDto = SetUpDto.builder().build();
        setUpDto.initSteps();
        AppConfig appConfig = appConfigRepository.find("key", "setUpState").firstResult();
        setUpDto.setState(appConfig == null ? SetUpDto.STATE_NEW : appConfig.getValue());
        log.info("{}", setUpDto);
    }

    @Override
    public SetUpDto getCurrentState() {
        return setUpDto;
    }

    public boolean auth(String password) {
        if(sadminPassword.equals(password)) {
            this.setUpDto.setAuthenticated(true);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public SetUpDto registerOwner(@Valid User item) {
        item.setType(UserType.OWNER);
        item.setRoleId("owner");
        item.setStatus(UserStatus.ACTIVE);
        setUpDto.setOwner(userService.create(item));
        this.markRegisterOwnerCompleted();
        return setUpDto;
    }

    @Transactional
    @Override
    public SetUpDto createShop(@Valid Shop item) {
        if(item.getId() == null) item.setParentId(0L);
        item.setUserId(setUpDto.getOwner().getId());
        item.setStatus(EntityStatus.ACTIVE);
        setUpDto.setShop(shopService.create(item));
        return setUpDto;
    }

    @Override
    public void markRegisterOwnerCompleted() {
        setUpDto.setState(SetUpDto.STATE_IN_PROGRESS);
        setUpDto.getSteps().put(SetUpDto.STEP_REGISTER_OWNER, true);
    }

    @Override
    public void markCreateShopCompleted() {
        setUpDto.setState(SetUpDto.STATE_IN_PROGRESS);
        setUpDto.getSteps().put(SetUpDto.STEP_CREATE_SHOP, true);
    }

    @Override
    public void markSummaryCompleted() {
        setUpDto.setState(SetUpDto.STATE_COMPLETED);
        setUpDto.getSteps().put(SetUpDto.STEP_SUMMARY, true);
        setUpDto.setTermsAndConditions(true);
    }
}
