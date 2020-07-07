/*
 * Copyright (C) 2016 Francisco José Montiel Navarro.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.base.basemodule.net.cookie;


import android.util.Log;

import com.base.basemodule.net.cookie.cache.CookieCache;
import com.base.basemodule.net.cookie.persistence.CookiePersistor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentCookieJar implements ClearableCookieJar {

    private CookieCache cache;
    private CookiePersistor persistor;

    public PersistentCookieJar(CookieCache cache, CookiePersistor persistor) {
        this.cache = cache;
        this.persistor = persistor;
        this.cache.addAll(persistor.loadAll());
    }

    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        //Log.e("lzscookie","saveFromResponse:"+cookies.size());
        cache.addAll(cookies);
        persistor.saveAll(cookies);
    }

    @Override
    synchronized public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> removedCookies = new ArrayList<>();
        List<Cookie> validCookies = new ArrayList<>();
        for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
            Cookie currentCookie = it.next();
            if (isCookieExpired(currentCookie)) {
                //Log.e("lzscookie", "currentCookie 已过期>" + currentCookie.toString());
                removedCookies.add(currentCookie);
                it.remove();
            } else if (currentCookie.matches(url)) {
                //Log.e("lzscookie", "currentCookie 未过期>" + currentCookie.toString());
                validCookies.add(currentCookie);
            }
        }
        persistor.removeAll(removedCookies);
        return validCookies;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        //Log.e("lzs","isCookieExpired: "+cookie.expiresAt()+"  " + System.currentTimeMillis());
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    @Override
    public void clearSession() {
        cache.clear();
        cache.addAll(persistor.loadAll());
    }

    synchronized public void clear() {
        cache.clear();
        persistor.clear();
    }
}
