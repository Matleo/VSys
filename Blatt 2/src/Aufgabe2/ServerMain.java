package Aufgabe2;



public class ServerMain {
    public static void main(String[] args){
        WebServer server = new WebServer(1234,"src/Aufgabe2/logfiles.log");
        server.listen();
    }

}
