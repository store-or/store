package com.core.dao;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by laizy on 2017/6/7.
 */
public class PropertyFilterDeserializer extends JsonDeserializer<PropertyFilter> {

    private static final Logger logger = LoggerFactory.getLogger(PropertyFilterDeserializer.class);

    @Override
    public PropertyFilter deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        PropertyFilter filter = new PropertyFilter();
        JsonNode node =  jp.readValueAsTree();
        JsonNode propertyName =  node.findValue("propertyName");
        filter.setPropertyName(propertyName.getTextValue());
        JsonNode condition = node.findValue("condition");
        String con = condition.getTextValue();
        int index = con.indexOf(PropertyFilter.REVERSE_STRING);
        if (index == -1) {
            filter.setMatchType(PropertyFilter.MatchType.valueOf(con.toUpperCase()));
        } else {
            filter.setMatchType(PropertyFilter.MatchType.valueOf(con.substring(index + PropertyFilter.REVERSE_STRING.length()).toUpperCase()));
            filter.setReverse(true);
        }
        JsonNode type = node.findValue("type");
        String typeValue = type.getTextValue();
        filter.setType(typeValue);
        JsonNode value = node.findValue("value");

        if (PropertyFilter.STRING_TYPE.equalsIgnoreCase(typeValue)) {
            filter.setValue(value.getTextValue());
        } else if (PropertyFilter.LONG_TYPE.equalsIgnoreCase(typeValue)) {
            filter.setValue(value.getLongValue());
        } else if (PropertyFilter.INTEGER_TYPE.equalsIgnoreCase(typeValue)) {
            filter.setValue(value.getIntValue());
        } else if (PropertyFilter.SHORT_TYPE.equalsIgnoreCase(typeValue)) {
            filter.setValue(value.getNumberValue().shortValue());
        } else if (PropertyFilter.FLOAT_TYPE.equalsIgnoreCase(typeValue)) {
            filter.setValue(value.getNumberValue().floatValue());
        } else if (PropertyFilter.DOUBLE_TYPE.equalsIgnoreCase(typeValue)) {
            filter.setValue(value.getDoubleValue());
        } else if (PropertyFilter.BOOLEAN_TYPE.equalsIgnoreCase(typeValue)) {
            filter.setValue(value.getBooleanValue());
        } else if (typeValue.startsWith("date")) {
            //date2012-09-10 11:11:00
            //test
            if (value.isNumber()) {
                filter.setValue(new Date(value.getNumberValue().longValue()));
            } else {
                String format = typeValue.substring(4);
                if (StringUtils.isBlank(format)) {
                    format = "EEE MMM dd HH:mm:ss zzz yyyy";
                }
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                try {
                    Date date = sdf.parse(value.getTextValue());
                    filter.setValue(date);
                } catch (ParseException e) {
                    logger.warn(e.getMessage(), e);
                }
            }

        } else if (PropertyFilter.TEXT_TO_ARRAY.equalsIgnoreCase(typeValue) ||
                PropertyFilter.VARCHAR_TO_ARRAY.equalsIgnoreCase(typeValue)) {
            filter.setValue(value.getTextValue());
        }
        return filter;
    }
}
