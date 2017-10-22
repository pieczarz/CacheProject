package com.cacheproject;

import java.util.stream.Collectors;

public class CacheManager implements Cache {

    private final int cacheSize = 3;
    static LinkedMap<String, CacheItem> cache = new LinkedMap<>();

    @Override
    public CacheItem cacheItem(Object item, String key) {

        CacheItem castItem = (CacheItem) item;

        if(cache.size() < cacheSize) {
            cache.put(key, castItem);
        } else {
            cache.remove(0);
            cache.put(key, castItem);
        }

        return castItem;
    }

    @Override
    public void invalidateCache() {
        cache.clear();
    }

    @Override
    public CacheView getView() {
        return CacheViewManager.getInstance();
    }


    protected static class CacheViewManager implements CacheView {

        private static CacheViewManager instance = null;
        protected CacheViewManager() {}

        public static CacheViewManager getInstance() {
            if(instance == null) {
                instance = new CacheViewManager();
            }
            return instance;
        }

        @Override
        public int size() {
            return cache.size();
        }

        @Override
        public CacheItem getItem(int index) {
            return cache.getValue(index);
        }

        @Override
        public CacheItem getItem(String key) {
            CacheItem item = cache.entrySet().stream()
                    .filter(map -> map.getKey().equals(key))
                    .map(map -> map.getValue()).findFirst().get();

            return item;
        }

        public void getAllItems() {
            String currentCache = cache.entrySet().stream()
                    .map(map -> map.getKey())
                    .collect(Collectors.joining(", "));

            System.out.println("Cache items: " +"\n" + currentCache);

        }
    }
}
