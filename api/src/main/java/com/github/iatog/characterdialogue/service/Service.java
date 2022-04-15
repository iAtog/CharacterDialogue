package com.github.iatog.characterdialogue.service;

public interface Service {
    void start();

    default void stop() {}
}