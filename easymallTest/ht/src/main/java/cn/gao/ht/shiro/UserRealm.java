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
 * Created by gaojian on 2016/10/18.
 */
public class UserRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;
    /**
     * 根据用户信息返回用户的角色和权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        //获得用户名
        String username= (String) principal.getPrimaryPrincipal();
//        UsernamePasswordToken token=
        //通过用户名查询用户所拥有的素有角色名称
        List<String> roleNameList=userService.findRoleNamesByUsername(username);
        //通过用户过名查询用户所拥有的所有权限名称
        List<String> ModuleNameList=userService.findModuleNameByUsername(username);

        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(roleNameList);
        authorizationInfo.addStringPermissions(ModuleNameList);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken token1= (UsernamePasswordToken) token;
        String username= token1.getUsername();
        User user=userService.findUserByUserName(username);
        if(user==null){
            throw new UnknownAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(
           user.getUsername(),user.getPassword(),getName()
        );
        return authenticationInfo;
    }
}
