package cn.gao.service;


import cn.gao.pojo.Permission;
import cn.gao.pojo.Role;
import cn.gao.pojo.User;

import java.util.Set;

/**
 * Created by tarena on 2016/10/13.
 */
public interface UserService {

    Set<Role> findRoles(String username);

    Set<Permission> findPermissions(String username);

    User finByUserName(String username);
}
