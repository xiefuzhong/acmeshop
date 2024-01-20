package com.acme.acmemall.utils.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * @description:缓存工具类
 * @author: ihpangzi
 * @time: 2024/1/20 14:51
 */
public class CacheUtil {
    public void setCacheList(String key, Object value) {
        InputStream inputStream = new BufferedInputStream(this.getClass().getResourceAsStream("ehcache.xml"));
        CacheManager create = CacheManager.create(inputStream);
        Cache cache = create.getCache("shopCache");
        cache.put(new Element(key, value));
    }

    public Object getCacheList(String key) {
        InputStream inputStream = new BufferedInputStream(this.getClass().getResourceAsStream("ehcache.xml"));
        CacheManager create = CacheManager.create(inputStream);
        Cache cache = create.getCache("shopCache");
        Element element = cache.get(key);
        if (element == null) {
            return null;
        }
        return element.getObjectValue();
    }
}
