package com.github.iatog.characterdialogue.module;

import com.github.iatog.characterdialogue.api.service.Service;
import com.github.iatog.characterdialogue.service.DialogueService;
import com.github.iatog.characterdialogue.service.ListenerService;
import com.github.iatog.characterdialogue.service.MainService;

import team.unnamed.inject.Binder;
import team.unnamed.inject.Module;
import team.unnamed.inject.scope.Scopes;

public class ServiceModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(Service.class)
                .to(MainService.class)
                .in(Scopes.SINGLETON);
        
        binder.bind(Service.class)
                .named("listener")
                .to(ListenerService.class)
                .in(Scopes.SINGLETON);

        binder.bind(Service.class)
                .named("dialogue")
                .to(DialogueService.class)
                .in(Scopes.SINGLETON);
    }

}
