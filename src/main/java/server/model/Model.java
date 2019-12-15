package server.model;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;
import server.controller.Properties;

public class Model implements ModelActions {
    private static final Logger LOG = Logger.getLogger(Model.class);
    private static UserIO USERIO;
    private static int port;
    private boolean statusWork;
    private HashMap<Long, User> list;

    public Model() {
        start();
    }

    protected synchronized static void logMessage(Long id, String message, String forWhom) {
        try (Writer logMess = new BufferedWriter(new FileWriter("logMessage.txt", true))) {
            String number = String.format("%15d", id);
            logMess.append("id: <").append(number).append("> ")
                    .append(new Date(System.currentTimeMillis()).toString())
                    .append(" - send ").append(forWhom).append(" ")
                    .append("\"").append(message).append("\"")
                    .append("\n");
            logMess.flush();
        } catch (FileNotFoundException e) {
            LOG.error("log for messages not found ", e);
        } catch (IOException e) {
            LOG.error("write in log of messages ", e);
        }
    }

    public static void logMessage (XmlSet xmlSet) {
        String forWhom = null;
        if (xmlSet.getProperty().equals(Properties.MessageForAll.name())) {
            forWhom = "to all:";
        }

        if (xmlSet.getProperty().equals(Properties.PrivateMessage.name())) {
            forWhom = "to private, with: ";

            List<String> l = xmlSet.getList();
            for (int i = 0; i < l.size() - 1 ; i++) {
                forWhom += l.get(i) + ", ";
            }
            forWhom += l.get(l.size() - 1) + ".";
        }

        if (forWhom != null) {
            logMessage(xmlSet.getIdUser(), xmlSet.getMessage(), forWhom);
        }
    }

    protected void saveHashMapOfUsers(){
        USERIO.writeList(list);
    }

    public long authorizationUser(String login, String password) {
        for (Map.Entry<Long, User> user: list.entrySet()) {
            if (user.getValue().getLogin().equals(login)) {
                if (user.getValue().getPassword().equals(password)) {
                    return user.getKey();
                }
            }
        }
        return -1;
    }

    public List<String> getBanList() {
        List<String> listBan = new ArrayList<>();
        for (Map.Entry<Long, User> user: list.entrySet()) {
            if (user.getValue().isBan()) {
                listBan.add(user.getValue().getLogin());
            }
        }

        return listBan;
    }

    public synchronized boolean setBan(String login, boolean ban) {
        if (login == null) {
            return false;
        }

        for (Map.Entry<Long, User> user: list.entrySet()) {
            if (user.getValue().getLogin().equals(login)) {
                user.getValue().setBan(ban);
            }
        }
        saveHashMapOfUsers();
        return true;
    }

    public User getUser(long id) {
        for (Map.Entry<Long, User> user: list.entrySet()) {
            if (user.getKey().equals(id)) {
                return  user.getValue().clone();
            }
        }
        return null;
    }

    public synchronized boolean addUser(User user) {
        if (user == null) {
            LOG.debug("user is empty(null)");
            return false;
        }

        for (Map.Entry<Long, User> u : list.entrySet()) {
            if (u.getValue().getLogin().equals(user.getLogin())) {
                return false;
            }
        }

        list.put(user.getId(), user);
        saveHashMapOfUsers();
        LOG.info("user add success: " + user.getLogin());
        LOG.debug("user add: " + user.toString());
        return true;
    }

    public synchronized boolean removeUser(User user) {
        if (user == null) {
            LOG.debug("user is empty(null)");
            return false;
        }

        list.remove(user.getId());
        saveHashMapOfUsers();
        LOG.info("user remove success: " + user.getLogin());
        LOG.debug("user remove: " + user.toString());
        return true;
    }

    public synchronized boolean editUser(User user) {
        if (user == null) {
            LOG.debug("user is empty(null)");
            return false;
        }

        for (Map.Entry<Long, User> u : list.entrySet()) {
            if (u.getValue().getLogin().equals(user.getLogin()) &&
                    u.getValue().getId() != user.getId()) {
                return false;
            }
        }

        LOG.debug("user for change: " + getUser(user.getId()).toString());

        list.put(user.getId(), user);
        saveHashMapOfUsers();
        LOG.info("user edit success: " + user.getLogin());

        LOG.debug("user change on: " + getUser(user.getId()).toString());
        return true;
    }

    public void start() {
        if (statusWork) {
            return;
        } else {
            statusWork = true;
        }

        ConfigParameters conf = XmlMessageServer.loadProperties();
        port = conf.getPort();
        USERIO  = UserIO.getInstance();

        try {
            list = USERIO.readList().getHashList();
        } catch (FileNotFoundException e) {
            list = new HashMap<>();
        }

        addAdmin();
        LOG.info("server start");
    }

    public void stop() {
        saveHashMapOfUsers();
        statusWork = false;
        LOG.info("server stop");
    }

    private void addAdmin() {
        for (Map.Entry<Long, User> user: list.entrySet()) {
            if(user.getValue().isAdmin()) {
                return;
            }
        }

        User admin = new User();
        admin.setIsAdmin(true);
        admin.setLogin("root");
        admin.setPassword("root");

        addUser(admin);
        LOG.debug("create admin " + admin.getLogin() + admin.getPassword());
    }

    public static int getPort() {
        return port;
    }
}
