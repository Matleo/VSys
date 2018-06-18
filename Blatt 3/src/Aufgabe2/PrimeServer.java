package Aufgabe2;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeServer extends Thread{
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
    }




    public void run() {
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
            int newPort = request.getPort();


            if(this.executor!=null) {
                this.executor.execute(new PrimeServerWorker(value, newPort, timeReceived));
            }else{
                new PrimeServerWorker(value,newPort, timeReceived).start();
            }
        }
    }

}
