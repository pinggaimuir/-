package mappertest9_23;

import domain.*;

import java.util.List;

/**
 * Created by tarena on 2016/9/23.
 */
public interface UserMapper  {
//    List<User> findUserByNickname(String nickname);
    void addUser(User user);
    void delUser(String nickname);
    void updateUser(User user);
    List<UserCustom> findUserList(UserQueryVo uvo);
    List<UserCustom> findUserResult(UserQueryVo uvo);
    List<UserCustom> findUserByMultiId(UserQueryVo uvo);
    User2 findUserById(int id);
    void addUser2(User2 user);
    void updateUser2(User2 user);
    List<User22> selectUserOrderByAge(String age);
}
