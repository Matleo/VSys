package Aufgabe2;

public class ServerConfig {
    private String hostname;
    private int sendPort;
    private int receivePort;
    public boolean isAvailable;

    public ServerConfig(String hostname, int receivePort, int sendPort) {
        this.hostname = hostname;
        this.sendPort = sendPort;
        this.receivePort = receivePort;
        this.isAvailable = true;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getSendPort() {
        return sendPort;
    }

    public void setSendPort(int sendPort) {
        this.sendPort = sendPort;
    }

    public int getReceivePort() {
        return receivePort;
    }

    public void setReceivePort(int receivePort) {
        this.receivePort = receivePort;
    }
    public void release(){this.isAvailable = true;}

    public void bind(){this.isAvailable = false;}

}
