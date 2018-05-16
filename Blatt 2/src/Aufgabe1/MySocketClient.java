package Aufgabe1;
import java.io.*;
import java.net.*;
 
public class MySocketClient {
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	
	MySocketClient(String hostname, int port) 
					throws IOException {
		socket=new Socket();
		System.out.print("Client: connecting '"+hostname+
			"' on "+port+" ... ");
		socket.connect(new InetSocketAddress(hostname,port));
		System.out.println("done.");
		objectInputStream=
			new ObjectInputStream(socket.getInputStream());
		objectOutputStream=
			new ObjectOutputStream(socket.getOutputStream());
	}

	public String sendAndReceive(String message) 
					throws Exception {
		objectOutputStream.writeObject(message);
		String returnValue;
		if(message!=null){
			returnValue = "Client: received '" +(String)objectInputStream.readObject()+"'";
			System.out.println("Client: send "+message);
		}else{
			returnValue = "";
		}
		return returnValue;
	}
	
	public void disconnect() 
					throws IOException {
		try {
			socket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
