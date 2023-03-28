package com.acme.acmemall.utils.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/3/28 11:46
 */
public class J2EcacheUtil {
    protected static Logger LOGGER = Logger.getLogger(J2EcacheUtil.class);

    private static final String CACHE_CONFIG_PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "ehcache.xml";


    /**
     * 商城业务缓存
     */
    public static String SHOP_CACHE_NAME = "shopCache";
    public static CacheManager cacheManager = CacheManager.getInstance();

    public static Cache cache = cacheManager.getCache(SHOP_CACHE_NAME);

    private CacheManager manager;
    private static J2EcacheUtil ehCache;

    /**
     * 获得缓存配置管理
     *
     * @param path
     */
    private J2EcacheUtil(String path) {
        try {
            manager = CacheManager.create(path);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("获取配置文件错误：" + e.getMessage());
        }
    }

    public static J2EcacheUtil getInstance() {
        try {
            if (ehCache == null) {
                ehCache = new J2EcacheUtil(CACHE_CONFIG_PATH);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            LOGGER.error("初始化错误：" + e1.getMessage());
        }
        return ehCache;
    }

    /**
     * 获取Cache类
     *
     * @param cacheName
     * @return
     */
    public Cache getCache(String cacheName) {
        return manager.getCache(cacheName);
    }

    /**
     * 添加缓存数据
     *
     * @param cacheName
     * @param key
     * @param value
     */
    public void put(String cacheName, String key, Object value) {
        try {
            Cache cache = ehCache.getCache(cacheName);
            Element element = new Element(key, value);
            cache.put(element);
            cache.flush();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("添加缓存失败：" + e.getMessage());
        }
    }

    /**
     * 获取缓存数据
     *
     * @param cacheName
     * @param key
     * @return
     */
    public Object get(String cacheName, String key) {
        try {
            Cache cache = ehCache.getCache(cacheName);
            Element element = cache.get(key);
            return element == null ? null : element.getObjectValue();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("添加缓存失败：" + e.getMessage());
            return null;
        }
    }

    public void delete(String cacheName, String key) {
        try {
            Cache cache = ehCache.getCache(cacheName);
            cache.remove(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("删除缓存数据失败：" + ex.getMessage());
        }
    }
}
