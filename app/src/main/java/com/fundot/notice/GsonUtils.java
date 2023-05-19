package com.fundot.notice;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Map;

/**
 * 描述：Gson解析的工具类
 */
@Keep
public class GsonUtils {

    public static final Gson gson = new Gson();

    /**
     * 解析json数据
     *
     * @param json  json数据
     * @param clazz 映射的类
     * @param <T>   泛型
     * @return T类元素
     */
    public static <T> T json2Bean(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        }catch (Throwable e){
            return null;
        }
    }

    /**
     * 解析jsonArray，这里使用报错类型转换异常的话，使用下面的
     *
     * @param json json数据
     * @param <T>  泛型
     * @return ArrayList
     */
    public static <T> ArrayList<T> json2BeanArray(String json) {
        ArrayList<T> arrayList;
        arrayList = gson.fromJson(json, new TypeToken<ArrayList<T>>() {
        }.getType());
        return arrayList;
    }

    /**
     * 解析jsonArray，有人推荐这种方式更好
     *
     * @param json  json数据
     * @param clazz 映射的类
     * @param <T>   泛型
     * @return ArrayList
     */
    public static @NonNull <T> ArrayList<T> json2BeanArrayPlus(String json, Class<T> clazz) {
        ArrayList<T> list = new ArrayList<>();
        try {
            JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                list.add(gson.fromJson(element, clazz));
            }
            return list;
        } catch (Throwable e) {
            e.printStackTrace();
            return list;
        }
    }
    /**
     * 解析json数据
     *
     * @param json  json数据
     * @return T类元素
     */
    public static Map<String, Object> json2Map(String json) {
        try {
            return gson.fromJson(json, Map.class);
        }catch (Throwable e){
            return null;
        }
    }
    public static String jsonCreate(Object object){
        try {
            return gson.toJson(object);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
