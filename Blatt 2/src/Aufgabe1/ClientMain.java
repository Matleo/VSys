package Aufgabe1;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientMain {
	private static final int port=1234;
	private static final String hostname="localhost";
	private static MySocketClient client;
	private static BufferedReader reader;



	public static void main(String args[]) {
		try {
			client=new MySocketClient(hostname,port); //ist das so richtig, für jede anfrage einen neuen client?
			System.out.print("Client: Enter name> ");
			reader=new BufferedReader(new InputStreamReader(System.in ));
			String clientName=reader.readLine();
			int i = 0;
			while(true){
				i++;
				System.out.println(client.sendAndReceive(clientName+"-"+i));
				Thread.sleep((long) (Math.random()*1000));
				if(reader.ready()){
					client.sendAndReceive(null);
					break;
				}
			}
			client.disconnect();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
