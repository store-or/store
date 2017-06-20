package com.core.dao;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by laizy on 2017/6/7.
 */
public class StringToArrayType extends AbstractSingleColumnStandardBasicType<ArrayList> {

    public static final StringToArrayType textToArray = new StringToArrayType(StringToArraySqlTypeDescriptor.text, StringToArrayJavaTypeDescriptor.instance);
    public static final StringToArrayType varcharToArray = new StringToArrayType(StringToArraySqlTypeDescriptor.varchar, StringToArrayJavaTypeDescriptor.instance);

    public static final String SEPARATOR = PropertyFilter.STRING_TO_ARRAY_SEPARATOR;

    public StringToArrayType(SqlTypeDescriptor sqlTypeDescriptor, JavaTypeDescriptor<ArrayList> javaTypeDescriptor) {
        super(sqlTypeDescriptor, javaTypeDescriptor);
    }

    @Override
    public String getName() {
        return "string_to_array";
    }

    public static class StringToArraySqlTypeDescriptor implements SqlTypeDescriptor {

        public static final StringToArraySqlTypeDescriptor text = new StringToArraySqlTypeDescriptor("text");
        public static final StringToArraySqlTypeDescriptor varchar = new StringToArraySqlTypeDescriptor("varchar");

        private String type;
        public StringToArraySqlTypeDescriptor(String type) {
            this.type = type;
        }
        @Override
        public int getSqlType() {
            return Types.ARRAY;
        }

        @Override
        public <X> ValueBinder<X> getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new BasicBinder<X>( javaTypeDescriptor, this ) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
                    List list = (List) value;
                    String[] strings = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        strings[i] = list.get(i).toString();
                    }
                    Array array = st.getConnection().createArrayOf(type, strings);
                    st.setArray(index, array);
                }
            };
        }

        @Override
        public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new BasicExtractor<X>( javaTypeDescriptor, this ) {
                @Override
                protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                    return javaTypeDescriptor.wrap( rs.getString(name), options );
                }
            };
        }
    }

    public static class StringToArrayJavaTypeDescriptor extends AbstractTypeDescriptor<ArrayList> {
        @SuppressWarnings("unchecked")
        public static final StringToArrayJavaTypeDescriptor instance = new StringToArrayJavaTypeDescriptor(ArrayList.class);

        protected static Logger logger = LoggerFactory.getLogger(StringToArrayJavaTypeDescriptor.class);

        private StringToArrayJavaTypeDescriptor(Class<ArrayList> type) {
            super(type);
        }

        @Override
        public String toString(ArrayList value) {
            return StringUtils.join(value, SEPARATOR);
        }

        @Override
        public ArrayList fromString(String string) {
            String[] split = StringUtils.split(string, SEPARATOR);
            return Lists.newArrayList(split);
        }

        @Override
        public <X> X unwrap(ArrayList value, Class<X> type, WrapperOptions options) {
            //ignore
            return null;
        }

        @Override
        public <X> ArrayList wrap(X value, WrapperOptions options) {
            return fromString((String)value);
        }
    }
}
