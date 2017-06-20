package com.core.dao;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;

/**
 * Created by laizhiyang on 2017/6/17.
 */
public class SQLCriterion implements Criterion {
    private String sqlFormula;
    private String[] replaceColumns;

    public SQLCriterion(String sqlFormula, String... replaceColumns) {
        this.sqlFormula = sqlFormula;
        this.replaceColumns = replaceColumns;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        if (ArrayUtils.isEmpty(replaceColumns)) {
            return sqlFormula;
        }
        for (String replaceName : replaceColumns) {
            String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, replaceName);
            if (ArrayUtils.isEmpty(columns)) {
                continue;
            }
            sqlFormula = sqlFormula.replaceAll(replaceName, columns[0]);
        }

        return sqlFormula;
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return new TypedValue[0];
    }
}
