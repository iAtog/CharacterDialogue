package com.github.iatog.characterdialogue.api.hook;

public abstract class HookConfig implements Hook {
    
    private Class<? extends Hook> hookType;
    private Hook instance;
    
    protected HookConfig(Class<? extends Hook> hook) {
        this.hookType = hook;
        this.instance = this;
    }
    
    public Hook getHook() {
        return instance;
    }
    
    public Class<? extends Hook> hookType() {
        return hookType;
    }

}
