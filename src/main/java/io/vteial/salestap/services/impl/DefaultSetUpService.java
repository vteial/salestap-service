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

@Slf4j
@ApplicationScoped
public class DefaultSetUpService implements SetUpService {

    @ConfigProperty(name = "app.sadmin-password")
    private String sadminPassword = "-";

    @Inject
    private AppConfigRepository appConfigRepository;

    @Inject
    private UserService userService;

    @Inject
    private ShopService shopService;

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
        return sadminPassword.equals(password);
    }

    @Transactional
    @Override
    public User registerOwner(User item) {
        item.setType(UserType.OWNER);
        item.setRoleId("owner");
        item.setStatus(UserStatus.ACTIVE);
        item = userService.create(item);
        setUpDto.setOwner(item);
        return item;
    }

    @Transactional
    @Override
    public Shop createShop(Shop item) {
        if(item.getId() == 0) item.setParentId(0L);
        item.setUserId(setUpDto.getOwner().getId());
        item.setStatus(EntityStatus.ACTIVE);
        item = shopService.create(item);
        setUpDto.setShop(item);
        return item;
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
