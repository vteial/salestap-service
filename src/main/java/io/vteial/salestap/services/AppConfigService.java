package io.vteial.salestap.services;

import java.util.List;
import java.util.Map;

import io.vteial.salestap.models.AppConfig;

public interface AppConfigService {

    List<AppConfig> listAll();

    String getValue(String key);

    void setValue(String key, String val);

}
