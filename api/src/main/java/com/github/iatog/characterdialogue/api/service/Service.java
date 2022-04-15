package com.github.iatog.characterdialogue.api.service;

public interface Service {
    void start();

    default void stop() {}
}