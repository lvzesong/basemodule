package com.base.basemodule.utils;

/**
 * Created by lzs on 2018/10/24 15:26
 * E-Mail Address：343067508@qq.com
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * SharedPreferences处理工具类
 *
 * @version v1.0
 * @date 2014-2-20
 */
public class SharedPreferencesUtil {
    private static SharedPreferences preferences = null;

    /**
     * 在调用SharedPreferencesUtil里的方法之前必须进行初始化
     * <p>
     * <p>
     * SharedPreferences文件名
     */
    public static SharedPreferencesUtil getInstance() {
        return SingletonHolder.instance;
    }

    public static void init(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        //Log.e("init","init:"+preferences);
    }

    private static class SingletonHolder {
        static final SharedPreferencesUtil instance = new SharedPreferencesUtil();
    }




    public void putBoolean(String key, boolean value) {
        if(preferences == null){
            return;
        }
        preferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 默认值false
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public void putInt(String key, int value) {
        if(preferences == null){
            return;
        }
        preferences.edit().putInt(key, value).commit();
    }

    /**
     * 默认值0
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        if(preferences == null){
            return 0;
        }
        return preferences.getInt(key, 0);
    }

    public void putFloat(String key, Float value) {
        Log.e("putFloat","preferences:"+preferences);
        if(preferences == null){
            return;
        }
        preferences.edit().putFloat(key, value).commit();
    }

    /**
     * 默认值0f
     *
     * @param key
     * @return
     */
    public Float getFloat(String key) {
        if(preferences == null){
            return 0f;
        }
        return preferences.getFloat(key, 0f);
    }

    public void putLong(String key, Long value) {
        if(preferences == null){
            return;
        }
        preferences.edit().putLong(key, value).commit();
    }

    /**
     * 默认值0L
     *
     * @param key
     * @return
     */
    public Long getLong(String key) {
        return preferences.getLong(key, 0L);
    }

    public void putString(String key, String value) {
        if(preferences == null){
            return;
        }
        preferences.edit().putString(key, value).commit();
    }

    /**
     * 默认值null
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        if(preferences == null){
            return null;
        }
        return preferences.getString(key, null);
    }

    /*清空*/
    public void clear() {
        preferences.edit().clear().commit();
    }

    /**
     * 保存一个对象
     *
     * @param object
     */
    public void putObject(Object object) {
        Class clazz = object.getClass();
        String className = clazz.getSimpleName();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String methodName = "get" + getFirstUpString(fieldName);
            try {
                Method method = clazz.getMethod(methodName);
                Object result = method.invoke(object);
                if (result instanceof Integer) {
                    putInt(getName(className, fieldName),
                            Integer.parseInt(result.toString()));
                } else if (result instanceof Long) {
                    putLong(
                            getName(className, fieldName),
                            Long.parseLong(result.toString()));
                } else if (result instanceof Float) {
                    putFloat(
                            getName(className, fieldName),
                            Float.parseFloat(result.toString()));
                } else if (result instanceof Boolean) {
                    putBoolean(
                            getName(className, fieldName),
                            Boolean.parseBoolean(result.toString()));
                } else if (result instanceof String) {
                    putString(
                            getName(className, fieldName), result.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取一个对象
     *
     * @param clazz 传入对象的class
     * @return 返回对象，如果没有，返回为null
     */
    public Object getObject(Class clazz) {
        Object object = null;
        try {
            object = clazz.getConstructor(new Class[0]).newInstance();
            String className = clazz.getSimpleName();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                String methodName = "set" + getFirstUpString(fieldName);

                Method method = null;
                // Object result = method.invoke(object, null);
                String result = field.getType().getSimpleName();
                Object args = null;
                if (result.equals("Integer") || result.equals("int")) {
                    method = clazz.getMethod(methodName, Integer.class);
                    args = getInt(getName(className,
                            fieldName));
                } else if (result.equals("Long") || result.equals("long")) {
                    method = clazz.getMethod(methodName, Long.class);
                    args = getFloat(getName(className,
                            fieldName));
                } else if (result.equals("Float") || result.equals("float")) {
                    method = clazz.getMethod(methodName, Float.class);
                    args = getBoolean(getName(className,
                            fieldName));
                } else if (result.equals("Boolean") || result.equals("boolean")) {
                    method = clazz.getMethod(methodName, Boolean.class);
                    args = getBoolean(getName(className,
                            fieldName));
                } else if (result.equals("String")) {
                    method = clazz.getMethod(methodName, String.class);
                    args = getString(getName(className,
                            fieldName));
                }
                method.invoke(object, args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 把第一个字符大写
     */
    private String getFirstUpString(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String getName(String className, String fieldName) {
        return className + "." + fieldName;
    }
}
