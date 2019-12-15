package server.model;


class  ConfigParameters {
    private int     port;
    private boolean isLog;
    private String  levelLog;

    public boolean isLog() {
        return isLog;
    }

    public void setLog(boolean log) {
        isLog = log;
    }

    public String getLevelLog() {
        return levelLog;
    }

    public void setLevelLog(String levelLog) {
        this.levelLog = levelLog;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
