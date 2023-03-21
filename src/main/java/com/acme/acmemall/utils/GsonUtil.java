package com.acme.acmemall.utils;

import com.google.gson.*;
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

    public static void main(String[] args) {
//        System.out.println(DigestUtils.sha256Hex("test"));
    }
}
