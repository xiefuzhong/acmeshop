package com.acme.acmemall.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/3/7 14:20
 */
public class GsonUtil {
    private GsonUtil() {}

    private static final Gson gson;

    static {
        gson =
                new GsonBuilder()
                        .disableHtmlEscaping()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .addSerializationExclusionStrategy(
                                new ExclusionStrategy() {
                                    @Override
                                    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                                        final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                                        return expose != null && !expose.serialize();
                                    }

                                    @Override
                                    public boolean shouldSkipClass(Class<?> aClass) {
                                        return false;
                                    }
                                })
                        .addDeserializationExclusionStrategy(
                                new ExclusionStrategy() {
                                    @Override
                                    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                                        final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                                        return expose != null && !expose.deserialize();
                                    }

                                    @Override
                                    public boolean shouldSkipClass(Class<?> aClass) {
                                        return false;
                                    }
                                })
                        .create();
    }


    public static Gson getGson() {
        return gson;
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}
