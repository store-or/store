package com.privilege.domain;

import com.core.dao.IdDO;
import com.core.dao.annotation.Alias;
import org.hibernate.Criteria;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 */
@Entity
@Table(name = "resource")
@SequenceGenerator(name = "seq_gen", sequenceName = "seq_resource")
public class Resource extends IdDO implements Serializable {

    private String name;
    private String path;
    private String description;
    private String fetchClass;
    private List<Action> actions;

    public Resource() {
    }

    public Resource(String name, String path, String fetchClass, String description) {
        this.name = name;
        this.path = path;
        this.fetchClass = fetchClass;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "fetch_class")
    public String getFetchClass() {
        return fetchClass;
    }

    public void setFetchClass(String fetchClass) {
        this.fetchClass = fetchClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_id")
    @Fetch(value = FetchMode.JOIN)
    @Alias(value = "action",joinType = Criteria.LEFT_JOIN)
    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resource resource = (Resource) o;

        if (name != null ? !name.equals(resource.name) : resource.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
