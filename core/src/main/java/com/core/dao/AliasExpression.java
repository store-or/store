package com.core.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;

/**
 * Created by laizy on 2017/6/7.
 */
public class AliasExpression implements Criterion {


    private final String associationPath;
    private final String alias;
    private final int joinType;

    public AliasExpression(String associationPath, String alias) {
        this(associationPath, alias, Criteria.INNER_JOIN);
    }

    public AliasExpression(String associationPath, String alias, int joinType) {
        this.associationPath = associationPath;
        this.alias = alias;
        this.joinType = joinType;
    }

    public Criteria createAlias(Criteria criteria) {
        return criteria.createAlias(associationPath, alias, joinType);
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return "";
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return new TypedValue[0];
    }
}
