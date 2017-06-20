package com.core.dao;

import com.core.dao.annotation.Alias;
import org.hibernate.*;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by laizy on 2017/6/7.
 */
public class SimpleHibernateDao<T, PK extends Serializable> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected SessionFactory sessionFactory;

    protected Class<T> entityClass;

    protected Map<String,Alias> aliasMap = new HashMap<String, Alias>();
    /**
     * 用于Service层直接使用SimpleHibernateDAO的构造函数.
     * 在构造函数中定义对象类型Class.
     * eg.
     * SimpleHibernateDao<User, Long> userDao = new SimpleHibernateDao<User, Long>(sessionFactory, User.class);
     */
    public SimpleHibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(this.entityClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method readMethod = propertyDescriptor.getReadMethod();
                Alias annotation = readMethod.getAnnotation(Alias.class);
                if (annotation != null) {
                    aliasMap.put(propertyDescriptor.getName(), annotation);
                }
            }
        } catch (IntrospectionException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * 采用@Autowired按类型注入SessionFactory,当有多个SesionFactory的时候Override本函数.
     */
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 保存新增或修改的对象.
     */
    public void saveOrUpdate(final T entity) {
        getSession().saveOrUpdate(entity);
        logger.debug("save entity: {}", entity);
    }

    /*
    * 修改合并对象
    * */
    public T merge(final T entity) {
        logger.debug("merge entity:{}", entity);
        return (T) getSession().merge(entity);
    }

    /*
    * 持久化对象
    * */
    public void persist(final T entity) {
        getSession().persist(entity);
        logger.debug("persist entity:{}", entity);
    }

    /**
     * 删除对象.
     *
     * @param entity 对象必须是session中的对象或含id属性的transient对象.
     */
    public void delete(final T entity) {
        getSession().delete(entity);
        logger.debug("delete entity: {}", entity);
    }

    /**
     * 按id删除对象.
     */
    public void delete(final PK id) {
        delete(get(id));
        logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), id);
    }

    /**
     * 按id获取对象.
     */
    public T get(final PK id) {
        return (T) getSession().get(entityClass, id);
    }

    /**
     * 获取全部对象.
     */
    public List<T> getAll() {
        return find();
    }

    public void batchSave(Collection<T> collection) {
        batchSave(collection, 20);
    }

    public void batchSave(Collection<T> collection, int batch) {
        int count = 0;
        for (T t : collection) {
            saveOrUpdate(t);
            if (++count % batch == 0) {
                getSession().flush();
                getSession().clear();
            }
        }
    }

    /**
     * 按属性查找对象列表,匹配方式为相等.
     */
    public List<T> findBy(final String propertyName, final Object value) {
        Criterion criterion = Restrictions.eq(propertyName, value);
        return find(criterion);
    }

    /**
     * 按属性查找唯一对象,匹配方式为相等.
     */
    public T findByUnique(final String propertyName, final Object value) {
        Criterion criterion = Restrictions.eq(propertyName, value);
        return (T) createCriteria(criterion).uniqueResult();
    }

    /**
     * 按HQL查询对象列表.
     *
     * @param values 数量可变的参数,按顺序绑定.
     */
    public List<T> find(final String hql, final Object... values) {
        return createQuery(hql, values).list();
    }

    /**
     * 按HQL查询对象列表.
     *
     * @param values 命名参数,按名称绑定.
     */
    public List<T> find(final String hql, final Map<String, Object> values) {
        return createQuery(hql, values).list();
    }

    /**
     * 按HQL查询唯一对象.
     *
     * @param values 数量可变的参数,按顺序绑定.
     */
    public T findUnique(final String hql, final Object... values) {
        return (T) createQuery(hql, values).uniqueResult();
    }

    /**
     * 按HQL查询唯一对象.
     *
     * @param values 命名参数,按名称绑定.
     */
    public T findUnique(final String hql, final Map<String, Object> values) {
        return (T) createQuery(hql, values).uniqueResult();
    }

    /**
     * 按HQL查询Integer类型结果.
     */
    public Integer findInt(final String hql, final Object... values) {
        return (Integer) findUnique(hql, values);
    }

    public Integer findInt(final String hql, final Map<String, Object> values) {
        return (Integer) findUnique(hql, values);
    }

    /**
     * 按HQL查询Long类型结果.
     */
    public Long findLong(final String hql, final Object... values) {
        return (Long) findUnique(hql, values);
    }

    /**
     * 按HQL查询Long类型结果.
     */
    public Long findLong(final String hql, final Map<String, Object> values) {
        return (Long) findUnique(hql, values);
    }

    /**
     * 根据查询HQL与参数列表创建Query对象.
     * <p/>
     * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
     *
     * @param values 数量可变的参数,按顺序绑定.
     */
    public Query createQuery(final String queryString, final Object... values) {
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    /**
     * 根据查询HQL与参数列表创建Query对象.
     *
     * @param values 命名参数,按名称绑定.
     */
    public Query createQuery(final String queryString, final Map<String, Object> values) {
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            query.setProperties(values);
        }
        return query;
    }

    /**
     * 按Criteria查询对象列表.
     *
     * @param criterions 数量可变的Criterion.
     */
    public List<T> find(final Criterion... criterions) {
        return createCriteria(criterions).list();
    }

    public List<T> find(ResultTransformer resultTransformer, final Criterion... criterions) {
        Criteria criteria = createCriteria(criterions);
        criteria.setResultTransformer(resultTransformer);
        return criteria.list();
    }

    /**
     * 按Criteria查询唯一对象.
     *
     * @param criterions 数量可变的Criterion.
     */
    public T findUnique(final Criterion... criterions) {
        return (T) createCriteria(criterions).uniqueResult();
    }

    /**
     * 根据Criterion条件创建Criteria.
     * <p/>
     * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
     *
     * @param criterions 数量可变的Criterion.
     */
    public Criteria createCriteria(final Criterion... criterions) {
        return createCriteria(CriteriaSpecification.ROOT_ALIAS, criterions);
    }

    public Criteria createCriteria(final String alias, final Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass, alias);
        List<AliasExpression> aliasExpressions = new ArrayList<AliasExpression>();
        for (Criterion c : criterions) {
            if (c != null) {
                if (c instanceof AliasExpression) {
                    aliasExpressions.add((AliasExpression) c);
                } else {
                    criteria.add(c);
                }
            }
        }
        for (AliasExpression aliasExpression : aliasExpressions) {
            aliasExpression.createAlias(criteria);
        }
        for (Map.Entry<String, Alias> entry : aliasMap.entrySet()) {
            criteria.createAlias(entry.getKey(), entry.getValue().value(), entry.getValue().joinType());
        }
        return criteria;
    }

    /**
     * 初始化被lazy initialize的关联集合.
     */
    public void initCollection(Collection collection) {
        if (!Hibernate.isInitialized(collection)) {
            Hibernate.initialize(collection);
        }
    }

    /**
     * 取得对象的主键名.
     */
    public String getIdName() {
        ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
        return meta.getIdentifierPropertyName();
    }
}

