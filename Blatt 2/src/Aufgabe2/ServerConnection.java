package Aufgabe2;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServerConnection extends Thread {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ServerConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void run() {

        try {
            System.out.println("Connection: received the following");
            String s;
            String filename = "";
            boolean doOnce = true;
            while ((s = in.readLine()) != null) {
                //aus erster header zeile den DateiNamen extrahieren
                if (doOnce) {
                    if (s.substring(5, 9).equals("?url")) {//wenn mit parameter angefragt
                        filename = s.substring(10, s.length() - 9);
                    }else{
                        filename="website.html";
                    }
                    doOnce = false;
                }
                System.out.println("-" + s);
                if (s.isEmpty()) {
                    break;
                }
            }
            writeHTML(filename);
            System.out.println("Connection: Returned the html");
            out.flush();
            out.close();
            in.close();
            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHTML(String filename) throws IOException {
        out.write("HTTP/1.1 200 OK");
        out.write("\r\n\r\n");
        String content = readHTMLFile(filename);
        out.write(content);
    }

    private String readHTMLFile(String filename) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get("src/Aufgabe2/" + filename)));
        } catch (IOException e) {
            try {
                content = new String(Files.readAllBytes(Paths.get("src/Aufgabe2/notAvailable.html")));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return (content);
    }
}
