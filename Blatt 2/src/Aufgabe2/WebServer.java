package Aufgabe2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private int port;
    private Writer logger ;
    private ServerSocket serverSocket;

    public WebServer(int port, String filename) {
        this.port = port;

        try {
            logger = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        while (true) {
            try {
                System.out.println("Server: listening on port " + port);
                logger.write("Listening for new client on port " + port+"\n");

                Socket clientSocket = serverSocket.accept();
                System.out.println("Server: Creating connection for "+ clientSocket.getLocalSocketAddress());
                logger.write("Creating connection for "+ clientSocket.getLocalSocketAddress()+"\n");

                ServerConnection connection = new ServerConnection(clientSocket);
                connection.start();
                logger.write("A new Thread is handling the client's request now\n");
                logger.write("--------------------------\n");
                logger.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
