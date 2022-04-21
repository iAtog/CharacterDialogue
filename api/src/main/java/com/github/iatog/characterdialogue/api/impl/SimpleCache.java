package com.github.iatog.characterdialogue.api.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import com.github.iatog.characterdialogue.api.cache.Cache;

public class SimpleCache<K, V> implements Cache<K, V> {

    private final Map<K, V> map;

    public SimpleCache() {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void set(K key, V value) {
        map.put(key, value);
    }

    @Override
    public boolean contains(K key) {
        return map.containsKey(key);
    }

    @Override
    public V remove(K key) {
        return map.remove(key);
    }

    @Override
    public void forEach(BiConsumer<K, V> consumer) {
        map.forEach(consumer);
    }

    @Override
    public Map<K, V> map() {
        return map;
    }

}
