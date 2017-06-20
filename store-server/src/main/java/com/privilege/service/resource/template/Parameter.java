package com.privilege.service.resource.template;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlType
public class Parameter {

    private String name;
    private String value;

    public Parameter() {
    }

    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Parameter)) {
            return false;
        }
        Parameter parameter = (Parameter) obj;
        if (!this.name.equals(parameter.getName())) {
            return false;
        }
        if (this.value.equals("*") || parameter.getValue().equals("*") || this.value.equals(parameter.getValue())) {
            return true;
        }
        return false;
    }
}
