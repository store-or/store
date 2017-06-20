package com.privilege.security;

import com.core.util.ValidatorUtil;
import com.privilege.domain.ActionRole;
import com.privilege.domain.User;
import com.privilege.service.ActionRoleService;
import com.privilege.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 */
public class ShiroDbRealm  extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private ActionRoleService actionRoleService;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        final Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                List<ActionRole> actionRoles = actionRoleService.findActionByUserId(principal.getId());
                for (ActionRole actionRole : actionRoles) {
                    principal.addAction(actionRole.getAction());
                }
                principal.setSuperuser(userService.isSuperuser(principal.getId()));
                principal.setAuthentication(true);
            }
        });

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.findUserByLoginName(token.getUsername());
        if (user != null) {
            if (user.getStatus() == User.USER_UNABLE) {
                throw new DisabledAccountException();
            }

            return new SimpleAuthenticationInfo(new Principal(user.getId(), user.getLoginName(), user.getTrueName(), user.getEmail(), !ValidatorUtil.checkPasswordComplexity(String.valueOf(token.getPassword()))),
                    user.getPassword(), ByteSource.Util.bytes(Base64.decode(user.getSalt())), getName());
        } else {
            return null;
        }
    }

    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME);
        matcher.setHashIterations(UserService.HASH_INTERATIONS);
        matcher.setStoredCredentialsHexEncoded(false);
        super.setCredentialsMatcher(matcher);
    }
}
