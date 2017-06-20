package com.privilege.domain;

import com.core.dao.IdDO;
import com.core.json.JsonMapper;
import com.privilege.service.resource.template.ActionTemplate;
import com.privilege.service.resource.template.MethodType;
import com.privilege.service.resource.template.Parameters;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 */
@Entity
@Table(name = "action")
@SequenceGenerator(name = "seq_gen", sequenceName = "seq_action")
public class Action extends IdDO implements Serializable {

    private String name;
    private String uri;
    private String method = MethodType.ANY;
    private String parameter;
    private Parameters parameters;
    private String filter;
    private String description;
    private Long resourceId;

    public Action() {
    }

    public Action(String name) {
        this.name = name;
    }

    public Action(ActionTemplate actionTemplate) {
        this.name = actionTemplate.getName();
        this.method = actionTemplate.getMethod();
        if(StringUtils.isNotBlank(actionTemplate.getUri())){
            actionTemplate.setUri(actionTemplate.getUri().replace("\n",""));
            actionTemplate.setUri(actionTemplate.getUri().replace(" ",""));
        }
        this.uri = actionTemplate.getUri();
        this.description = actionTemplate.getDescription();
        this.parameter = JsonMapper.getDefault().toJson(actionTemplate.getParameters());
        this.parameters = actionTemplate.getParameters();
        this.filter = actionTemplate.getFilter();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
        this.parameters = JsonMapper.getDefault().fromJson(this.parameter, Parameters.class);
    }

    @Transient
    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "resource_id")
    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Action action = (Action) o;

        if (name != null ? !name.equals(action.name) : action.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}