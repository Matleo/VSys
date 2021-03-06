package Aufgabe1;
import rm.requestResponse.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeServer {
    private final static int PORT = 1234;
    private final static Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());
    private final static String EXECUTORTYPE = "FIXED"; //DYNAMIC | FIXED | <ELSE>
    private final static int fixexThreadPoolN = 2;

    private Component communication;
    private int port = PORT;
    private ExecutorService executor;

    PrimeServer(int port,String executerType) {
        communication = new Component();
        if (port > 0) this.port = port;
        if(executerType.equals("FIXED")){
            this.executor = Executors.newFixedThreadPool(fixexThreadPoolN);
        }else if(executerType.equals("DYNAMIC")){
            this.executor =  Executors.newCachedThreadPool();
        }else{
            this.executor = null;
        }

//    	setLogLevel(Level.FINER);
    }


    void setLogLevel(Level level) {
        for (Handler h : LOGGER.getLogger("").getHandlers()) h.setLevel(level);
        LOGGER.setLevel(level);
        LOGGER.info("Log level set to " + level);
    }


    void listen() {
        LOGGER.info("Listening on port " + port);

        while (true) {
            Message request = null;

            LOGGER.finer("Receiving ...");
            try {
                request = communication.receive(port, true, false);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            long timeReceived = System.currentTimeMillis();
            Long value = (Long) request.getContent();
            LOGGER.fine(value.toString() + " received.");
            int newPort = request.getPort();


            if(this.executor!=null) {
                this.executor.execute(new PrimeServerWorker(value, newPort, timeReceived));
            }else{
                new PrimeServerWorker(value,newPort, timeReceived).start();
            }
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

        new PrimeServer(port,EXECUTORTYPE).listen();
    }
}
