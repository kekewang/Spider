package com.snm.domain;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;

public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 2249325746951841661L;
    private static Map<Class<?>, PropertyDescriptor[]> propMap = new HashMap(256);
    private transient ThreadLocal<BaseEntity> visitor = new ThreadLocal() {
        protected BaseEntity initialValue() {
            return null;
        }
    };
    private String simpleName = this.getClass().getSimpleName();

    public BaseEntity() {
    }

    private static int getTypeSortValue(Class<?> clazz) {
        byte result;
        if (!Map.class.isAssignableFrom(clazz) && !Collection.class.isAssignableFrom(clazz)) {
            if (String.class != clazz && !Number.class.isAssignableFrom(clazz) && !clazz.isPrimitive() && !clazz.isEnum()) {
                result = 1;
            } else {
                result = 0;
            }
        } else {
            result = 2;
        }

        return result;
    }

    private boolean isNullOrEmpty(Object result) {
        return result == null ? true : (result instanceof Map ? ((Map) result).isEmpty() : (!(result instanceof List) && !(result instanceof Collection) ? false : ((Collection) result).isEmpty()));
    }

    private String toArrayString(Class<?> componentType, Object result) {
        String str;
        if (componentType == Character.TYPE) {
            str = Arrays.toString((char[]) ((char[]) result));
        } else if (componentType == Boolean.TYPE) {
            str = Arrays.toString((boolean[]) ((boolean[]) result));
        } else if (componentType == Byte.TYPE) {
            str = Arrays.toString((byte[]) ((byte[]) result));
        } else if (componentType == Short.TYPE) {
            str = Arrays.toString((short[]) ((short[]) result));
        } else if (componentType == Integer.TYPE) {
            str = Arrays.toString((int[]) ((int[]) result));
        } else if (componentType == Long.TYPE) {
            str = Arrays.toString((long[]) ((long[]) result));
        } else if (componentType == Float.TYPE) {
            str = Arrays.toString((float[]) ((float[]) result));
        } else if (componentType == Double.TYPE) {
            str = Arrays.toString((double[]) ((double[]) result));
        } else {
            str = Arrays.toString((Object[]) ((Object[]) result));
        }

        return !"[]".equals(str) && !"null".equals(str) ? str : null;
    }

    private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
        is.defaultReadObject();
        this.visitor = new ThreadLocal() {
            protected BaseEntity initialValue() {
                return null;
            }
        };
    }
}