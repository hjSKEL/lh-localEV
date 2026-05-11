package kr.co.kevit.localcsms.common.util.json;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * JSON Utility
 *
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a> 
 * @since 2018. 5. 14.
 */
public class JsonUtil {
    //
    private JsonUtil() {
        // Nothing to do.
    }

    public static String toJson(Object object) {
        //
        return gson().toJson(object);
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        //
        return gson().fromJson(jsonString, clazz);
    }
    
    public static <T> T fromJson(String jsonString, Class<T> clazz, Gson gson) {
        if (gson != null) {
            return gson.fromJson(jsonString, clazz);
        } else {
            return gson().fromJson(jsonString, clazz);
        }
    }

    public static <T> T fromJson(String jsonString, Type type) {
        //
        return gson().fromJson(jsonString, type);
    }

    public static <T> List<T> fromJsonArray(String jsonString, Class<T[]> clazz) {
        //
        Gson gson = gson();

        // fromJson으로 리스트를 변환할 경우 요소 타입이 LinkedTreeMap으로 바뀌는 문제 때문에
        // 배열로 변환 후 다시 리스트로 변환
        T[] array = gson.fromJson(jsonString, clazz);
        return new ArrayList<T>(Arrays.asList(array));
    }

    public static <T> List<T> fromJsonArray(Reader reader, Class<T[]> clazz) {
        //
        Gson gson = gson();

        // fromJson으로 리스트를 변환할 경우 요소 타입이 LinkedTreeMap으로 바뀌는 문제 때문에
        // 배열로 변환 후 다시 리스트로 변환
        T[] array = gson.fromJson(reader, clazz);
        return new ArrayList<T>(Arrays.asList(array));
    }

    public static <K,V> Map<K,V> fromJsonMap(String json, Class<K> k, Class<V> v) {
        //
        Type type = new TypeToken<Map<K, V>>(){}.getType();
        return JsonUtil.fromJson(json, type);
    }

    public static boolean equalsTo(Object obj1, Object obj2) {
        //
        return gson().toJson(obj1).equals(gson().toJson(obj2));
    }

    public static String toPrettyJson(Object obj) {
        //
        return new GsonBuilder().setPrettyPrinting().create().toJson(obj);
    }

    // private methods

    private static JsonSerializer<Date> getJsonDateSerializer() {
        //
        return new JsonSerializer<Date>() {
            //
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc,
                    JsonSerializationContext context) {
                //
                return new JsonPrimitive(src.getTime());
            }
        };
    }

    private static JsonDeserializer<Date> getJsonDateDeserializer() {
        //
        return new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT,
                    JsonDeserializationContext context)
                            throws JsonParseException {
                //
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        };
    }

    private static JsonSerializer<java.sql.Date> getJsonSqlDateSerializer() {
        //
        return new JsonSerializer<java.sql.Date>() {
            //
            @Override
            public JsonElement serialize(java.sql.Date src, Type typeOfSrc,
                    JsonSerializationContext context) {
                //
                return new JsonPrimitive(src.getTime());
            }
        };
    }

    private static JsonDeserializer<java.sql.Date> getJsonSqlDateDeserializer() {
        //
        return new JsonDeserializer<java.sql.Date>() {
            @Override
            public java.sql.Date deserialize(JsonElement json, Type typeOfT,
                    JsonDeserializationContext context)
                            throws JsonParseException {
                //
                return new java.sql.Date(json.getAsJsonPrimitive().getAsLong());
            }
        };
    }

    public static GsonBuilder defaultBuilder() {
        return new GsonBuilder()
            .registerTypeAdapter(Date.class, getJsonDateSerializer())
            .registerTypeAdapter(Date.class, getJsonDateDeserializer())
            .registerTypeAdapter(java.sql.Date.class, getJsonSqlDateSerializer())
            .registerTypeAdapter(java.sql.Date.class, getJsonSqlDateDeserializer());
    }
    
    private static Gson gson() {
        //
        return new GsonBuilder()
            .registerTypeAdapter(Date.class, getJsonDateSerializer())
            .registerTypeAdapter(Date.class, getJsonDateDeserializer())
            .registerTypeAdapter(java.sql.Date.class, getJsonSqlDateSerializer())
            .registerTypeAdapter(java.sql.Date.class, getJsonSqlDateDeserializer())
            .create();
    }
    
}