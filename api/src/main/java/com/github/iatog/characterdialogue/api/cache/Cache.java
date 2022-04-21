package com.github.iatog.characterdialogue.api.cache;

import java.util.Map;
import java.util.function.BiConsumer;

public interface Cache<K, V> {

    V get(K key);

    void set(K key, V value);

    boolean contains(K key);

    V remove(K key);
    
    void forEach(BiConsumer<K, V> consumer);
    
    Map<K, V> map();

}
