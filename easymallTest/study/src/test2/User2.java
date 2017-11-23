package test2;

/**
 * Created by tarena on 2016/9/5.
 */
public class User2 {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    public User2(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public User2(String email, int id, String nickname, String password, String username) {
        this.email = email;
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.username = username;
    }

    @Override
    public String toString() {
        return "User2{" +
                "email='" + email + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
