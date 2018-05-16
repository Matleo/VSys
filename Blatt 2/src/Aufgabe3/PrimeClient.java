package Aufgabe3;

import rm.requestResponse.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PrimeClient extends Thread {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 30;
    private static final String CLIENT_NAME = PrimeClient.class.getName();

    private static final Object monitor = new Object();

    private static int prime = -1;//-1 falls berechnung nich fertig

    private Component communication;
    String hostname;
    int port;
    long initialValue, count;
    String mode;


    public PrimeClient(String hostname, int port, long initialValue, long count) {
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
                processNumber(i);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {
        int newPort = (int) (Math.random() * 5000) + 2000;

        communication.send(new Message(hostname, newPort, value), port, false);

        Boolean isPrime;

        isPrime = (Boolean) communication.receive(newPort, true, true).getContent();
        synchronized (monitor) {
            prime = isPrime ? 1 : 0;
        }
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String hostname = HOSTNAME;
        int port = PORT;
        long initialValue = INITIAL_VALUE;
        long count = COUNT;

        boolean doExit = false;

        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to " + CLIENT_NAME + "\n");

        while (!doExit) {
            System.out.print("Server hostname [" + hostname + "] > ");
            input = reader.readLine();
            if (!input.equals("")) hostname = input;

            System.out.print("Server port[" + port + "] > ");
            input = reader.readLine();
            if (!input.equals("")) port = Integer.parseInt(input);

            System.out.print("Prime search initial value [" + initialValue + "] > ");
            input = reader.readLine();
            if (!input.equals("")) initialValue = Integer.parseInt(input);

            System.out.print("Prime search count [" + count + "] > ");
            input = reader.readLine();
            if (!input.equals("")) count = Integer.parseInt(input);


            PrimeClient calculator = new PrimeClient(hostname, port, initialValue, count);
            calculator.start();
            int i = 0;
            int last_i = -1;


            while (i < count) {
                if (!(last_i == i)) {
                    System.out.print(initialValue + i + ": ");
                }
                last_i = i;

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
                    synchronized (monitor) {
                        monitor.notifyAll();
                    }

                }
            }

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")) doExit = true;
        }
    }
}
	
