package dev.eggl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AppConfig {


    @PostConstruct
    public void init() {
        System.out.println("Starting scheduled task");
    }
}
