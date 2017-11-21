package cn.gao.ht.shiro;

import cn.gao.ht.pojo.User;
import cn.gao.ht.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tarena on 2016/10/20.
 */
public class TestRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username= (String) principalCollection.getPrimaryPrincipal();
        List<String> roleNamesList=userService.findRoleNamesByUsername(username);
        List<String> moduleNameLiset=userService.findRoleNamesByUsername(username);

        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();

        authorizationInfo.addRoles(roleNamesList);
        authorizationInfo.addStringPermissions(moduleNameLiset);

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        String username=token.getUsername();
        String password=String.valueOf(token.getPassword());
        User user=userService.findUserByUserName(username);
        SimpleAuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(username,user.getPassword(),getName());
        return authenticationInfo;
    }
}
