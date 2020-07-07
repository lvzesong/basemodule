package com.base.basemodule.utils;

import com.base.basemodule.BaseApplication;
//import com.base.basemodule.db.entity.Cache;
//import com.base.basemodule.db.entity.Cache_;
//
//import io.objectbox.Box;
//import io.objectbox.Property;

/**
 * @FileName: com.base.basemodule.utils.NetworkCache.java
 * @author: lzs
 * @date: 2019-03-15 11:53
 */
public class NetworkCache {
    private NetworkCache() {
    }

    //
    private static class SingletonHolder {
        private static final NetworkCache instance = new NetworkCache();
    }

    public static NetworkCache getInstance() {
        return SingletonHolder.instance;
    }

    private boolean hasCache() {
        return false;
    }

    //public String loadCache(String cacheKey) {
//        Box<Cache> cacheBox = BaseApplication.getApplication().getBoxStore().boxFor(Cache.class);
//        Cache cache = cacheBox.query().equal(Cache_.key, cacheKey).build().findFirst();
//        return cache.data;
//    }

//    public long saveCache(String cacheKey, String cache) {
//        Box<Cache> cacheBox = BaseApplication.getApplication().getBoxStore().boxFor(Cache.class);
//        Cache cache1 = new Cache();
//        cache1.data = cache;
//        cache1.key = cacheKey;
//        return cacheBox.put(cache1);
//    }


}
