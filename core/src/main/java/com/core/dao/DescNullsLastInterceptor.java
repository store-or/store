package com.core.dao;

import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by laizy on 2017/6/7.
 */
public class DescNullsLastInterceptor extends EmptyInterceptor {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static final String DESC = " desc";
    private static final String DESC_NULLS_LAST = " desc nulls last";

    @Override
    public String onPrepareStatement(String sql) {
        // 在最后补上空格
        String tmpSql = sql + " ";
        // 找出desc的关键字
        Pattern pattern = Pattern.compile("([\\s]+desc[,\\s]+)", Pattern.CASE_INSENSITIVE);
        StringBuilder builder = new StringBuilder(1024);
        Matcher matcher = pattern.matcher(tmpSql);
        int start = 0;
        int end = tmpSql.length();
        while (matcher.find()) {
            int tmpStart = matcher.start(1);
            builder.append(tmpSql.substring(start, tmpStart));
            builder.append(matcher.group().replace(DESC, DESC_NULLS_LAST));
            start = matcher.end(1);
        }
        if (start < end) {
            builder.append(tmpSql.substring(start, end));
        }
        return super.onPrepareStatement(builder.toString());
    }

}
