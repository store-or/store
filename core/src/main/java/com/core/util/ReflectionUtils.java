package com.core.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by laizy on 2017/6/7.
 */
public class ReflectionUtils {

    private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);


    public static Object getFieldValue(final Object object, final String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

        makeAccessible(field);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }


    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
    }


    public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes,
                                      final Object[] parameters) throws InvocationTargetException {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null)
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");

        method.setAccessible(true);

        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        return null;
    }


    protected static Field getDeclaredField(final Object object, final String fieldName) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }


    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 通过反射,获得Class定义中声明的父类的泛型参数的类型.
     * eg.
     * public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
     *
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */

    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }

    public static Class getInterfaceGenericType(final Class clazz) {
        return getInterfaceGenericType(clazz, 0, 0);
    }

    public static Class getInterfaceGenericType(final Class clazz, final int interfaceIndex, final int genericTypeIndex) {

        Type[] types = clazz.getGenericInterfaces();

        if (types.length < 1 || interfaceIndex >= types.length) {
            logger.warn("{} has not interface index {} ", clazz.getSimpleName(), interfaceIndex);
            return Object.class;
        }

        Type genType = types[0];

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (genericTypeIndex >= params.length || genericTypeIndex < 0) {
            logger.warn("Index: " + genericTypeIndex + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (params[genericTypeIndex] instanceof ParameterizedType) {
            return (Class) ((ParameterizedType)params[genericTypeIndex]).getRawType();
        }

        if (!(params[genericTypeIndex] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[genericTypeIndex];
    }

    /**
     * 提取集合中的对象的属性(通过getter函数),组合成List.
     *
     * @param collection 来源集合.
     * @param propertyName 要提取的属性名.
     * @return :
     */
    @SuppressWarnings("unchecked")
    public static List fetchElementPropertyToList(final Collection collection, final String propertyName) {
        List list = new ArrayList();

        try {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        } catch (Exception e) {
            convertToUncheckedException(e);
        }

        return list;
    }

    /**
     * 提取集合中的对象的属性(通过getter函数),组合成由分割符分隔的字符串.
     *
     * @param collection 来源集合.
     * @param propertyName 要提取的属性名.
     * @param separator 分隔符.
     * @return :
     */
    @SuppressWarnings("unchecked")
    public static String fetchElementPropertyToString(final Collection collection, final String propertyName,
                                                      final String separator) {
        List list = fetchElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    /**
     * 将反射时的checked exception转换为unchecked exception。
     * @param e :
     * @throws IllegalArgumentException
     */
    public static void convertToUncheckedException(Exception e) throws IllegalArgumentException {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException)
            throw new IllegalArgumentException("Refelction Exception.", e);
        else
            throw new IllegalArgumentException(e);
    }
}

