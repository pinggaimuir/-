package cn.tedu.domain;

import cn.tedu.exception.MsgException;

import java.io.Serializable;

/**
 * Created by tarena on 2016/8/29.
 */
public class User implements Serializable {
    public User(){}
    private int id;
    private String username;
    private String password;
    private String password2;
    private String nickname;
    private String email;
    private String valistr;

    /**
     * 校验注册信息
     * @throws MsgException 注册信息错误的反馈信息
     */
    public void check() throws MsgException {
        if("".equals(username)||username==null){
            throw new MsgException("用户名不能为空");
        }
        if("".equals(password)||password==null){
            throw new MsgException("密码不能为空");
        }
        if("".equals(password2)||password2==null){
            throw new MsgException("确认密码不能为空");
        }
        if(!password.equals(password2)){
            throw new MsgException("确认密码和密码要一致");
        }
        if("".equals(nickname)||nickname==null){
            throw new MsgException("昵称不能为空");
        }
        if("".equals(email)||email==null){
            throw new MsgException("邮箱不能为空");
        }
        if(!email.matches("^\\w+@\\w+(\\.\\w+)+$")){
            throw new MsgException("邮箱格式不正确");
        }
        if("".equals(valistr)||valistr==null){
            throw new MsgException("验证码不能为空");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getValistr() {
        return valistr;
    }

    public void setValistr(String valistr) {
        this.valistr = valistr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
