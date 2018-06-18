package Aufgabe3;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class PrimeServer implements RMIPrimeServer{
    private final static int PORT = 1234;
    private final static Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());
    private final static String EXECUTORTYPE = "DYNAMIC"; //DYNAMIC | FIXED | <ELSE>

    private int port = PORT;
    private ExecutorService executor;

    PrimeServer(int port,String executerType) {
        if (port > 0) this.port = port;

        if(executerType.equals("FIXED")){
            this.executor = Executors.newFixedThreadPool(3);
        }else if(executerType.equals("DYNAMIC")){
            this.executor =  Executors.newCachedThreadPool();
        }else{
            this.executor = null;
        }
    }

    public void calculatePrime(long candidate , String returnTo) throws RemoteException{
        if(this.executor!=null) {
            this.executor.execute(new PrimeServerWorker(candidate , returnTo, this.port));
        }else{
            new PrimeServerWorker(candidate , returnTo, this.port).start();
        }
    }



    void initiate() {
        try {
            RMIPrimeServer serverStub = (RMIPrimeServer) UnicastRemoteObject.exportObject(this,0);

            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("PrimeServer",serverStub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 0;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-port":
                    try {
                        port = Integer.parseInt(args[++i]);
                    } catch (NumberFormatException e) {
                        LOGGER.severe("port must be an integer, not " + args[i]);
                        System.exit(1);
                    }
                    break;
                default:
                    LOGGER.warning("Wrong parameter passed ... '" + args[i] + "'");
            }
        }

        new PrimeServer(port,EXECUTORTYPE).initiate();
    }
}
