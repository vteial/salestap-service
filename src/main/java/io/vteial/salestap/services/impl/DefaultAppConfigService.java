package io.vteial.salestap.services.impl;

import io.quarkus.panache.common.Sort;
import io.vteial.salestap.dtos.SetUpDto;
import io.vteial.salestap.models.AppConfig;
import io.vteial.salestap.repos.AppConfigRepository;
import io.vteial.salestap.services.AppConfigService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Slf4j
@ApplicationScoped
public class DefaultAppConfigService implements AppConfigService {

    @Inject
    AppConfigRepository appConfigRepository;

    @PostConstruct
    public void init() {
        AppConfig appConfig = appConfigRepository.find("key", "setUpState").firstResult();
        if (appConfig == null) {
            appConfig = AppConfig.builder().key("setUpState").value(SetUpDto.STATE_NEW).build();
            appConfig.prePersist(null, new Date());
            appConfigRepository.persist(appConfig);
        }
    }

    @Override
    public List<AppConfig> listAll() {
        return appConfigRepository.listAll(Sort.by("key"));
    }

    @Override
    public String getValue(String key) {
        AppConfig appConfig = appConfigRepository.find("key", key).firstResult();
        return appConfig == null ? "" : appConfig.getValue();
    }

    @Transactional
    @Override
    public void setValue(String key, String val) {
        AppConfig appConfig = appConfigRepository.find("key", key).firstResult();
        Date now = new Date();
        if (appConfig == null) {
            appConfig = AppConfig.builder().key(key).value(val).build();
            appConfig.prePersist(null, now);
            appConfigRepository.persist(appConfig);
        } else {
            appConfig.setValue(val);
            appConfig.preUpdate(null, now);
        }
    }


}
