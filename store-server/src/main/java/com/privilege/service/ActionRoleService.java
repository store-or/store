package com.privilege.service;

import com.core.dao.TransactionBaseService;
import com.privilege.domain.Action;
import com.privilege.domain.ActionRole;
import com.privilege.domain.Role;
import com.privilege.security.SecurityContext;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 */
@Service
public class ActionRoleService extends TransactionBaseService<ActionRole,Long> {

    @Autowired
    private RoleService roleService;

    @Autowired
    private ActionService actionService;


    @Transactional
    public List<ActionRole> findActionByUserId(Long userId) {
        Set<Role> userRoles = roleService.findRoleByUserId(userId);
        if (userRoles.size() < 1) {
            return Collections.emptyList();
        }
        List<Long> roleIds = new ArrayList<Long>(userRoles.size());
        for (Role role : userRoles) {
            roleIds.add(role.getId());
        }
        return find(Restrictions.in("roleId", roleIds));
    }


    @SuppressWarnings("unchecked")
    @Transactional
    public Set<Action> findActionsByRole(Long roleId) {
        Set<Action> actions = new HashSet<Action>();
        if (SecurityContext.ROLE_SUPERUSER_ID.equals(roleId)) {
            actions.addAll(actionService.find());
        } else {
            List<ActionRole> actionRoles = find(Restrictions.eq("roleId", roleId));
            for (ActionRole actionRole : actionRoles) {
                actions.add(actionRole.getAction());
            }
        }
        return actions;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<ActionRole> findByRoleIds(List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            return find(Restrictions.in("roleId", ids));
        } else {
            return null;
        }
    }

}