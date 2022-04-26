package io.vteial.salestap.services;

import io.vteial.salestap.models.AppConfig;

import java.util.List;

public interface AppConfigService {

    List<AppConfig> listAll();

    String getValue(String key);

    void setValue(String key, String val);

    boolean remove(String key);

    boolean remove(Long id);

}
