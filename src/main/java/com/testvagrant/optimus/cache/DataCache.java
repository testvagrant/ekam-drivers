package com.testvagrant.optimus.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public abstract class DataCache<Key, Value> {

  protected LoadingCache<Key, Value> build(CacheLoader<Key, Value> cacheLoader) {
    return CacheBuilder.newBuilder()
        .maximumSize(100)
        .expireAfterWrite(4, TimeUnit.HOURS)
        .build(cacheLoader);
  }

  public abstract void put(Key key, Value value);

  public abstract boolean isPresent(Predicate<Value> predicate);

  public abstract Value get(Predicate<Value> predicate) throws ExecutionException;

  protected abstract void lock(Key value);

  public abstract void release(Key value);

  public abstract long size();

  protected boolean anyMatch(LoadingCache<Key, Value> cache, Predicate<Value> predicate) {
    return cache.asMap().values().stream().anyMatch(predicate);
  }
}
