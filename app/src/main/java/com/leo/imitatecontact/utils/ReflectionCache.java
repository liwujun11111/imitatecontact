package com.leo.imitatecontact.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

class ReflectionCache {

    private static ReflectionCache INSTANCE;

    private HashMap<String, Class> mClassMap;
    private HashMap<String, Method> mMethodMap;
    private HashMap<String, Field> mFieldMap;

    private ReflectionCache() {
        mClassMap = new HashMap<>();
        mMethodMap = new HashMap<>();
        mFieldMap = new HashMap<>();
    }

    static ReflectionCache getInstance() {
        if (INSTANCE == null) {
            synchronized (ReflectionCache.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ReflectionCache();
                }
            }
        }

        return INSTANCE;
    }


    Class<?> forName(String className) throws ClassNotFoundException {
        Class<?> objClass = mClassMap.get(className);
        if (objClass == null) {
            objClass = Class.forName(className);
            mClassMap.put(className, objClass);
        }
        return objClass;
    }


    Method getMethod(String className, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
        Class<?> classObj = forName(className);
        return getMethod(classObj, methodName, parameterTypes);
    }

    Method getMethod(Class<?> objClassObj, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        String methodKey = objClassObj.getName() + methodName;
        if (parameterTypes != null) {
            for (Class<?> c : parameterTypes) {
                methodKey += c.getName();
            }
        }
        Method method = mMethodMap.get(methodKey);
        if (method == null) {
            method = objClassObj.getMethod(methodName, parameterTypes);
            mMethodMap.put(methodKey, method);
        }
        return method;
    }

    Method getDeclaredMethod(Class<?> objClassObj, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        String methodKey = objClassObj.getName() + methodName;
        if (parameterTypes != null) {
            for (Class<?> c : parameterTypes) {
                methodKey += c.getName();
            }
        }
        Method method = mMethodMap.get(methodKey);
        if (method == null) {
            method = objClassObj.getDeclaredMethod(methodName, parameterTypes);
            mMethodMap.put(methodKey, method);
        }
        return method;
    }


    Field getField(String className, String fieldName) throws NoSuchFieldException, ClassNotFoundException {
        Class<?> classObj = forName(className);
        return getField(classObj, fieldName);
    }

    Field getField(Class<?> objClass, String fieldName) throws NoSuchFieldException {
        String fieldKey = objClass.getName() + fieldName;
        Field fieldObj = mFieldMap.get(fieldKey);
        if (fieldObj == null) {
            fieldObj = objClass.getField(fieldName);
            mFieldMap.put(fieldKey, fieldObj);
        }
        return fieldObj;
    }

    Field getDeclaredField(String className, String fieldName) throws NoSuchFieldException, ClassNotFoundException {
        Class<?> classObj = forName(className);
        return getDeclaredField(classObj, className);
    }

    Field getDeclaredField(Class<?> objClass, String fieldName) throws NoSuchFieldException {
        String fieldKey = objClass.getName() + fieldName;
        Field fieldObj = mFieldMap.get(fieldKey);
        if (fieldObj == null) {
            fieldObj = objClass.getDeclaredField(fieldName);
            mFieldMap.put(fieldKey, fieldObj);
        }
        return fieldObj;
    }


    void clearCache() {
        mClassMap.clear();
        mMethodMap.clear();
        mFieldMap.clear();
    }


}
