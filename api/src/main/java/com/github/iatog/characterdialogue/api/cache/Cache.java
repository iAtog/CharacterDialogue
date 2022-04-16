package com.github.iatog.characterdialogue.api.cache;

public interface Cache<K, V> {

    V get(K key);

    void set(K key, V value);

    boolean contains(K key);

    V remove(K key);

}
