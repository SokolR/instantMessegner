package server.controller;


import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import server.model.*;

import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ControllerServer extends Observable implements Server{
    private int PORT;
    private ServerSocket socket;
    private List<ServerThread> activeUsers;
    private ModelActions model;
    private static final Logger logger = Logger.getLogger(ControllerServer.class);
    private volatile boolean finish = false;

    private static final String USER_IS_ALREADY = "This user has already been created.";
    private static final String ONLINE_USER = "The user is online.";
    private static final String EXIST_USER = "User does not exist!";
    private static final String WRONG_COMMAND = "The client is not authenticated. No token \"authentication\"  word. Please try to connect again.";
    private static final String UNBAN =  "You was unban";
    private static final String DELETE = "Admin deleted you.";
    private static final String STOP = "Server is stopped. Please, try to reconnect.";

    public synchronized void addActiveUser(ServerThread activeUser){
        activeUsers.add(activeUser);
        this.setChanged();
        notifyObservers();
    }
    public synchronized void updateUsers(){
        this.setChanged();
        notifyObservers();
    }

    public synchronized void removeActiveUser(ServerThread activeUser){
        activeUsers.remove(activeUser);
        this.setChanged();
        notifyObservers();
    }

    public ControllerServer(ModelActions model){
        this.PORT = Model.getPort();
        this.model = model;
    }

    public synchronized void registration (ServerThread client, String login, String password)throws TransformerException{
        User createUser = new User();
        createUser.setLogin(login);
        createUser.setPassword(password);
        createUser.setBan(false);
        createUser.setIsAdmin(false);
        if(model.addUser(createUser)) {
            client.getXmlUser().setIdUser(createUser.getId());
            client.setUser(createUser);
            client.setAuthentication(true);
            client.getXmlUser().setMessage(Properties.Successfully.name());
            client.sendMessage(Properties.Registration.name());
            this.displayInfoLog("New user: " + client.getUser().getLogin() + "  is welcome.");
            logger.debug("Create new user " + client.getUser().getLogin());
        }
        else{
            throw new IllegalArgumentException(USER_IS_ALREADY);
        }
    }

  public synchronized void authorization(ServerThread client)throws TransformerException{
      boolean online = false;
      String preference = client.getXmlUser().getProperty();
      Properties command = Properties.fromString(preference);
      List<String> data = client.getXmlUser().getList();
      long idUser = model.authorizationUser(data.get(0), data.get(1));

      switch (command){
          case Registration:
              try {
                  if (idUser == -1) {
                      registration(client, data.get(0), data.get(1));
                  }
                  else {
                      throw new IllegalArgumentException(USER_IS_ALREADY);
                  }
              }
              catch (IllegalArgumentException e){
                  client.getXmlUser().setMessage(Properties.IncorrectValue.name() + " name of user. "+USER_IS_ALREADY);
                  client.sendMessage(Properties.Registration.name());
              }
              break;
          case Authentication:
              if (idUser != -1) {
                  client.getXmlUser().setIdUser(idUser);
                  client.setUser(model.getUser(idUser));

                  for (int i = 0; i < activeUsers.size(); i++) {
                      if (activeUsers.get(i).getUser().getLogin().compareToIgnoreCase(client.getUser().getLogin()) == 0) {
                          online = true;
                          client.getXmlUser().setMessage(ONLINE_USER);
                          client.sendMessage(Properties.Authentication.name());
                          break;
                      }
                  }
                  if (!online) {
                      client.setAuthentication(true);
                      if (client.getUser().isBan()) {
                          client.getXmlUser().setMessage(Properties.Ban.name());
                      }
                      else{
                          client.getXmlUser().setMessage(Properties.Successfully.name());
                      }
                      if (client.getUser().isAdmin()) {
                          client.sendMessage(Properties.Admin.name());
                      } else {
                          client.sendMessage(Properties.Authentication.name());
                      }

                      logger.debug("Authentications user is " + client.getUser().getLogin());
                      if (client.getUser().isAdmin()) {
                          this.displayInfoLog("Admin: " + client.getUser().getLogin() + " is welcome.");
                      }
                      else {
                          this.displayInfoLog(client.getUser().getLogin() + " is welcome.");
                      }
                  }
              }
              else{
                  client.getXmlUser().setMessage(EXIST_USER);
                  client.sendMessage(Properties.Authentication.name());
              }
              break;
          default:
              client.getXmlUser().setMessage(WRONG_COMMAND);
              client.sendMessage(Properties.Authentication.name());
              break;
      }
      if(client.isAuthentication()){
          addActiveUser(client);
          client.getXmlUser().setMessage(getDate() + " System message: user < " + client.getUser().getLogin() + " > is welcome.");
          try {
              readCommand(client, Properties.MessageForAll);
          }
          catch (TransformerException e){
            logger.error(e);
          }

      }

  }

    @Override
    public void displayInfoLog(String message){
        System.out.println(message);
        logger.info(message);
    }

    @Override
    public void run() throws IOException, SAXException{
      activeUsers = new ArrayList<>();
      this.displayInfoLog("Building to port " + this.PORT + ", please wait  ...");
      socket = new ServerSocket(PORT);
      this.displayInfoLog("Server started.");
        this.displayInfoLog("Waiting a client... ");
        while (true) {
          if(this.finish){
              return;
          }
          try {
              Socket client = socket.accept();
              logger.debug("Connection from " + client.getInetAddress().getHostName());
              this.addObserver(new ServerThread(client));
          }
          catch (SocketException e){
              continue;
          }
      }
    }
    private String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String date = ".:Time: " +formatter.format(new Date())+":.";
        return date;

    }

    public void stop() throws IOException{
        if (activeUsers.size() != 0) {
            activeUsers.get(0).reportOfStop();
        }
        this.finish=true;
        for(int i=0;i<activeUsers.size();i++){
            try {
                readCommand(activeUsers.get(i), Properties.Close);
            }
            catch (TransformerException e){
                logger.error(e);
            }
        }
        if (socket != null) {
                socket.close();
            }
        this.displayInfoLog("\n" + "Server is stopped." + "\n");
        model.stop();
    }

  public List<String> getUserListString(){
        ArrayList<String> userList = new ArrayList<>();
      for(int i=0;i<activeUsers.size();i++){
          userList.add(activeUsers.get(i).getUser().getLogin());
      }
        return userList;
    }

    public synchronized void readCommand(ServerThread client, Properties properties) throws TransformerException{

        switch (properties){

            case MessageForAll:
                String messageToChat = client.getXmlUser().getMessage();
                for(int i=0;i<activeUsers.size();i++){
                    activeUsers.get(i).getXmlUser().setMessage(messageToChat);
                    activeUsers.get(i).sendMessage(Properties.MessageForAll.name());
                }
                Model.logMessage(client.getXmlUser());
                break;

            case PrivateMessage:
                String messageToPrivateChat = client.getXmlUser().getMessage();
                List<String> userList = client.getXmlUser().getList();
                if(userList!=null) {
                    int keyDialog=client.getXmlUser().getKeyDialog();
                    List<String> privateList = new ArrayList<>();
                    privateList.add(client.getUser().getLogin());
                    privateList.addAll(userList);
                    client.sendMessage(Properties.PrivateMessage.name());
                    for(int j=0;j<activeUsers.size();j++){
                        for (int i = 0; i < userList.size(); i++) {
                            if (activeUsers.get(j).getUser().getLogin().compareToIgnoreCase(userList.get(i)) == 0) {
                                activeUsers.get(j).getXmlUser().setMessage(messageToPrivateChat);
                                activeUsers.get(j).getXmlUser().setList(privateList);
                                activeUsers.get(j).getXmlUser().setKeyDialog(keyDialog);
                                activeUsers.get(j).sendMessage(Properties.PrivateMessage.name());
                            }
                        }
                    }
                    Model.logMessage(client.getXmlUser());
                }
                else{
                    client.getXmlUser().setMessage(Properties.IncorrectValue.name());
                    client.sendMessage(Properties.PrivateMessage.name());
                }
                break;

            case Ban:
                if(client.getUser().isAdmin()) {
                    String infoFoBan = client.getXmlUser().getList().get(0);
                    if (model.setBan(infoFoBan, true)) {
                        client.getXmlUser().setMessage(Properties.Successfully.name());
                        client.sendMessage(Properties.Ban.name());
                    }
                    for(int i=0;i<activeUsers.size();i++){
                        if (activeUsers.get(i).getUser().getLogin().compareToIgnoreCase(infoFoBan) == 0) {
                                activeUsers.get(i).getUser().setBan(true);
                                activeUsers.get(i).getXmlUser().setMessage(Properties.Ban.name());
                                activeUsers.get(i).sendMessage(Properties.Ban.name());
                                break;
                        }
                    }
                    this.displayInfoLog("Admin "+ Properties.Ban.name()+" user:  " + infoFoBan);
                    client.getXmlUser().setMessage(getDate()+" System message: user < " + infoFoBan + " > is baned.");
                    readCommand(client, Properties.MessageForAll);

                }
                break;

            case UnBan:
                if(client.getUser().isAdmin()) {
                    String infoFoBan2 = client.getXmlUser().getList().get(0);
                    List<String> banUsers = model.getBanList();
                    for(int i=0;i<banUsers.size();i++){
                        if (banUsers.get(i).compareToIgnoreCase(infoFoBan2) == 0) {
                             if(model.setBan(infoFoBan2,false)) {
                                client.getXmlUser().setMessage(Properties.Successfully.name());
                                break;
                            }
                        }
                    }
                    for(int i=0;i<activeUsers.size();i++){
                        if (activeUsers.get(i).getUser().getLogin().compareToIgnoreCase(infoFoBan2) == 0) {
                                activeUsers.get(i).getUser().setBan(false);
                                activeUsers.get(i).getXmlUser().setMessage(UNBAN);
                                activeUsers.get(i).sendMessage(Properties.UnBan.name());
                                break;
                        }
                    }
                    this.displayInfoLog("Admin "+ Properties.UnBan.name()+" user:  " + infoFoBan2);
                    client.getXmlUser().setMessage(getDate()+" System message: user < " + infoFoBan2 + " > is unbaned.");
                    readCommand(client, Properties.MessageForAll);
                }

                client.sendMessage(Properties.UnBan.name());
                break;

            case BanUsers:
                List<String> banUsers = model.getBanList();
                client.getXmlUser().setList(banUsers);
                client.getXmlUser().setMessage(Properties.BanUsers.name());
                client.sendMessage(Properties.BanUsers.name());
                break;

            case Edit:
                    List<String> newUser = client.getXmlUser().getList();
                    try {
                        client.getUser().setLogin(newUser.get(0));
                        client.getUser().setPassword(newUser.get(1));
                        if(!model.editUser(client.getUser())){
                            throw  new IllegalArgumentException("Not unique name!");
                        }
                        client.getXmlUser().setMessage(Properties.Successfully.name());
                        client.sendMessage(Properties.Edit.name());
                        this.displayInfoLog("Edit of user: " + client.getUser().getLogin() + " is successful. ");
                        logger.debug(Properties.Edit.name() + " user: " + client.getUser().getLogin());
                        this.setChanged();
                        this.notifyObservers();
                        break;
                    }
                    catch (IllegalArgumentException e ){
                        client.getXmlUser().setMessage(Properties.IncorrectValue.name());
                        client.sendMessage(Properties.Edit.name());
                        break;
                    }

            case Remove:
                if(client.getUser().isAdmin()){
                    String removeUser = client.getXmlUser().getList().get(0);
                    for(int i=0;i<activeUsers.size();i++){
                        if(activeUsers.get(i).getUser().getLogin().compareToIgnoreCase(removeUser)==0){
                            model.removeUser(activeUsers.get(i).getUser());
                            activeUsers.get(i).getXmlUser().setMessage(DELETE);
                            activeUsers.get(i).sendMessage(Properties.Remove.name());
                            deleteObserver(activeUsers.get(i));
                            client.getXmlUser().setMessage(Properties.Admin.name());
                            client.sendMessage(Properties.Remove.name());
                            activeUsers.get(i).close();
                            removeActiveUser(activeUsers.get(i));
                            break;
                        }
                    }
                    this.displayInfoLog("Admin remove user: " + removeUser);
                    logger.debug(Properties.Remove.name()+ " removeUser");
                }
                else{

                    model.removeUser(client.getUser());
                    deleteObserver(client);
                    removeActiveUser(client);
                    client.getXmlUser().setMessage(Properties.Successfully.name());
                    client.sendMessage(Properties.Remove.name());
                    client.close();

                    this.displayInfoLog("Server remove user:  " + client.getUser().getLogin());
                    logger.debug("Remove " + client.getUser().getLogin());
                }
                this.setChanged();
                notifyObservers();
                break;

            case Close:
                deleteObserver(client);
                removeActiveUser(client);
                client.close();
                this.displayInfoLog("User: " + client.getUser().getLogin() + " close.");
                client.getXmlUser().setMessage(getDate()+" System message: user < " + client.getUser().getLogin() + " > is closed.");
                readCommand(client, Properties.MessageForAll);
                break;

            case Stop:
                for(int i=0;i<activeUsers.size();i++){
                    activeUsers.get(i).getXmlUser().setMessage(STOP);
                    activeUsers.get(i).sendMessage(Properties.Stop.name());
                }
                break;

            default:
                client.sendMessage(Properties.IncorrectValue.name());
                break;
        }
    }

    public class ServerThread extends Thread implements Observer{
        private User user;
        private final Socket socket;
        private InputStream fromClient;
        private OutputStream toClient;
        private XmlSet xmlUser;
        private boolean authentication;

        public ServerThread(Socket socket) throws  IOException{
            this.socket = socket;
            this.authentication = false;
            fromClient = socket.getInputStream();
            toClient=socket.getOutputStream();
            this.setDaemon(true);
            start();
        }

        @Override
        public void update(Observable o, Object arg) {
            try {
                this.getXmlUser().setList(((ControllerServer) o).getUserListString());
                sendMessage(Properties.ActiveUsers.name());
            }
            catch (TransformerException | NullPointerException e) {
                updateUsers();
                logger.error(e);
            }
        }

        public XmlSet getXmlUser() {
            return xmlUser;
        }

        public void setXmlUser(XmlSet xmlUser) {
            this.xmlUser = xmlUser;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public boolean isAuthentication() {
            return authentication;
        }

        public void setAuthentication(boolean authentication) {
            this.authentication = authentication;
        }

        public synchronized void reportOfStop(){
            this.getXmlUser().setMessage(getDate()+" System message: "+STOP);
            try{
                readCommand(this, Properties.MessageForAll);
                readCommand(this, Properties.Stop);
            }
            catch (TransformerException e1){
                logger.error(e1);
            }
            this.close();
        }

        public void getMessage() throws SAXException{
                try {
                    BufferedReader is = new BufferedReader(new InputStreamReader(fromClient));
                    StringBuffer ans = new StringBuffer();
                    while (true) {
                        String input = is.readLine();
                        ans.append(input);
                        if (input == null || input.equals("</XmlMessage>")) {
                            break;
                        }
                    }
                    this.setXmlUser(XmlMessage.readXmlFromStream(new ByteArrayInputStream(ans.toString().getBytes())));
                }
                catch (IOException e) {
                    if(!this.socket.isClosed()) {
                        try {
                            readCommand(this, Properties.Close);
                        } catch (TransformerException e1) {
                            logger.error(e1);
                        }
                    }

                }

        }

        @Override
        public void run() {
            try {
                boolean isEditRepeat=false;
                while (true) {
                   if (finish) {
                       return;
                   }
                    this.getMessage();
                    if (this.socket.isClosed()) {
                        return;
                    }
                    if(isEditRepeat){
                        isEditRepeat=false;
                        continue;
                        }
                    if (getXmlUser() != null) {
                        if (!this.isAuthentication()) {
                            authorization(this);
                        }
                        else {
                            String preference = getXmlUser().getProperty();
                            Properties command = Properties.fromString(preference);
                           if(!finish) {
                                readCommand(this, command);
                                if (preference.equals(Properties.Edit.name())) {
                                    isEditRepeat = true;
                                }
                            }
                        }
                    }
                }
            }
            catch (SAXException | TransformerException e) {
                    logger.error(e);
            }
        }

        public void sendMessage(String message) throws TransformerException {
                getXmlUser().setProperty(message);
                XmlMessage.writeXMLinStream(getXmlUser(), toClient);
        }

        public void close() {
                try {
                   if (fromClient != null) {
                        fromClient.close();
                    }
                    if (toClient != null) {
                        toClient.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                }
                catch (IOException e){
                    logger.error(e);
                }
        }


    }
}

