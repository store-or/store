package com.privilege.service;

import com.core.dao.TransactionBaseService;
import com.core.util.collection.Tree;
import com.core.util.collection.TreeNode;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.privilege.domain.*;
import com.privilege.exception.NotUniqueException;
import com.privilege.exception.PermissionException;
import com.privilege.exception.UserHasBeenAssignedException;
import com.privilege.security.SecurityContext;
import com.privilege.service.resource.template.FilterType;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 */
@Service
public class RoleService extends TransactionBaseService<Role,Long> {

    @Autowired
    private ActionRoleService actionRoleService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ActionService actionService;

    @Autowired
    private UserService userService;

    @Transactional
    public Tree<RoleNode> listRole() {
        List<Role> roles = find(Restrictions.not(Restrictions.eq("id", SecurityContext.ROLE_SUPERUSER_ID)));
        Tree<RoleNode> tree = new Tree<RoleNode>(newRoleRoot());
        for (Role role : roles) {
            tree.insert(new RoleNode(role));
        }
        return tree;
    }

    @Transactional
    public void save(Role role) {
        boolean unique = isPropertyUnique("name", role.getName(), null);
        if (!unique) {
            throw new NotUniqueException(role.getName() + "不唯一");
        }
        saveOrUpdate(role);
        role.setPath(role.getParentPath() + role.getId() + "/");
    }

    @Transactional
    public void save(Role role, String[] action) {
        save(role);
        assignAction(action, role.getId());
    }

    @Transactional
    public void modify(Role role) {
        Criteria criteria = baseDao.createCriteria(Restrictions.not(Restrictions.eq("id", role.getId())), Restrictions.eq("name", role.getName()));
        criteria.setProjection(Projections.rowCount());
        long count = ((Number) criteria.uniqueResult()).longValue();
        if (count > 0) {
            throw new NotUniqueException(role.getName() + " 不唯一");
        }
        String hql = "update Role set name = ? , description = ? where id = ? ";
        executeHql(hql, role.getName(), role.getDescription(), role.getId());
    }

    @Transactional
    public void modify(Role role, String[] action) {
        modify(role);
        assignAction(action, role.getId());
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Set<Role> findRoleByUserId(Long userId) {
        String sql = "SELECT r.* FROM role r LEFT OUTER JOIN user_role ur ON r.id = ur.role_id  WHERE ur.user_id = ? ";
        Query query = getSession().createSQLQuery(sql).addEntity(Role.class).setLong(0, userId);
        return new HashSet<Role>((List<Role>)query.list());
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public void listUserForRole(final Long roleId,final List<User> assignedUsers, List<User> unassignedUsers) {
        if (SecurityContext.ROLE_SUPERUSER_ID.equals(roleId)) {
            throw new PermissionException("不能打开超管角色列表");
        }
        List<User> users = userService.listWithoutSuperuser();
        List<Long> parentIds = listParentId(roleId);
        String sql = "SELECT user_id, role_id FROM user_role  WHERE role_id IN :parentIds";
        List<Object[]> list = getSession().createSQLQuery(sql)
                .addScalar("user_id", StandardBasicTypes.LONG)
                .addScalar("role_id", StandardBasicTypes.LONG)
                .setParameterList("parentIds", parentIds).list();
        HashMultimap<Long, Long> userRole = HashMultimap.create();
        for (Object[] objects : list) {
            userRole.put((Long) objects[0], (Long) objects[1]);
        }
        for (User user : users) {
            if(user.getStatus().equals(Integer.valueOf(1))){
                Set<Long> roleIds = userRole.get(user.getId());
                if (roleIds.size() < 1) {
                    //未分配的用户
                    unassignedUsers.add(user);
                } else if (roleIds.contains(roleId)) {
                    //已分配的用户
                    assignedUsers.add(user);
                }
            }
        }
        Collections.sort(assignedUsers, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        Collections.sort(unassignedUsers, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
    }

    /**
     * @param userIds 要分配的用户id
     * @param roleId: 要分配的角色id
     * 1、不能分配超级角色,即roleId不能为1
     * 2、所有用户不能含有该角色的任何父角色，因为含有父角色即含有该角色的权限
     * 3、删掉所有已经存在的子角色
     * 4、写入user_role表
     */
    @Transactional
    public void assignUser(Long[] userIds, Long roleId) {
        checkAssignUser(userIds, roleId);
        // 查看用户是否已经含有 父角色
        List<Long> parentIds = listParentId(roleId);
        parentIds.remove(roleId);
        String countParentRoleSql = "SELECT count(*) FROM user_role WHERE user_id IN :userIds AND role_id IN :parentIds";
        long count = ((Number) getSession().createSQLQuery(countParentRoleSql)
                .setParameterList("userIds",userIds)
                .setParameterList("parentIds",parentIds)
                .uniqueResult()).longValue();
        if (count > 0) {
            throw new UserHasBeenAssignedException("某些用户已在父角色");
        }
        //删掉所有已经存在子角色
        List<Long> childIds = listChildId(roleId);
        String deleteChildRoleSql = "DELETE FROM user_role WHERE user_id IN :userIds AND role_id IN :childIds";
        getSession().createSQLQuery(deleteChildRoleSql)
                .setParameterList("userIds", userIds)
                .setParameterList("childIds", childIds)
                .executeUpdate();

        //写进user_role表
        String saveUserRole = "insert into user_role(user_id,role_id) values(?,?)";
        for (Long userId : userIds) {
            if (userId != null) {
                executeSql(saveUserRole, userId, roleId);
            }
        }
    }

    @Transactional
    public void unassignUser(Long[] userIds, Long roleId) {
        checkAssignUser(userIds, roleId);
        String sql = "DELETE FROM user_role WHERE role_id = :roleId AND user_id IN :userId";
        Query query = getSession().createSQLQuery(sql)
                .setLong("roleId", roleId)
                .setParameterList("userId", userIds);
        query.executeUpdate();
    }

    private void checkAssignUser(Long[] userIds, Long roleId) {
        if (userIds.length < 0) {
            throw new IllegalArgumentException("请先选择用户");
        }
        if (roleId.equals(SecurityContext.ROLE_SUPERUSER_ID)) {
            throw new PermissionException("不能分配超级角色");
        }
    }

    /**
     * 列出可分配给角色的actions(filter必须是autho)。只有该角色父亲拥有的actions才能分配给该角色
     * @param roleId 角色id
     */
    @Transactional
    public Tree<ActionNode> listActionForRole(Long roleId) {
        if (SecurityContext.ROLE_SUPERUSER_ID.equals(roleId)) {
            throw new PermissionException("不能打开超管权限列表，超管含有所有权限");
        }
        Role role = get(roleId);
        String[] split = StringUtils.split(role.getPath(), "/");
        Long parentId = SecurityContext.ROLE_SUPERUSER_ID;
        if (split.length > 1) {
            parentId = Long.parseLong(split[split.length - 2]);
        }
        List<Resource> resources = resourceService.find(CriteriaSpecification.DISTINCT_ROOT_ENTITY, Restrictions.eq("action.filter", FilterType.AUTHORIZATION_FILTER));
        Tree<ActionNode> tree = new Tree<ActionNode>(newActionRoot());
        Set<Action> actions = actionRoleService.findActionsByRole(parentId);
        Set<Action> assignedActions = actionRoleService.findActionsByRole(roleId);
        for (Resource resource : resources) {
            String resourceId = resource.getName() + resource.getId();
            List<Action> resourceActions = resource.getActions();
            boolean insert = false;
            for (Action resourceAction : resourceActions) {
                if (actions.contains(resourceAction)) {
                    boolean assign = assignedActions.contains(resourceAction);
                    tree.insert(new ActionNode(resource.getName() + resource.getId(), "/" + resourceId + "/" + resourceAction.getName() + "/", resourceAction.getName(), resourceAction.getDescription(), assign));
                    insert = true;
                }
            }
            if (insert) {
                tree.insert(new ActionNode("/", "/" + resourceId + "/", resourceId, "资源:" + resource.getDescription(), false));
            }
        }
        return tree;
    }

    /**
     * 列出可分配给角色的actions(filter必须是autho)。只有该角色父亲拥有的actions才能分配给该角色
     * @param roleId 角色id
     */
    @Transactional
    public Tree<ActionNode> listActionOfRole(Long roleId) {
        List<Resource> resources = resourceService.find(CriteriaSpecification.DISTINCT_ROOT_ENTITY, Restrictions.eq("action.filter", FilterType.AUTHORIZATION_FILTER));
        Tree<ActionNode> tree = new Tree<ActionNode>(newActionRoot());
        Set<Action> actions = actionRoleService.findActionsByRole(roleId);
        for (Resource resource : resources) {
            String resourceId = resource.getName() + resource.getId();
            List<Action> resourceActions = resource.getActions();
            boolean insert = false;
            for (Action resourceAction : resourceActions) {
                if (actions.contains(resourceAction)) {
                    tree.insert(new ActionNode(resource.getName() + resource.getId(), "/" + resourceId + "/" + resourceAction.getName() + "/", resourceAction.getName(), resourceAction.getDescription(), false));
                    insert = true;
                }
            }
            if (insert) {
                tree.insert(new ActionNode("/", "/" + resourceId + "/", resourceId, "资源:" + resource.getDescription(), false));
            }
        }
        return tree;
    }

    /**
     * 1、判断actions 都在父角色拥有的权限范围
     * 2、判断actions的filter都是{@link com.privilege.service.resource.template.FilterType#AUTHORIZATION_FILTER}
     * 2、删除该角色的有所权限，插入新的权限
     * 3、删除该角色子角色拥有的不在新的权限范围内权限
     * @param actions 要分配的权限
     * @param roleId 角色id
     */
    @Transactional
    public void assignAction(String[] actions, Long roleId) {
        if (SecurityContext.ROLE_SUPERUSER_ID.equals(roleId)) {
            throw new PermissionException("不能给超管分配权限");
        }
        Role role = get(roleId);
        String[] split = StringUtils.split(role.getPath(), "/");
        Long parentId = SecurityContext.ROLE_SUPERUSER_ID;
        if (split.length > 1) {
            parentId = Long.parseLong(split[split.length - 2]);
        }
        Set<Action> parentActions = actionRoleService.findActionsByRole(parentId);
        if (actions == null){
            String deleteRoleSql = "delete from action_role where role_id = ? ";
            executeSql(deleteRoleSql, roleId);
            return;
        }
        for (String action : actions) {
            if (!parentActions.contains(new Action(action))) {
                logger.error("roleId: {}. 分配权限，包含父角色没有的权限. parent role {}, \n chile role {}", new Object[]{roleId, StringUtils.join(parentActions, ","), StringUtils.join(actions, ",")});
                throw new PermissionException("含有父角色没有的权限");
            }
        }

        //判断filter都是autho
        List<Action> filterActions = actionService.find(Restrictions.in("name", actions), Restrictions.not(Restrictions.eq("filter", FilterType.AUTHORIZATION_FILTER)));
        if (filterActions.size() > 0) {
            throw new PermissionException("actions 包含非autho的权限" + StringUtils.join(actions, ","));
        }
        //分配roleId的权限
        String deleteRoleSql = "delete from action_role where role_id = ? ";
        executeSql(deleteRoleSql, roleId);

        for (String action : actions) {
            ActionRole actionRole = new ActionRole(action, roleId);
            actionRoleService.persist(actionRole);
        }
        //删除子角色的权限
        String sql = "DELETE FROM action_role WHERE ACTION NOT IN :actions AND role_id IN (SELECT r.id FROM ROLE r WHERE r.path LIKE :_path AND r.id != :roleId)";
        getSession().createSQLQuery(sql)
                .setParameterList("actions", actions)
                .setString("_path", role.getPath() + "%")
                .setLong("roleId", roleId)
                .executeUpdate();
    }

    /**
     * @param roleId :
     * @return parents of roleId contains roleId
     */
    private List<Long> listParentId(Long roleId) {
        Role role = get(roleId);
        String[] split = StringUtils.split(role.getPath(), "/");
        List<Long> parentIds = Lists.newArrayList(SecurityContext.ROLE_SUPERUSER_ID);
        for (String id : split) {
            parentIds.add(Long.parseLong(id));
        }
        return parentIds;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    private List<Long> listChildId(Long roleId) {
        Role role = get(roleId);
        String childIdSql = "SELECT r.id AS id FROM role r WHERE r.path LIKE '" + role.getPath() + "%'";
        return getSession().createSQLQuery(childIdSql).addScalar("id", StandardBasicTypes.LONG).list();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!SecurityContext.ROLE_SUPERUSER_ID.equals(id)) {
            Role role = get(id);
            String deleteUserRoleSql = "delete from user_role where role_id in (select id from role where path like ?)";
            executeSql(deleteUserRoleSql, role.getPath() + "%");
            String deleteActionRoleSql = "delete from action_role where role_id in (select id from role where path like ?)";
            executeSql(deleteActionRoleSql, role.getPath() + "%");
            String hql = "delete from Role where path like ? ";
            executeHql(hql, role.getPath() + "%");
        } else {
            throw new PermissionException("超级管理员角色不能删除");
        }
    }


    public static class RoleNode extends TreeNode<RoleNode> {
        private Long id;
        private String name;
        private Long parentId;
        private String description;

        protected RoleNode(Role role) {
            super(role.getPath());
            this.id = role.getId();
            this.name = role.getName();
            this.description = role.getDescription();
            String[] split = StringUtils.split(this.getPath(), "/");
            if (split.length == 0) {
                parentId = null;
            } else if (split.length == 1) {
                parentId = SecurityContext.ROLE_SUPERUSER_ID;
            } else {
                parentId =  Long.parseLong(split[split.length - 2]);
            }
        }

        @Override
        protected int compare(RoleNode node0, RoleNode node1) {
            return 0;
        }

        public Long getParentId() {
            return parentId;
        }

        public Long getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String getName() {
            return name;
        }
    }

    public static RoleNode newRoleRoot() {
        Role role = new Role();
        role.setId(SecurityContext.ROLE_SUPERUSER_ID);
        role.setName(SecurityContext.ROLE_SUPERUSER);
        role.setDescription(SecurityContext.ROLE_SUPERUSER);
        role.setPath(SecurityContext.ROLE_SUPERUSER_PATH);
        return new RoleNode(role);
    }

    public static class ActionNode extends TreeNode<ActionNode> {

        private String name;
        private String parentName;
        private String description;
        private boolean assign = false;

        protected ActionNode(String parentName, String path,  String name, String description, boolean assign) {
            super(path);
            this.name = name;
            this.description = description;
            this.parentName = parentName;
            this.assign = assign;
        }

        private ActionNode() {
            super("/");
        }

        @Override
        protected int compare(ActionNode node0, ActionNode node1) {
            return node0.getName().compareTo(node1.getName());
        }

        public String getDescription() {
            return description;
        }

        public String getName() {
            return name;
        }

        public String getParentName() {
            return parentName;
        }

        public boolean isAssign() {
            return assign;
        }
    }

    private static ActionNode newActionRoot() {
        return new ActionNode();
    }
}