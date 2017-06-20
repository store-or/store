package com.core.dao;

import com.google.common.collect.Lists;
import com.core.util.ReflectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.*;

/**
 * Created by laizy on 2017/6/7.
 */
public class HibernateDao<T, PK extends Serializable> extends SimpleHibernateDao<T, PK> {


    /**
     * 用于Service层直接使用HibernateDAO的构造函数.
     * 在构造函数中定义对象类型Class.
     * eg.
     * HibernateDao<User, Long> userDao = new HibernateDao<User, Long>(sessionFactory, User.class);
     */
    public HibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
        super(sessionFactory, entityClass);
    }

    /**
     * 按HQL分页查询.
     * 不支持自动获取总结果数,需用户另行执行查询.
     *
     * @param page   分页参数.仅支持pageSize 和firstResult,忽略其他参数.
     * @param hql    hql语句.
     * @param values 数量可变的查询参数,按顺序绑定.
     * @return 分页查询结果,附带结果列表及所有查询时的参数.
     */
    @SuppressWarnings("unchecked")
    public Page<T> find(final Page<T> page, final String hql, final Object... values) {

        Query q = createQuery(hql, values);
        setPageParameter(q, page);
        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, values);
            page.setTotalCount(totalCount);
        }
        List result = q.list();
        page.setResult(result);
        return page;
    }

    /**
     * 按HQL分页查询.
     * 不支持自动获取总结果数,需用户另行执行查询.
     *
     * @param page   分页参数.仅支持pageSize 和firstResult,忽略其他参数.
     * @param hql    hql语句.
     * @param values 命名参数,按名称绑定.
     * @return 分页查询结果,附带结果列表及所有查询时的参数.
     */
    @SuppressWarnings("unchecked")
    public Page<T> find(final Page<T> page, final String hql, final Map<String, Object> values) {

        Query q = createQuery(hql, values);
        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, values);
            page.setTotalCount(totalCount);
        }
        setPageParameter(q, page);
        List result = q.list();
        page.setResult(result);
        return page;
    }

    /**
     * 按Criteria分页查询.
     *
     * @param page       分页参数.支持pageSize、firstResult和orderBy、order、autoCount参数.
     *                   其中autoCount指定是否动态获取总结果数.
     * @param criterions 数量可变的Criterion.
     * @return 分页查询结果.附带结果列表及所有查询时的参数.
     */
    @SuppressWarnings("unchecked")
    public Page<T> find(final Page<T> page, final Criterion... criterions) {

        Criteria c = createCriteria(criterions);

        if (page.isAutoCount()) {
            long totalCount = countCriteriaResult(c);
            page.setTotalCount(totalCount);
        }

        setPageParameter(c, page);
        List result = c.list();
        page.setResult(result);
        return page;
    }

    /**
     * 设置分页参数到Query对象,辅助函数.
     */
    public Query setPageParameter(final Query q, final Page<T> page) {
        //hibernate的firstResult的序号从0开始
        q.setFirstResult(page.getFirst() - 1);
        q.setMaxResults(page.getPageSize());
        return q;
    }

    /**
     * 设置分页参数到Criteria对象,辅助函数.
     */
    public Criteria setPageParameter(final Criteria c, final Page<T> page) {
        //hibernate的firstResult的序号从0开始
        c.setFirstResult(page.getFirst() - 1);
        c.setMaxResults(page.getPageSize());

        if (page.isOrderBySetted()) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');


            for (int i = 0; i < orderByArray.length; i++) {
                if (Page.ASC.equals(orderArray[i])) {
                    c.addOrder(Order.asc(orderByArray[i]));
                } else {
                    c.addOrder(Order.desc(orderByArray[i]));
                }
            }
        }
        return c;
    }

    public long countHqlResult(final String hql,Map<String,Object> values) {
        int from = hql.toLowerCase().indexOf("from");
        String count = "select count(*) " + hql.substring(from);
        Query countQuery = createQuery(count, values);
        return  ((Number)countQuery.uniqueResult()).longValue();
    }

    public long countHqlResult(final String hql,Object... values) {
        int from = hql.toLowerCase().indexOf("from");
        String count = "select count(*) " + hql.substring(from);
        Query countQuery = createQuery(count, values);
        return  ((Number)countQuery.uniqueResult()).longValue();
    }

    /**
     * 执行count查询获得本次Criteria查询所能获得的对象总数.
     */
    @SuppressWarnings("unchecked")
    public long countCriteriaResult(final Criteria c) {
        CriteriaImpl impl = (CriteriaImpl) c;

        // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();

        List<CriteriaImpl.OrderEntry> orderEntries = null;
        try {
            orderEntries = (List) ReflectionUtils.getFieldValue(impl, "orderEntries");
            ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        // 执行Count查询
        long totalCount =( (Number) c.setProjection(Projections.rowCount()).uniqueResult()).longValue();

        // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
        c.setProjection(projection);

        if (projection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (transformer != null) {
            c.setResultTransformer(transformer);
        }
        try {
            ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        return totalCount;
    }

    /**
     * 按属性过滤条件列表查找对象列表.
     */
    public List<T> find(List<PropertyFilter> filters) {
        Criterion[] criterions = buildFilterCriterions(filters);
        return find(criterions);
    }

    /**
     * 按属性过滤条件列表分页查找对象.
     */
    public Page<T> find(final Page<T> page, final List<PropertyFilter> filters) {
        Criterion[] criterions = buildFilterCriterions(filters);
        return find(page, criterions);
    }
    @SuppressWarnings("unchecked")
    public int executeUpdate(final String hql, final Map<String, Object> values) {
        Query q = createQuery(hql, values);
        return q.executeUpdate();
    }

    /**
     * 按属性条件列表创建Criterion数组,辅助函数.
     */
    public Criterion[] buildFilterCriterions(final List<PropertyFilter> filters) {
        List<Criterion> criterionList = new ArrayList<Criterion>();
        if (filters != null) {

            for (PropertyFilter filter : filters) {
                if (filter == null) {
                    continue;
                }
                String propertyName = filter.getPropertyName();

                boolean multiProperty = StringUtils.contains(propertyName, PropertyFilter.OR_SEPARATOR);
                if (!multiProperty) { //properNameName中只有一个属性的情况.
                    Criterion criterion = buildPropertyCriterion(propertyName, filter);
                    criterionList.add(criterion);
                } else {//properName中包含多个属性的情况,进行or处理.
                    Disjunction disjunction = Restrictions.disjunction();
                    String[] params = StringUtils.split(propertyName, PropertyFilter.OR_SEPARATOR);

                    for (String param : params) {
                        Criterion criterion = buildPropertyCriterion(param, filter);
                        disjunction.add(criterion);
                    }
                    criterionList.add(disjunction);
                }
            }
        }
        return criterionList.toArray(new Criterion[criterionList.size()]);
    }

    /**
     * 按属性条件参数创建Criterion,辅助函数.
     */
    protected Criterion buildPropertyCriterion(String propertyName, PropertyFilter propertyFilter) {
        Object value = propertyFilter.getValue();
        PropertyFilter.MatchType matchType = propertyFilter.getMatchType();
        if (value == null) {
            return null;
        }
        Criterion criterion = null;

        switch (matchType) {
            case EQ :
                criterion = Restrictions.eq(propertyName, value);
                break;
            case LIKE:
                criterion = Restrictions.like(propertyName, (String) value, MatchMode.ANYWHERE);
                break;
            case ILIKE:
                criterion = Restrictions.ilike(propertyName, (String) value, MatchMode.ANYWHERE);
                break;
            case LIKE_START:
                criterion = Restrictions.like(propertyName, (String) value, MatchMode.START);
                break;
            case LIKE_END:
                criterion = Restrictions.like(propertyName, (String) value, MatchMode.END);
                break;
            case GE:
                criterion = Restrictions.ge(propertyName, value);
                break;
            case GT:
                criterion = Restrictions.gt(propertyName, value);
                break;
            case LE:
                criterion = Restrictions.le(propertyName, value);
                break;
            case LT:
                criterion = Restrictions.lt(propertyName, value);
                break;
            case IN:
                if (value instanceof Object[]) {
                    if (((Object[]) value).length < 1) {
                        return null;
                    }
                    criterion = Restrictions.in(propertyName, (Object[]) value);
                } else if (value instanceof Collection) {
                    if (((Collection) value).size() < 1) {
                        return null;
                    }
                    criterion = Restrictions.in(propertyName, (Collection) value);
                } else {
                    throw new RuntimeException("not");
                }
                break;
            case EQ_PROPERTY:
                criterion = Restrictions.eqProperty(propertyName, (String) value);
                break;
            case GE_PROPERTY:
                criterion = Restrictions.geProperty(propertyName, (String) value);
                break;
            case GT_PROPERTY:
                criterion = Restrictions.gtProperty(propertyName, (String) value);
                break;
            case LE_PROPERTY:
                criterion = Restrictions.leProperty(propertyName, (String) value);
                break;
            case LT_PROPERTY:
                criterion = Restrictions.ltProperty(propertyName, (String) value);
                break;
            case SQL_RESTRICTION:
                criterion = Restrictions.sqlRestriction(propertyName, value, HibernateTypeExchange.exchange(propertyFilter.getType()));
                break;
            case SQL_RESTRICTION_ARRAY:
                if (value instanceof Collection) {
                    Collection<?> collection = (Collection) value;
                    Object[] objects = collection.toArray(new Object[collection.size()]);
                    Type[] types = new Type[objects.length];
                    Arrays.fill(types, HibernateTypeExchange.exchange(propertyFilter.getType()));
                    criterion = Restrictions.sqlRestriction(propertyName, objects, types);
                } else if (value instanceof Object[]) {
                    Object[] objects = (Object[]) value;
                    Type[] types = new Type[objects.length];
                    Arrays.fill(types, HibernateTypeExchange.exchange(propertyFilter.getType()));
                    criterion = Restrictions.sqlRestriction(propertyName, objects, types);
                } else {
                    criterion = Restrictions.sqlRestriction(propertyName, value, HibernateTypeExchange.exchange(propertyFilter.getType()));
                }
                break;
            case ALIAS:
                criterion = new AliasExpression(propertyName, propertyName,(Integer)value);
                break;

        }

        if (propertyFilter.isReverse()) {
            criterion = Restrictions.not(criterion);
        }

        return criterion;
    }

    /**
     * 判断对象的属性值在数据库内是否唯一.
     * <p/>
     * 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
     */
    public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
        if (newValue == null || newValue.equals(oldValue))
            return true;
        Object object = findByUnique(propertyName, newValue);
        return (object == null);
    }

    /**
     * @author hongxingshi
     * @param page
     * @param hql
     * @param filterList
     * @param values
     * @return
     */
    @SuppressWarnings("unchecked")
    public Page<T> find(final Page<T> page, final String hql,List<PropertyFilter> filterList, final Object... values) {
        if (StringUtils.isBlank(hql)) {
            return find(page, filterList);
        }
        Query q;
        StringBuilder queryHql = new StringBuilder(hql);
        List<Object> valueList = Lists.newArrayList(values);
        if (filterList != null) {
            if (!StringUtils.containsIgnoreCase(hql, "where")) {
                queryHql.append(" where 1 = 1 ");
            }
            for (PropertyFilter filter : filterList) {
                buildPropertyHql(queryHql, filter, valueList);
            }
        }
        if (page.isAutoCount()) {
            int from = queryHql.toString().toLowerCase().indexOf("from");
            String countHql = "select count(*) " + queryHql.substring(from);
            long totalCount = countHqlResult(countHql, valueList.toArray());
            page.setTotalCount(totalCount);
        }
        if (StringUtils.isNotBlank(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
            queryHql.append(" order by ").append(page.getOrderBy()).append(" ").append(page.getOrder());
        }
        q = createQuery(queryHql.toString(), valueList.toArray());
        setPageParameter(q, page);
        List result = q.list();
        page.setResult(result);
        return page;

    }

    /**
     * @author hongxingshi
     * @param hql
     * @param filterList
     * @param values
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> find(final String hql, List<PropertyFilter> filterList,final Object... values) {
        if (StringUtils.isBlank(hql)) {
            return find(filterList);
        }
        StringBuilder queryHql = new StringBuilder(hql);
        Query q = null;
        if (filterList != null) {
            List<Object> valueList = new ArrayList<Object>();
            valueList.addAll(Arrays.asList(values));
            if (!StringUtils.containsIgnoreCase(hql, "where")) {
                queryHql.append(" where 1 = 1 ");
            }
            for (PropertyFilter filter : filterList) {
                buildPropertyHql(queryHql, filter, valueList);
            }
            q = createQuery(queryHql.toString(), valueList.toArray());
        }
        if (q != null) {
            return q.list();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * @param propertyFilters :
     * @param parameters : 存放生成的?,必须传进来且list不能是unmodified
     * @return 已经包含where关键字了，必须放在where语句开头
     */
    public String parseWhere(List<PropertyFilter> propertyFilters, final List<Object> parameters) {
        if (propertyFilters == null || propertyFilters.size() < 1 || parameters == null) {
            return "";
        }
        StringBuilder where = new StringBuilder(" where 1 = 1 ");
        for (PropertyFilter propertyFilter : propertyFilters) {
            buildPropertyHql(where, propertyFilter, parameters);
        }
        return where.toString();
    }

    /**
     * @author hongxingshi
     * @param queryHql
     * @param filter
     * @param values
     */
    private void buildPropertyHql(StringBuilder queryHql, PropertyFilter filter, List<Object> values){
        String propertyName = filter.getPropertyName();
        Object value = filter.getValue();
        PropertyFilter.MatchType matchType = filter.getMatchType();
        if (value == null) {
            return;
        }
        queryHql.append(" and ");
        queryHql.append(propertyName);
        switch (matchType) {
            case EQ :
                if(filter.isReverse()){
                    queryHql.append(" <> ");
                }else {
                    queryHql.append(" = ");
                }
                queryHql.append("?");
                values.add(value);
                break;
            case LIKE:
            case LIKE_START:
            case LIKE_END:
                if(filter.isReverse()){
                    queryHql.append(" not like ");
                }else {
                    queryHql.append(" like ");
                }
                queryHql.append("?");
                StringBuilder sb = new StringBuilder();
                if (matchType == PropertyFilter.MatchType.LIKE || matchType == PropertyFilter.MatchType.LIKE_END) {
                    sb.append("%");
                }
                sb.append(value);
                if (matchType == PropertyFilter.MatchType.LIKE || matchType == PropertyFilter.MatchType.LIKE_START) {
                    sb.append("%");
                }
                values.add(sb.toString());
                break;
            case GE:
                if(filter.isReverse()){
                    queryHql.append(" < ");
                }else {
                    queryHql.append(" >= ");
                }
                queryHql.append("?");
                values.add(value);
                break;
            case GT:
                if(filter.isReverse()){
                    queryHql.append(" <= ");
                }else {
                    queryHql.append(" > ");
                }
                queryHql.append("?");
                values.add(value);
                break;
            case LE:
                if(filter.isReverse()){
                    queryHql.append(" > ");
                }else {
                    queryHql.append(" <= ");
                }
                queryHql.append("?");
                values.add(value);
                break;
            case LT:
                if(filter.isReverse()){
                    queryHql.append(" >= ");
                }else {
                    queryHql.append(" < ");
                }
                queryHql.append("?");
                values.add(value);
                break;
            case IN:
                if (value instanceof Object[]) {
                    if (((Object[]) value).length < 1) {
                        return;
                    }
                    if(filter.isReverse()){
                        queryHql.append(" not in ");
                    }else {
                        queryHql.append(" in ");
                    }
                    queryHql.append(" ( ");
                    boolean flag = true;
                    for(Object inVal: (Object[])value){
                        if(flag){
                            flag=false;
                        }else{
                            queryHql.append(",");
                        }
                        queryHql.append("?");
                        values.add(inVal);
                    }
                    queryHql.append(" ) ");

                } else if (value instanceof Collection) {
                    if (((Collection) value).size() < 1) {
                        return;
                    }
                    if(filter.isReverse()){
                        queryHql.append(" not in ");
                    }else {
                        queryHql.append(" in ");
                    }
                    queryHql.append(" ( ");
                    boolean flag = true;
                    for(Object inVal: (Collection)value){
                        if(flag){
                            flag=false;
                        }else{
                            queryHql.append(",");
                        }
                        queryHql.append("?");
                        values.add(inVal);
                    }
                    queryHql.append(" ) ");
                } else {
                    throw new RuntimeException("not");
                }
                break;
            case EQ_PROPERTY:
                if(filter.isReverse()){
                    queryHql.append(" <> ");
                }else {
                    queryHql.append(" = ");
                }
                queryHql.append((String)value);
                break;
            case GE_PROPERTY:
                if(filter.isReverse()){
                    queryHql.append(" < ");
                }else {
                    queryHql.append(" >= ");
                }
                queryHql.append((String)value);
                break;
            case GT_PROPERTY:
                if(filter.isReverse()){
                    queryHql.append(" <= ");
                }else {
                    queryHql.append(" > ");
                }
                queryHql.append((String)value);
                break;
            case LE_PROPERTY:
                if(filter.isReverse()){
                    queryHql.append(" > ");
                }else {
                    queryHql.append(" <= ");
                }
                queryHql.append((String)value);
                break;
            case LT_PROPERTY:
                if(filter.isReverse()){
                    queryHql.append(" >= ");
                }else {
                    queryHql.append(" < ");
                }
                queryHql.append((String)value);
                break;
        }
    }
}

