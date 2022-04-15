package com.github.iatog.characterdialogue.modules;

import com.github.iatog.characterdialogue.api.service.Service;
import com.github.iatog.characterdialogue.service.ListenerService;
import com.github.iatog.characterdialogue.service.MainService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import static com.google.inject.name.Names.named;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(Service.class)
                .to(MainService.class)
                .in(Scopes.SINGLETON);
        
        this.bind(Service.class)
            .annotatedWith(named("listener"))
            .to(ListenerService.class)
            .in(Scopes.SINGLETON);
    }

}
