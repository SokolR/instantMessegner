package server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Cloneable {
    private long    id;
    private String  password;
    private String  login;
    private boolean ban;
    private boolean isAdmin;

    public User() {
        this.id = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public User clone() {
        User result;
        try {
            result = (User) super.clone();
            result.setLogin(this.login);
            result.setBan(this.ban);
            result.setPassword(this.password);
        } catch (CloneNotSupportedException e) {
            return null;
        }
        return result;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", password='" + password +
                "', login='" + login + "', ban='" + ban +
                "', isAdmin='" + isAdmin + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id && (login != null ? login.equals(user.login) : user.login == null);

    }

    @Override
    public int hashCode() {
        int result = (int) id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }

}
