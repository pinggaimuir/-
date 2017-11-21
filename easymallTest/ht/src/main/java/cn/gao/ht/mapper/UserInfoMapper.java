package cn.gao.ht.mapper;

import cn.gao.ht.pojo.UserInfo;

/**
 * Created by tarena on 2016/10/15.
 */
public interface UserInfoMapper {
    /* 插入用户扩展信息*/
    void saveUserInfo(UserInfo userInfo)throws Exception;

    void deleteUserInfo(String[] userIds);

    void updateUserInfo(UserInfo userInfo);
}
