package io.vteial.salestap.repos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.vteial.salestap.models.AppConfig;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppConfigRepository implements PanacheRepository<AppConfig> {
}
