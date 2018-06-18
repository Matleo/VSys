package Aufgabe2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ServerAdmin {
    private List<ServerConfig> servers;
    private int searchstart = 0;

    public ServerAdmin(String configFile){
        servers = new ArrayList<>();
        Path path = Paths.get(configFile);
        try {
            List<String> lines = Files.readAllLines(path);
            for(String line : lines){
                String[] arguments = line.split(" ");
                servers.add(new ServerConfig(arguments[0],Integer.parseInt(arguments[1]),Integer.parseInt(arguments[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerConfig bind(){
        //search until an available server is found
        while(true) {
            for (int i = searchstart; i< searchstart + servers.size();i++) {
                int index = i % servers.size();
                ServerConfig server = servers.get(index);
                if (server.isAvailable) {
                    searchstart = (i+1) % servers.size();
                    server.bind();
                    return server;
                }
            }
        }
    }

    public void release(ServerConfig server){
        server.release();
    }
}
