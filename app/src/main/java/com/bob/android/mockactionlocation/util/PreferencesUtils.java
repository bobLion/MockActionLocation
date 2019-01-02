package com.bob.android.mockactionlocation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @package com.bob.android.mockactionlocation.util
 * @fileName PreferencesUtils
 * @Author Bob on 2018/12/21 18:04.
 * @Describe TODO
 */
public class PreferencesUtils {

    public static String PREFERENCE_NAME = "settings";

    /**
     *  保存String值到配置文件
     *
     * @param context 应用Context
     * @param key     键名称
     * @param value   键值
     * @return 保存成功返回true，否则返回false
     */
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 获取保存的String
     *
     * @param context 应用Context
     * @param key     键名称
     * @return 返回保存的String值, 如果找不到默认返回null
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * 获取保存的String
     *
     * @param context      应用Context
     * @param key          键名称
     * @param defaultValue 如果找不到，返回此默认值
     * @return 返回保存的String值，如果找不到，返回此默认值
     */
    public static String getString(Context context, String key,
                                   String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    /**
     * 保存int值
     *
     * @param context 应用Context
     * @param key     键名称
     * @param value   待保存int值
     * @return 保存成功返回true，否则返回false
     */
    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * 获取保存的int值
     *
     * @param context 应用context
     * @param key     键名称
     * @return 返回保存的int值，如果找不到返回-1
     * @see #getInt(Context, String, int)
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * 获取int值
     *
     * @param context      应用context
     * @param key          键名称
     * @param defaultValue 如果找不到，则使用此默认值
     * @return 返回保存的
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    /**
     * 保存long类型数据
     *
     * @param context 应用context
     * @param key     键名称
     * @param value   待保存的值
     * @return 保存成功返回true，保存失败返回false
     */
    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * 获取保存的long类型值
     *
     * @param context 应用context
     * @param key     键名称
     * @return 返回保存的long类型数据，如果找不到返回-1
     * @see #getLong(Context, String, long)
     */
    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    /**
     * 获取保存的long类型值
     *
     * @param context      应用context
     * @param key          键名称
     * @param  如果找不到则返回此默认值
     * @return 返回保存的long类型数据，如果找不到则返回默认值
     */
    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    /**
     * 保存float值
     *
     * @param context 应用context
     * @param key     键名称
     * @param value   待保存的float值
     * @return 保存成功返回true，保存失败返回false
     */
    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * 获取保存的float值
     *
     * @param context 应用context
     * @param key     键名称
     * @return 返回保存的float值，没有找到，则返回-1
     * @see #getFloat(Context, String, float)
     */
    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    /**
     * 获取保存的float值
     *
     * @param context      应用context
     * @param key          键名称
     * @param defaultValue 如果没有找到，返回此默认值
     * @return 返回保存的float值，没有找到，则返回默认值
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * 保存boolean类型值
     *
     * @param context 应用context
     * @param key     键名称
     * @param value   待保存的boolean类型值
     * @return 保存成功返回true，保存失败返回false
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 获取保存的boolean类型值
     *
     * @param context 应用context
     * @param key     键名称
     * @return 返回保存的boolean值，如果找不到，返回默认值false
     * @see #getBoolean(Context, String, boolean)
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * 获取保存的boolean类型值
     *
     * @param context      应用context
     * @param key          键名称
     * @param defaultValue 如果找不到，返回此默认值
     * @return 返回保存的boolean值，如果找不到，返回默认值
     */
    public static boolean getBoolean(Context context, String key,
                                     boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * @Description ：保存String集合
     * @Method @param context 应用context
     * @Method @param key 键名称
     * @Method @param value String集合
     * @Method @return ：保存成功返回true，保存失败返回false
     */
    public static boolean putStringSet(Context context, String key,
                                       Set<String> value) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    /**
     * @Description ：获取保存的StringSet值
     * @Method @param context 应用context
     * @Method @param key 键名称
     * @Method @param defaultValue 如果找不到返回此默认值
     * @Method @return ：返回保存的StringSet值，如果找不到返回默认值
     */
    public static Set<String> getStringSet(Context context, String key,
                                           Set<String> defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getStringSet(key, defaultValue);
    }

    /**
     * @Description ：获取保存的StringSet值
     * @Method @param context 应用context
     * @Method @param key 键名称
     * @Method @return ：返回保存的StringSet值，如果找不到返回null
     */
    public static Set<String> getStringSet(Context context, String key) {
        return getStringSet(context, key, null);
    }

    /**
     * @Description ：保存String数组
     * @Method @param context 应用context
     * @Method @param key 键名称
     * @Method @param value 待保存string数组
     * @Method @return ：保存成功返回true，保存失败返回false
     */
    public static boolean putStringArray(Context context, String key,
                                         String[] value, String separator) {
        if (value == null || value.length == 0) {
            return false;
        }
        if (TextUtils.isEmpty(separator)) {
            separator = "#";
        }
        StringBuilder builder = new StringBuilder();
        for (String str : value) {
            builder.append(str);
            builder.append(separator);
        }
        return putString(context, key, builder.toString());
    }

    /**
     * @Description ：保存String数组
     * @Method @param context 应用context
     * @Method @param key 键名称
     * @Method @param value 待保存string数组
     * @Method @return ：保存成功返回true，保存失败返回false
     */
    public static boolean putStringArray(Context context, String key,
                                         String[] value) {
        return putStringArray(context, key, value, "#");
    }

    /**
     * @Description ：获取保存的String数组，将数组元素拼成字符串保存到preference文件中，需要保证分隔符与数组元素值不能有重复值
     * @Method @param context 应用context
     * @Method @param key 键名称
     * @Method @param separator 数组元素拼成string字符串的分隔符，默认分隔符为#
     * @Method @return ：返回保存的string数组
     */
    public static String[] getStringArray(Context context, String key,
                                          String separator) {
        if (TextUtils.isEmpty(separator)) {
            separator = "#";
        }
        String str = getString(context, key);
        String[] arrays = null;
        if (!TextUtils.isEmpty(str)) {
            arrays = str.split(separator);
        }
        return arrays;
    }

    /**
     * @Description ：获取保存的String数组
     * @Method @param context 应用context
     * @Method @param key 键名称
     * @Method @return ：返回保存的string数组
     */
    public static String[] getStringArray(Context context, String key) {
        return getStringArray(context, key, "#");
    }

    /**
     * @Description ：保存String对象集合List类型
     * @Method @param context 应用context
     * @Method @param key 键名称
     * @Method @param value 带保存的list
     * @Method @return ：保存成功返回true，保存失败返回false
     */
    public static boolean putStringList(Context context, String key,
                                        List<String> value) {
        return putStringList(context, key, value, "#");
    }

    /**
     * @Description ：保存String对象集合List类型
     * @Method @param context 应用context
     * @Method @param key 键名称
     * @Method @param value 键值
     * @Method @param separator 列表元素拼成string字符串保存，separator为字符分隔符
     * @Method @return ：保存成功返回true，否则返回false
     */
    public static boolean putStringList(Context context, String key,
                                        List<String> value, String separator) {
        if (value == null || value.size() == 0) {
            return false;
        }
        if (TextUtils.isEmpty(separator)) {
            separator = "#";
        }
        StringBuilder builder = new StringBuilder();
        for (String str : value) {
            builder.append(str);
            builder.append(separator);
        }
        return putString(context, key, builder.toString());
    }

    /**
     * @Description ：获取保存的List对象
     * @Method @param context 应用context
     * @Method @param key 键名称
     * @Method @return ：键值保存的List对象
     */
    public static List<String> getStringList(Context context, String key) {
        return getStringList(context, key, "#");
    }

    /**
     * @Description ：获取保存的List对象
     * @Method @param context 应用context
     * @Method @param key 键名称
     * @Method @param separator 列表元素拼成string字符串的分隔符，默认分隔符为#
     * @Method @return ：键值保存的List对象
     */
    public static List<String> getStringList(Context context, String key,
                                             String separator) {
        String str = getString(context, key);
        List<String> list = null;
        if (TextUtils.isEmpty(separator)) {
            separator = "#";
        }
        if (!TextUtils.isEmpty(str)) {
            String[] arrays = str.split(separator);
            if (arrays != null && arrays.length > 0) {
                list = new ArrayList<String>(arrays.length);
                for (int i = 0; i < arrays.length; i++) {
                    list.add(arrays[i]);
                }
            }
        }
        return list;
    }
}
