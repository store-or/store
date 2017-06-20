package com.core.dao;

import com.google.common.collect.Iterators;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

/**
 * Created by laizy on 2017/6/7.
 */
@JsonSerialize(using = PropertyFilterSerializer.class)
@JsonDeserialize(using = PropertyFilterDeserializer.class)
public class PropertyFilter {

    public static final String STRING_TO_ARRAY_SEPARATOR = "--##--";
    /*****/
    public static final String STRING_TYPE = "string";
    public static final String LONG_TYPE = "long";
    public static final String INTEGER_TYPE = "int";
    public static final String SHORT_TYPE = "short";
    public static final String FLOAT_TYPE = "float";
    public static final String DOUBLE_TYPE = "double";
    public static final String DATE_START_TYPE = "date";   //date | Format dateyyyy-MM-dd HH:mm:ss
    public static final String BOOLEAN_TYPE = "boolean";

    public static final String TEXT_TO_ARRAY = "text_to_array";
    public static final String VARCHAR_TO_ARRAY = "varchar_to_array";

    protected static Logger logger = LoggerFactory.getLogger(PropertyFilter.class);
    /**
     * 多个属性间OR关系的分隔符.
     */
    public static final String OR_SEPARATOR = "_OR_";

    public static final String REVERSE_STRING = "not_";

    /**
     * 属性比较类型.
     */
    public enum MatchType {
        EQ,ILIKE, LIKE, GE, LE, GT, LT, IN,
        LIKE_START, LIKE_END,
        EQ_PROPERTY, GE_PROPERTY,LE_PROPERTY,GT_PROPERTY,LT_PROPERTY,
        SQL_RESTRICTION, SQL_RESTRICTION_ARRAY,
        CONTAINS, //for TEXT_TO_ARRAY, VARCHAR_TO_ARRAY
        ALIAS     // AliasExpression
    }

    protected String propertyName;
    protected Object value;
    protected MatchType matchType = MatchType.EQ;
    //Restriction.not(Criterion c)
    protected boolean reverse = false;
    protected String type;

    public PropertyFilter() {
    }

    public PropertyFilter(final String propertyName, final MatchType matchType, final Object value) {
        this(propertyName, matchType, value, false);
    }

    public PropertyFilter(final String propertyName, final MatchType matchType, final Object value, final boolean reverse) {
        this.propertyName = propertyName;
        this.matchType = matchType;
        this.value = value;
        this.reverse = reverse;
        if (value instanceof Object[]) {
            Object obj[] = (Object[]) value;
            if (obj.length > 0) {
                detectType(obj[0].getClass());
            }
        } else if (value instanceof Collection) {
            Collection<?> collection = (Collection) value;
            if (collection.size() > 0) {
                Object obj = Iterators.get(collection.iterator(), 0);
                detectType(obj.getClass());
            }
        } else {
            detectType(this.value.getClass());
        }
    }

    public PropertyFilter(String propertyName,MatchType matchType, Object value,  boolean reverse, String type) {
        this.propertyName = propertyName;
        this.value = value;
        this.matchType = matchType;
        this.reverse = reverse;
        this.type = type;
    }

    private void detectType(Class clazz) {
        if (Long.class == clazz) {
            setType(LONG_TYPE);
        } else if (String.class == clazz) {
            setType(STRING_TYPE);
        } else if (Integer.class == clazz) {
            setType(INTEGER_TYPE);
        } else if (Short.class == clazz) {
            setType(SHORT_TYPE);
        } else if (Float.class == clazz) {
            setType(FLOAT_TYPE);
        } else if (Double.class == clazz) {
            setType(DOUBLE_TYPE);
        } else if (Date.class == clazz) {
            setType(DATE_START_TYPE);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取属性名称,可用'_OR_'分隔多个属性,此时属性间是or的关系.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 设置属性名称,可用'_OR_'分隔多个属性,此时属性间是or的关系.
     */
    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(final MatchType matchType) {
        this.matchType = matchType;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public boolean isNumber() {
        return LONG_TYPE.equals(this.type) ||
                INTEGER_TYPE.equals(this.type) ||
                SHORT_TYPE.equals(this.type) ||
                FLOAT_TYPE.equals(this.type) ||
                DOUBLE_TYPE.equals(this.type);
    }
}
