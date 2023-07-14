package com.fundot.openinfo.utils

import android.text.TextUtils
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.StringReader
import java.lang.reflect.Type
import java.util.*

/**
 * GSON工具类，用以解析json字符串等
 */
object GsonUtil {
    @JvmField
    val gson: Gson = GsonBuilder().registerTypeAdapter(Int::class.java, IntegerDefault0Adapter())
            .registerTypeAdapter(Int::class.javaPrimitiveType, IntegerDefault0Adapter())
            .registerTypeAdapter(Double::class.java, DoubleDefault0Adapter())
            .registerTypeAdapter(Double::class.javaPrimitiveType, DoubleDefault0Adapter())
            .registerTypeAdapter(Long::class.java, LongDefault0Adapter())
            .registerTypeAdapter(Long::class.javaPrimitiveType, LongDefault0Adapter())
            .registerTypeAdapter(Boolean::class.javaPrimitiveType, BooleanDefault0Adapter())
            .registerTypeAdapter(Boolean::class.java, BooleanDefault0Adapter())
            .registerTypeAdapter(JsonObject::class.java, JsonObjectDefault0Adapter())
            .create()

    private class IntegerDefault0Adapter : JsonSerializer<Int?>, JsonDeserializer<Int> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Int {
            try {
                if (json.asString == "" || json.asString == "null") { //定义为int类型,如果后台返回""或者null,则返回0
                    return 0
                }
            } catch (ignore: Exception) {
            }
            return try {
                json.asInt
            } catch (e: NumberFormatException) {
                throw JsonSyntaxException(e)
            }
        }

        override fun serialize(src: Int?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(src)
        }
    }

    private class DoubleDefault0Adapter : JsonSerializer<Double?>, JsonDeserializer<Double> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Double {
            try {
                if (json.asString == "" || json.asString == "null") { //定义为double类型,如果后台返回""或者null,则返回0.00
                    return 0.00
                }
            } catch (ignore: Exception) {
            }
            return try {
                json.asDouble
            } catch (e: NumberFormatException) {
                throw JsonSyntaxException(e)
            }
        }

        override fun serialize(src: Double?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(src)
        }
    }

    private class LongDefault0Adapter : JsonSerializer<Long?>, JsonDeserializer<Long> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Long {
            try {
                if (json.asString == "" || json.asString == "null") { //定义为long类型,如果后台返回""或者null,则返回0
                    return 0L
                }
            } catch (ignore: Exception) {
            }
            return try {
                json.asLong
            } catch (e: NumberFormatException) {
                throw JsonSyntaxException(e)
            }
        }

        override fun serialize(src: Long?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(src)
        }
    }

    private class BooleanDefault0Adapter : JsonSerializer<Boolean?>, JsonDeserializer<Boolean> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Boolean {
            try {
                if (json.asInt == 1) {
                    return true
                } else if (json.asInt == 0) {
                    return false
                }
            } catch (ignore: Exception) {
            }
            return try {
                json.asBoolean
            } catch (e: NumberFormatException) {
                throw JsonSyntaxException(e)
            }
        }

        override fun serialize(src: Boolean?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(src)
        }
    }

    private class JsonObjectDefault0Adapter : JsonDeserializer<JsonObject?> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): JsonObject? {
            try {
                if (TextUtils.isEmpty(json.asString)) {
                    return null
                }
            } catch (ignore: Exception) {
            }
            return try {
                json.asJsonObject
            } catch (e: NumberFormatException) {
                throw JsonSyntaxException(e)
            }
        }
    }

    /*--------------------------------------*/
    /**
     * 将Json字符串转化为List<T>对象
     *
     * @param jsonString Json字符串
     * @param cls        类型 即实体类
     * @return List<T>对象
    </T></T> */
    private fun <T> _json2List(jsonString: String?, cls: Class<T>, mGson: Gson = gson): MutableList<T> {
        if (TextUtils.isEmpty(jsonString)) {
            return arrayListOf()
        }
        val list: MutableList<T> = ArrayList()
        val array = JsonParser.parseString(jsonString).asJsonArray
        for (elem in array) {
            list.add(mGson.fromJson(elem, cls))
        }
        return list
    }

    /**
     * 解析json转为bean
     *
     * @param jsonString json数据
     * @return bean
     */
    private fun <T> _json2Bean(jsonString: String?, cls: Class<T>, mGson: Gson = gson): T? {
        val reader = JsonReader(StringReader(jsonString))
        reader.isLenient = true
        return mGson.fromJson(reader, cls)
    }

    /**
     * 将Json字符串转化为Map<String></String>, Object>对象
     *
     * @param jsonString Json字符串
     * @return Map对象
     */
    private fun _json2Map(jsonString: String?, mGson: Gson = gson): Map<String, Any>? {
        return mGson.fromJson(jsonString,
                object : TypeToken<Map<String, Any>>() {}.type)
    }

    /**
     * 转成json
     *
     * @param object 对象
     * @return json
     */
    private fun _jsonCreate(`object`: Any?, mGson: Gson = gson): String? {
        return mGson.toJson(`object`)
    }


    @JvmStatic
    @JvmOverloads
    fun jsonCreate(`object`: Any?, mGson: Gson = gson): String {
        return try {
            _jsonCreate(`object`, mGson) ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    @JvmStatic
    @JvmOverloads
    fun json2Map(jsonString: String?, mGson: Gson = gson): Map<String, Any> {
        return try {
            _json2Map(jsonString, mGson) ?: HashMap()
        } catch (e: Exception) {
            e.printStackTrace()
            HashMap()
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <T> json2List(jsonString: String?, cls: Class<T>, mGson: Gson = gson): MutableList<T> {
        return try {
            _json2List(jsonString, cls, mGson)
        } catch (e: Exception) {
            e.printStackTrace()
            ArrayList()
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <T> json2Bean(jsonString: String?, cls: Class<T>, mGson: Gson = gson): T? {
        return try {
            _json2Bean(jsonString, cls, mGson)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @JvmStatic
    fun <T> jsonRecreateList(list: List<*>, cls: Class<T>): List<T> {
        return json2List(jsonString = jsonCreate(`object` = list), cls = cls)
    }
}


