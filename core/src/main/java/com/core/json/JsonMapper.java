package com.core.json;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.util.JSONPObject;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by laizy on 2017/6/7.
 */
public class JsonMapper {
    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    private ObjectMapper mapper;

    private static JsonMapper defaultMapper = buildNonNullMapper();

    public static JsonMapper getDefault() {
        return defaultMapper;
    }

    public JsonMapper(JsonSerialize.Inclusion inclusion) {
        mapper = new ObjectMapper();
        //设置输出时包含属性的风格
        mapper.setSerializationInclusion(inclusion);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //禁止使用int代表Enum的order()來反序列化Enum,非常危險
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);

    }

    public void configure(DeserializationConfig.Feature feature, boolean state) {
        mapper.configure(feature, state);
    }

    public void configure(SerializationConfig.Feature feature, boolean state) {
        mapper.configure(feature, state);
    }

    public void configure(JsonParser.Feature feature, boolean state) {
        mapper.configure(feature, state);
    }

    public void configure(JsonGenerator.Feature feature, boolean state) {
        mapper.configure(feature, state);
    }

    /**
     * 创建输出全部属性到Json字符串的Mapper.
     * @return :
     */
    public static JsonMapper buildNormalMapper() {
        return new JsonMapper(JsonSerialize.Inclusion.ALWAYS);
    }

    /**
     * 创建只输出非空属性到Json字符串的Mapper.
     * @return :
     */
    public static JsonMapper buildNonNullMapper() {
        return new JsonMapper(JsonSerialize.Inclusion.NON_NULL);
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper.
     * @return :
     */
    public static JsonMapper buildNonDefaultMapper() {
        return new JsonMapper(JsonSerialize.Inclusion.NON_DEFAULT);
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper.
     * @return :
     */
    public static JsonMapper buildNonEmptyMapper() {
        return new JsonMapper(JsonSerialize.Inclusion.NON_EMPTY);
    }

    /**
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     * @param object :
     * @return :
     */
    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型.
     * @param jsonString :
     * @param clazz :
     * @see #constructParametricType(Class, Class...)
     * @return :
     */
    public <T> T fromJson(String jsonString, Class<T> clazz)  {
        if (StringUtils.isEmpty(jsonString)) {
            logger.warn("jsonString is empty or null");
            return null;
        }
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型.
     * @param jsonString :
     * @param javaType  :
     * @see #constructParametricType(Class, Class...)
     * @return   :
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType)  {
        if (StringUtils.isEmpty(jsonString)) {
            logger.error("jsonString is empty or null");
            return null;
        }
        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public <T> T fromJson(InputStream is, Class<T> clazz)  {
        try {
            return mapper.readValue(is, clazz);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(InputStream is, JavaType javaType) {
        try {
            return (T) mapper.readValue(is, javaType);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 構造泛型的Type如List<MyBean>, 则调用constructParametricType(ArrayList.class,MyBean.class)
     *             Map<String,MyBean>则调用(HashMap.class,String.class, MyBean.class)
     * @param parametrized :
     * @param parameterClasses :
     * @return :
     */
    public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    public JavaType constructParametricType(Class<?> parametrized, JavaType javaType) {
        return mapper.getTypeFactory().constructParametricType(parametrized, javaType);
    }

    public JavaType constructParametricType(Class<?> parametrized, JavaType[] javaTypes) {
        return mapper.getTypeFactory().constructParametricType(parametrized, javaTypes);
    }
    /**
     * 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
     * @param object :
     * @param jsonString :
     * @return  :
     */
    @SuppressWarnings("unchecked")
    public <T> T update(T object, String jsonString) {
        try {
            return (T) mapper.readerForUpdating(object).readValue(jsonString);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 輸出JSONP格式數據.
     * @param functionName :
     * @param object :
     * @return  :
     */
    public String toJsonP(String functionName, Object object)  {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum,
     * 為False時時使用Enum的name()函數來讀寫Enum, 默認為False.
     * 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
     * @param value :
     */
    public void setEnumUseToString(boolean value) {
        mapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, value);
        mapper.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, value);
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     * @return :
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
}
