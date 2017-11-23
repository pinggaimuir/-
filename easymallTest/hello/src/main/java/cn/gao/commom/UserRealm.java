package cn.gao.commom;

import cn.gao.pojo.Permission;
import cn.gao.pojo.Role;
import cn.gao.pojo.User;
import cn.gao.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tarena on 2016/10/13.
 */
public class UserRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;
    /**
     * 提供用户信息返回权限信息
     * @param principalCollection
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取用户名
        String username= (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        //根据用户名查询当前用户拥有的角色
        Set<Role> roles=userService.findRoles(username);
        Set<String> roleNames=new HashSet<String>();
        for(Role role:roles){
            roleNames.add(role.getRoleName());
        }
        //将角色提供给info
        authorizationInfo.setRoles(roleNames);
        //根据用户名查询当前用户权限
        Set<Permission> permissions=userService.findPermissions(username);
        Set<String> permissionNames=new HashSet<>();
        for(Permission permission:permissions){
            permissionNames.add(permission.getPermission());
        }
        //将权限名称提供给info
        authorizationInfo.setStringPermissions(permissionNames);
        return authorizationInfo;
    }

    /**
     * 提供账户信息返回认证信息
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username= (String) authenticationToken.getPrincipal();
        User user=userService.finByUserName(username);
        if(user==null){
            //用户名不存在抛出异常
            throw new UnknownAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(user.getUsername(),
                user.getSex(), ByteSource.Util.bytes(user.getAddress()),getName());
        return authenticationInfo;
    }
}
