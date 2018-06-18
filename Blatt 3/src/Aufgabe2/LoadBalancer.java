package Aufgabe2;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;

public class LoadBalancer {
    private static final String CONFIG_FILE ="./src/Aufgabe2/config/loadBalancerConfig.txt";
    private final static int PORT = 1234;

    private ServerAdmin admin;
    private Component communication;

    public LoadBalancer() {
        admin = new ServerAdmin(CONFIG_FILE);
        communication = new Component();

        ServerConfig server1 = admin.bind();
        ServerConfig server2 = admin.bind();
        ServerConfig server3 = admin.bind();
        new PrimeServer(server1.getReceivePort(),"DYNAMIC").start();
        new PrimeServer(server2.getReceivePort(),"DYNAMIC").start();
        new PrimeServer(server3.getReceivePort(),"DYNAMIC").start();
        admin.release(server1);
        admin.release(server2);
        admin.release(server3);
    }

    private void listen() {
        while (true) {
            Message request = null;
            try {
                request = communication.receive(PORT, true, false);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

            ServerConfig server = admin.bind();
            dispatchRequest(request,server);
            admin.release(server);

        }
    }

    public static void main(String[] args){
        new LoadBalancer().listen();
    }

    private void dispatchRequest(Message request, ServerConfig server){
        try {
            System.out.println("Dispatching request to: "+server.getReceivePort());
            communication.send(request, server.getReceivePort(), false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
