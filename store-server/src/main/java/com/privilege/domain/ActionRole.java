package com.privilege.domain;

import com.core.dao.IdDO;
import com.core.dao.annotation.Alias;

import javax.persistence.*;

/**
 */
@Entity
@Table(name = "action_role")
@SequenceGenerator(name = "seq_gen", sequenceName = "seq_action_role")
public class ActionRole extends IdDO {

    private Action action;
    private Long roleId;

    public ActionRole() {
    }

    public ActionRole(String action, Long roleId) {
        this.action = new Action();
        this.action.setName(action);
        this.roleId = roleId;
    }

    @ManyToOne
    @JoinColumn(name = "action", referencedColumnName = "name")
    @Alias(value = "action", joinType = 0)
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Column(name = "role_id")
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}