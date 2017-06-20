package com.privilege.security;

/**
 */
public class SecurityResource {

    private Long id;
    private String name;
    private String resource;
    private Long parentId;

    public SecurityResource(Long id,String name,String resource,Long parentId){
        this.id = id;
        this.name = name;
        this.resource = resource;
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}