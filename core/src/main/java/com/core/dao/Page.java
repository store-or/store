package com.core.dao;

import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by laizy on 2017/6/7.
 */
public class Page<T> {
    // 公共变量
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final int DEFAULT_PAGE_SIZE = 20 ;

    //分页参数
    protected int pageNo = 1;
    protected int pageSize = 20;
    protected String orderBy = null;
    protected String order = null;
    protected boolean autoCount = true;

    private int first = -1;

    //返回结果
    protected List<T> result = Collections.emptyList();
    protected long totalCount = -1;

    private int totalPage;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        if(StringUtils.isNotBlank(pageId)){
            this.pageId = pageId;
        }
    }

    private String pageId = "page";

    // 构造函数

    public Page() {
    }

    public Page(int pageNo) {
        this(pageNo, DEFAULT_PAGE_SIZE);
    }
    public Page(int pageNo,String pageId) {
        this(pageNo, DEFAULT_PAGE_SIZE,pageId);
    }

    public Page(int pageNo,final int pageSize) {
        this(pageNo, pageSize, true);
    }
    public Page(int pageNo,final int pageSize,String pageId) {
        this(pageNo, pageSize, true,pageId);
    }

    public Page(int pageNo,final int pageSize, final boolean autoCount) {
        this(pageNo, pageSize, autoCount,null);
    }
    public Page(int pageNo,final int pageSize, final boolean autoCount,String pageId) {
        this.pageNo = pageNo;
        setPageSize(pageSize);
        this.autoCount = autoCount;
        setPageId(pageId);
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(final int pageNo) {
        this.pageNo = pageNo;

        if (pageNo < 1) {
            this.pageNo = 1;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;

/*        if (pageSize < 1) {
            this.pageSize = 1;
        }*/
    }

    public int getFirst() {
        if (first == -1) {
            return ((pageNo - 1) * pageSize) + 1;
        } else {
            return first;
        }
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }


    public boolean isOrderBySetted() {
        return StringUtils.isNotBlank(orderBy);
    }


    public String getOrder() {
        return order;
    }

    /**
     * 设置排序方式向.
     *
     * @param order 可选值为desc或asc,多个排序字段时用','分隔.
     */
    public void setOrder(final String order) {
        //检查order字符串的合法值
        String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
        for (String orderStr : orders) {
            if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr))
                throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
        }

        this.order = StringUtils.lowerCase(order);
    }


    public boolean isAutoCount() {
        return autoCount;
    }

    public void setAutoCount(final boolean autoCount) {
        this.autoCount = autoCount;
    }

    // 查询结果函数

    public List<T> getResult() {
        return result;
    }

    public void setResult(final List<T> result) {
        this.result = result;
    }


    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
        setTotalPage();
    }


    public int getTotalPage() {

        return totalPage;
    }

    public void setTotalPage() {
        if (totalCount < 0) {
            totalPage = 0;
            return;
        }

        int count = (int) (totalCount / pageSize);
        if (totalCount % pageSize > 0) {
            count++;
        }
        this.totalPage = count;
    }


    public boolean isHasNext() {
        return (pageNo + 1 <= getTotalPage());
    }


    public int getNextPage() {
        if (isHasNext())
            return pageNo + 1;
        else
            return pageNo;
    }


    public boolean isHasPre() {
        return (pageNo - 1 >= 1);
    }


    public int getPrePage() {
        if (isHasPre())
            return pageNo - 1;
        else
            return pageNo;
    }
}
