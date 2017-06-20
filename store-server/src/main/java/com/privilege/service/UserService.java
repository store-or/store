package com.privilege.service;

import com.core.dao.TransactionBaseService;
import com.privilege.domain.User;
import com.privilege.exception.PermissionException;
import com.privilege.security.Principal;
import com.privilege.security.SecurityContext;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 */
@Service
public class UserService extends TransactionBaseService<User, Long> {

    public static final int HASH_INTERATIONS = 1024;

    private RandomNumberGenerator rng = new SecureRandomNumberGenerator();

    @Transactional
    public User findUserByLoginName(String loginName) {
        return baseDao.findUnique(Restrictions.eq("loginName", loginName));
    }


    @Transactional
    public void modify(String trueName, String email, Integer status, Long id) {
        if (SecurityContext.SUPERUSER_ID.equals(id)) {
            throw new PermissionException("不能修改超管角色");
        }
        String hql = "update User set trueName = ? ,email = ? ,status = ? where id = ?";
        executeHql(hql, trueName, email, status,id);
    }

    @Transactional
    public void modifyPassword(Long id, String password) {
        Principal principal = SecurityContext.getPrincipal();
        if (id.equals(SecurityContext.SUPERUSER_ID)) {
            throw new PermissionException("尝试修改超管密码，不允许更改");
        }
        if (!principal.getId().equals(SecurityContext.SUPERUSER_ID) && !id.equals(principal.getId())) {
            throw new PermissionException("非超管人员尝试修改别人密码，不允许更改");
        }
        changePassword(id, password);
    }

    @Transactional
    public void changePassword(Long id, String password) {
        ByteSource salt = rng.nextBytes();
        String encryptPassword = encryptPassword(password, salt);
        String hql = "update User set password = ? , salt = ? where id = ?";
        executeHql(hql, encryptPassword, salt.toBase64(), id);
    }


    @Transactional
    public void loginSuccess(Long id){
        User user = this.get(id);
        user.setLastLoginTime(new Date());
        this.saveOrUpdate(user);
    }

    @Override
    @Transactional
    public void persist(User user) {
        initUser(user);
        super.persist(user);
    }

    private void initUser(User user) {
        Date now = new Date();
        user.setCreateTime(now);
        user.setLastLoginTime(now);
        ByteSource salt = rng.nextBytes();
        user.setSalt(salt.toBase64());
        user.setPassword(encryptPassword(user.getPassword(), salt));
    }

    private String encryptPassword(String password, ByteSource salt) {
        return new Sha256Hash(password, salt, HASH_INTERATIONS).toBase64();
    }

    @Transactional
    public void batchDelete(Long[] userId){
        for(Long uid:userId){
            delete(uid);
        }
    }

    @Transactional
    @Override
    public void delete(Long id){
        super.delete(id);
        String sql = "DELETE FROM user_role WHERE user_id = :userId";
        getSession().createSQLQuery(sql).setLong("userId", id).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<Long> getUserIdByRole(Long roleId) {
        String sql = "SELECT ur.user_id AS userId FROM user_role ur WHERE ur.role_id = :roleId";
        return getSession().createSQLQuery(sql).addScalar("userId", StandardBasicTypes.LONG).setLong("roleId", roleId).list();
    }

    @Transactional
    public List<User> listWithoutSuperuser() {
        return find(Restrictions.not(Restrictions.eq("id", SecurityContext.SUPERUSER_ID)));
    }

    public boolean isSuperuser(Long userId) {
        return SecurityContext.SUPERUSER_ID.equals(userId);
    }

}
