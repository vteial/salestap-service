package io.vteial.salestap;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
@Slf4j
public class App {

    void onStart(@Observes StartupEvent ev) {
        log.info("App starting with the profiles {}", ProfileManager.getActiveProfile());
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.info("Application is stopping...");
    }
}
