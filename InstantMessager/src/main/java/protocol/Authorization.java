package protocol;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Authorization {
    private String userName;
    private String password;

    public Authorization(){}

    public Authorization(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
