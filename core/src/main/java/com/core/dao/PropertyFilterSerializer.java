package com.core.dao;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by laizy on 2017/6/7.
 */
public class PropertyFilterSerializer extends JsonSerializer<PropertyFilter> {

    @Override
    public void serialize(PropertyFilter propertyFilter, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        Object value = propertyFilter.getValue();
        if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            if (array.length > 0) {
                for (Object obj : array) {
                    writeJsonObject(propertyFilter, jsonGenerator, obj);
                }
            }
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            for (Object obj : collection) {
                writeJsonObject(propertyFilter, jsonGenerator, obj);
            }
        } else {
            writeJsonObject(propertyFilter, jsonGenerator, propertyFilter.getValue());
        }
    }

    private void writeJsonObject(PropertyFilter propertyFilter, JsonGenerator jsonGenerator, Object value) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("propertyName", propertyFilter.getPropertyName());
        if (propertyFilter.isReverse()) {
            jsonGenerator.writeStringField("condition", PropertyFilter.REVERSE_STRING + propertyFilter.getMatchType().name().toLowerCase());
        } else {
            jsonGenerator.writeStringField("condition", propertyFilter.getMatchType().name().toLowerCase());
        }
        jsonGenerator.writeStringField("type", propertyFilter.getType());
        jsonGenerator.writeObjectField("value", value);
        jsonGenerator.writeEndObject();
    }
}
