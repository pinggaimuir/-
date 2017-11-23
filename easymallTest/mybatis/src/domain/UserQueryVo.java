package domain;

import java.util.List;

/**
 * Created by tarena on 2016/9/29.
 */
public class UserQueryVo {
    private UserCustom userCustom;
    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public UserCustom getUserCustom() {
        return userCustom;
    }

    public void setUserCustom(UserCustom userCustom) {
        this.userCustom = userCustom;
    }
}
