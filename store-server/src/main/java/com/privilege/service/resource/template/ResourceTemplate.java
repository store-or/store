package com.privilege.service.resource.template;

import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 */
@XmlRootElement(name = "resource")
public class ResourceTemplate {

    private String name;
    private String parent;
    private String fetchClass;
    private String description;
    private List<ActionTemplate> actionTemplates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @XmlElement(name = "class")
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

    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    public List<ActionTemplate> getActionTemplates() {
        return actionTemplates;
    }

    public void setActionTemplates(List<ActionTemplate> actionTemplates) {
        this.actionTemplates = actionTemplates;
    }

    public String path(Map<String, ResourceTemplate> resourceTemplates) {
        if (StringUtils.isBlank(parent)) {
            return "/" + name;
        } else {
            ResourceTemplate parentTemplate = resourceTemplates.get(parent);
            return parentTemplate.path(resourceTemplates) + "/" + name;
        }
    }
}
