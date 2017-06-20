package com.privilege.service.resource.template;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.util.HashSet;
import java.util.Set;

/**
 */
@XmlType
public class Parameters {

    private Set<Parameter> includes = new HashSet<Parameter>();
    private Set<Parameter> excludes = new HashSet<Parameter>();

    @XmlElementWrapper(name = "includes")
    @XmlElement(name = "include")
    public Set<Parameter> getIncludes() {
        return includes;
    }

    public void setIncludes(Set<Parameter> includes) {
        this.includes = includes;
    }

    @XmlElementWrapper(name = "excludes")
    @XmlElement(name = "exclude")
    public Set<Parameter> getExcludes() {
        return excludes;
    }

    public void setExcludes(Set<Parameter> excludes) {
        this.excludes = excludes;
    }
}
