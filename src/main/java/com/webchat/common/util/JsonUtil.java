package com.webchat.common.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;

@Slf4j
public class JsonUtil {

    private static ObjectMapper om = new ObjectMapper();

    private static JsonFactory factory = new JsonFactory();

    static {
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    }

    /**
     * @param jsonStr
     * @param clazz
     * @param <T>
     *
     * @return
     */
    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        T value = null;
        try {
            value = om.readValue(jsonStr, clazz);
        } catch (Exception e) {
            log.error("fromJson error,jsonStr:" + jsonStr + ",class:" + clazz.getName() + ",exception:"
                    + e.getMessage());
            return null;
        }
        return value;
    }

    /**
     * @param jsonStr
     * @param valueTypeRef
     * @param <T>
     *
     * @return
     */
    public static <T> T fromJson(String jsonStr, TypeReference<?> valueTypeRef) {
        T value = null;
        try {
            value = (T) om.readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            log.error("fromJson error,jsonStr:" + jsonStr + ",class:" + valueTypeRef.getType() + ",exception:"
                    + e.getMessage());
            throw new RuntimeException(e);
        }
        return value;
    }

    /**
     * @param obj
     * @param prettyPrinter
     *
     * @return
     */
    public static String toJson(Object obj, boolean prettyPrinter) {
        if (obj == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        try {
            JsonGenerator jg = factory.createGenerator(sw);
            if (prettyPrinter) {
                jg.useDefaultPrettyPrinter();
            }
            om.writeValue(jg, obj);
        } catch (IOException e) {
            log.error("toJson error,jsonObj:" + obj.getClass().getName() + ",prettyPrinter:" + prettyPrinter
                    + ",exception:" + e.getMessage());
            throw new RuntimeException(e);
        }
        return sw.toString();
    }

    /**
     * @param object
     *
     * @return
     */
    public static String toJsonString(Object object) {
        try {
            String valueAsString = om.writeValueAsString(object);
            return valueAsString;
        } catch (Exception e) {
            log.error("toJson error,jsonObj:" + object.getClass().getName() + ",exception:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * @param object
     *
     * @return
     */
    public static String toJsonStringWithDefaultPrettyPrinter(Object object) {
        try {
            String valueAsString = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            return valueAsString;
        } catch (Exception e) {
            log.error("toJson error,jsonObj:" + object.getClass().getName() + ",exception:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
