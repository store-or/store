package com.store.domain;

/**
 * Created with IntelliJ IDEA.
 * User: laizy
 * Date: 14-11-22
 * Time: 下午10:54
 * To change this template use File | Settings | File Templates.
 */
public class FileInfo {
    // 访问的url,绝对路径
    private String visitUrl ;
    // 数据库保存的相对访问路径
    private String url ;
    private String name;
    private Integer width;
    private Integer height;

    public String getVisitUrl() {
        return visitUrl;
    }

    public void setVisitUrl(String visitUrl) {
        this.visitUrl = visitUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
