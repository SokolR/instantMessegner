package server.model;

import java.util.List;


public class XmlSet {
    private long idUser;
    private String message;
    private int keyMessage;
    private List<String> list;
    private String property;

    public XmlSet(long id) {
        this.idUser = id;
    }

    public void setIdUser(long id) {
        this.idUser = id;
    }

    public long getIdUser() {
        return idUser;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setKeyDialog(int keyDialog) {
        this.keyMessage = keyDialog;
    }

    public int getKeyDialog() {
        return keyMessage;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
