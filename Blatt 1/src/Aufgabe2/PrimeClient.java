package Aufgabe2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rm.requestResponse.*;

public class PrimeClient extends Thread {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 10;
    private static final String CLIENT_NAME = PrimeClient.class.getName();
    private static final String REQUEST_MODE = "CONCURRENT";

    private static final Object monitor = new Object();

    private static int prime = -1;//-1 falls berechnung nich fertig

    private Component communication;
    String hostname;
    int port;
    long initialValue, count;
    String mode;


    public PrimeClient(String hostname, int port, long initialValue, long count, String mode) {
        this.hostname = hostname;
        this.port = port;
        this.initialValue = initialValue;
        this.count = count;
        this.mode = mode;
    }

    public void run() {
        communication = new Component();
        for (long i = initialValue; i < initialValue + count; i++) {
            try {
                processNumber(i, this.mode);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (this.mode == "CONCURREN") {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void processNumber(long value, String mode) throws IOException, ClassNotFoundException {

        communication.send(new Message(hostname, port, new Long(value)), false);

        Boolean isPrime;

        if (mode.equals("BLOCKING")) {
            isPrime = (Boolean) communication.receive(port, true, true).getContent();
            System.out.println(value + ": " + "->" + (isPrime ? "prime" : "not prime"));
        } else if (mode.equals("NON-BLOCKING")) {
            System.out.print(value + ": ");
            while (true) {
                try {
                    isPrime = (Boolean) communication.receive(port, false, true).getContent();
                    break;
                } catch (NullPointerException e) {
                    System.out.print(".");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            System.out.println("->" + (isPrime ? "prime" : "not prime"));
        } else if (mode.equals("CONCURRENT")) {
            isPrime = (Boolean) communication.receive(port, true, true).getContent();
            synchronized (monitor) {
                prime = isPrime ? 1 : 0;
            }
        }
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String hostname = HOSTNAME;
        int port = PORT;
        long initialValue = INITIAL_VALUE;
        long count = COUNT;
        String mode = REQUEST_MODE;

        boolean doExit = false;

        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to " + CLIENT_NAME + "\n");

        while (!doExit) {
            System.out.print("Server hostname [" + hostname + "] > ");
            input = reader.readLine();
            if (!input.equals("")) hostname = input;

            System.out.print("Server port [" + port + "] > ");
            input = reader.readLine();
            if (!input.equals("")) port = Integer.parseInt(input);

            System.out.print("Request mode [" + mode + "] > ");
            input = reader.readLine();
            if (!input.equals("")) mode = input;

            System.out.print("Prime search initial value [" + initialValue + "] > ");
            input = reader.readLine();
            if (!input.equals("")) initialValue = Integer.parseInt(input);

            System.out.print("Prime search count [" + count + "] > ");
            input = reader.readLine();
            if (!input.equals("")) count = Integer.parseInt(input);

            if (!mode.equals("CONCURRENT")) {
                new PrimeClient(hostname, port, initialValue, count, mode).run();
            } else {
                PrimeClient calculator = new PrimeClient(hostname, port, initialValue, count, mode);
                calculator.start();
                int i = 0;
                int last_i = -1;


                while (i < count) {
                    if (!(last_i == i)) {
                        System.out.print(initialValue + i + ": ");
                    }
                    last_i = i;
                    synchronized (monitor) {
                        if (prime == -1) {
                            System.out.print(".");
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            System.out.println("->" + (prime == 1 ? "prime" : "not prime"));
                            prime = -1;
                            i++;
                            monitor.notify();
                        }
                    }
                }
            }

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")) doExit = true;
        }
    }
}
	
