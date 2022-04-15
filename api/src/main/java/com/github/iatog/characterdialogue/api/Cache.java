package com.github.iatog.characterdialogue.api;

public interface Cache<K, V> {

    V get(K key);

    void set(K key, V value);

    boolean contains(K key);

    V remove(K key);

}
