package br.com.frametcc.conn;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import br.com.frametcc.conn.api.HasServerId;
import br.com.frametcc.conn.api.JsonReceiveKey;
import br.com.frametcc.conn.api.JsonSendKey;

public class JsonParser {

    public static JSONObject parseToJson(Object obj) {
        return parseToJson(obj, null);
    }

    public static JSONObject parseToJson(Object obj, Integer api) {
        HashMap<Object, Object> map = new HashMap<>();
        try {
            Class<JsonSendKey> annotationType = JsonSendKey.class;
            Field[] fields = obj.getClass().getDeclaredFields();
            if(api != null) {
                fields = getUsedFields(fields, api);
            }
            for(Field f : fields) {
                if(f.isAnnotationPresent(annotationType)) {
                    JsonSendKey annotation = f.getAnnotation(annotationType);
                    String key = annotation.jsonKeyValue();
                    String value = getAsString(obj, f);
                    if(value != null) {
                        map.put(key, value.trim());
                    }
                }
            }
        } catch(IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
        return new JSONObject(map);
    }

    private static Field[] getUsedReceiveFields(Field[] fields, Integer api) {
        List<Field> fieldsUsed = new ArrayList<>();
        for(Field f : fields) {
            Class<JsonReceiveKey> annotationType = JsonReceiveKey.class;
            if(f.isAnnotationPresent(annotationType)) {
                JsonReceiveKey annotation = f.getAnnotation(annotationType);
                int[] usedInApi = annotation.usedInApi();
                for(int inApi : usedInApi) {
                    if(api == inApi) {
                        fieldsUsed.add(f);
                    }
                }
            }
        }

        Field[] gambi = new Field[fieldsUsed.size()];
        gambi = fieldsUsed.toArray(gambi);
        return gambi;
    }

    private static Field[] getUsedFields(Field[] fields, Integer api) {
        List<Field> fieldsUsed = new ArrayList<>();
        for(Field f : fields) {
            Class<JsonSendKey> annotationType = JsonSendKey.class;
            if(f.isAnnotationPresent(annotationType)) {
                JsonSendKey annotation = f.getAnnotation(annotationType);
                int[] usedInApi = annotation.usedInApi();
                for(int inApi : usedInApi) {
                    if(api == inApi) {
                        fieldsUsed.add(f);
                    }
                }
            }
        }

        Field[] gambi = new Field[fieldsUsed.size()];
        gambi = fieldsUsed.toArray(gambi);
        return gambi;
    }

    public static <E> List<E> parseListFromJson(Class<E> clazz, JSONArray array, Integer api, ObjectParsedListener<E> listener) {
        List<E> list = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            try {
                JSONObject jsonObject = array.getJSONObject(i);
                E object = parseFromJson(clazz, jsonObject, api);
                if(listener != null) {
                    listener.onObjectParsed(object, jsonObject, api);
                }
                list.add(object);
            } catch(JSONException e) {
                throw new RuntimeException("erro json");
            }
        }
        return list;
    }

    public static <E> E parseFromJson(Class<E> clazz, JSONObject json, Integer api) {
        E object = null;
        try {
            object = clazz.newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            if(api != null) {
                fields = getUsedReceiveFields(fields, api);
            }
            Class<JsonReceiveKey> annotationType = JsonReceiveKey.class;
            for(Field f : fields) {
                if(f.isAnnotationPresent(annotationType)) {
                    JsonReceiveKey annotation = f.getAnnotation(annotationType);
                    String key = annotation.jsonKeyValue();
                    String result = json.optString(key);
                    setFromRightType(result, object, f);
                }
            }
        } catch(InstantiationException | IllegalAccessException | IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    private static <E> void setFromRightType(String result, E obj, Field f) throws IllegalAccessException, IllegalArgumentException, IOException {
        if(result == null || result.isEmpty() || result.equals("null")) {
            f.set(obj, null);
        } else {
            Class<?> type = f.getType();
            if(type == byte[].class) {
                byte[] decoded = Base64.decode(result.getBytes(), Base64.DEFAULT);
                f.set(obj, decoded);
            } else if(type == Boolean.class) {
                Boolean bool = Boolean.parseBoolean(result);
                f.set(obj, bool);
            } else if(type.getSuperclass() == Number.class) {
                if(type == Double.class) {
                    f.set(obj, Double.parseDouble(result));
                } else if(type == Float.class) {
                    f.set(obj, Float.parseFloat(result));
                } else if(type == Long.class) {
                    f.set(obj, Long.parseLong(result));
                } else if(type == Integer.class) {
                    f.set(obj, Integer.parseInt(result));
                } else if(type == Short.class) {
                    f.set(obj, Short.parseShort(result));
                }
            } else if(type == String.class) {
                f.set(obj, result);
            } else if(type == Date.class) {
                Long data = Long.parseLong(result);
                f.set(obj, new Date(data * 1000));
            } else if(type.isEnum()) {
                f.set(obj, Enum.valueOf((Class<Enum>) type, result));
            }
        }
    }

    private static String getAsString(Object obj, Field f) throws IllegalAccessException, IllegalArgumentException, IOException {
        Object fieldValue = f.get(obj);
        if(fieldValue == null) {
            return null;
        }
        Class<?> clazzType = f.getType();
        if(clazzType == HasServerId.class) {
            return Long.toString(((HasServerId) f.get(obj)).getServerId());
        } else {
            if(clazzType == byte[].class) {
                byte[] encoded = Base64.encode((byte[]) fieldValue, Base64.DEFAULT);
                return new String(encoded, "UTF-8");
            } else if(clazzType == Boolean.class) {
                return ((Boolean) fieldValue) ? "true" : "false";
            } else if(clazzType.getSuperclass() == Number.class) {
                if(clazzType == Double.class) {
                    return Double.toString((Double) fieldValue);
                } else if(clazzType == Float.class) {
                    return Float.toString((Float) fieldValue);
                } else if(clazzType == Long.class) {
                    return Long.toString((Long) fieldValue);
                } else if(clazzType == Integer.class) {
                    return Integer.toString((Integer) fieldValue);
                } else if(clazzType == Short.class) {
                    return Short.toString((Short) fieldValue);
                }
            } else if(clazzType == Date.class) {
                Date date = (Date) fieldValue;
                return Long.toString(date.getTime() / 1000);
            } else if(clazzType.isEnum()) {
                Enum anEnum = (Enum) fieldValue;
                return anEnum.name();
            }
        }
        return String.valueOf(fieldValue);
    }

    public static byte[] compress(byte[] value) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(value.length);
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(value);
        gos.close();
        byte[] compressed = os.toByteArray();
        os.close();
        return compressed;
    }

    public static byte[] decompress(byte[] compressed) throws IOException {
        final int BUFFER_SIZE = 32;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        while((bytesRead = gis.read(data)) != -1) {
            string.append(new String(data, 0, bytesRead));
        }
        gis.close();
        is.close();
        return string.toString().getBytes();
    }

    public interface ObjectParsedListener<T> {

        void onObjectParsed(T object, JSONObject jsonObject, Integer api);
    }

}