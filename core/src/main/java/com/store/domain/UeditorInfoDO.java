package com.store.domain;

/**
 * Created by wangmj on 2015/12/2.
 */

/**
 * Ueditor处理文件上传、图片上传的返回信息
 */
public class UeditorInfoDO {
    public static final String STATE_SUCCESS = "SUCCESS";

    //标题(用于回显的显示名称)
    private String title;
    //原始文件名
    private String original;
    //文件大小
    private Long size;
    //上传状态
    private String state;
    //文件类型
    private String type;
    //文件上传地址
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
