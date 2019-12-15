package server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "ListUsers")
@XmlAccessorType(XmlAccessType.FIELD)
class ListUser {

    @XmlElement(name = "user")
    private List<User> list = new ArrayList<>();

    public void setList(HashMap<Long, User> listUser) {
        for (Map.Entry<Long, User> ent: listUser.entrySet()) {
            list.add(ent.getValue());
        }
    }

    public List<User> getList() {
        return list;
    }

    public HashMap<Long, User> getHashList() {
        HashMap<Long, User> hash = new HashMap<>();
        for (User u: list) {
            hash.put(u.getId(), u);
        }
        return hash;
    }
}
