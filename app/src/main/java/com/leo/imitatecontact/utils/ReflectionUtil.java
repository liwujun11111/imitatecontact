package com.leo.imitatecontact.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    private static final String TAG = "ReflectionUtil";
    public static Object reflectMethod(String className, String methodName, Class[] paramTypes, Object[] params) {
        Object obj = null;
        try {

            Class cls = ReflectionCache.getInstance().forName(className);
            Method method = ReflectionCache.getInstance().getMethod(cls, methodName, paramTypes);
            Constructor ct = cls.getConstructor();
            obj = ct.newInstance();
            method.setAccessible(true);
            method.invoke(obj, params);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            if (null != e.getTargetException()) {
                e.getTargetException().printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return obj;
    }
    public static Object invoke(Class<?> objClass, Object obj, String methodName, Class<?>[] paramsTypes, Object[] params) throws Exception {
        if(params == null || params.length == 0){
            Method method = objClass.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(obj);
        }else{
            Method method = objClass.getDeclaredMethod(methodName, paramsTypes);
            method.setAccessible(true);
            return method.invoke(obj, params);
        }
    }

    public static Object getField(Object desObj, Class<?> desClass, String fieldName) throws NoSuchFieldException {
        if(desObj == null || desClass == null || fieldName == null){
            throw new IllegalArgumentException("parameter can not be null!");
        }
        try{
            Field field = desClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(desObj);
        }catch(Exception ignore){
            throw new NoSuchFieldException(fieldName);
        }
    }



}

