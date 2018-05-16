package Aufgabe3;

import java.io.*;

public class MySerializer {
	private MySerializableClass mySerializableClass;
	
	MySerializer(MySerializableClass serializableClass) {
		mySerializableClass=serializableClass;
	}
	
	private String readFilename() throws IOException {
		String filename;
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in )); 
		
		System.out.print("filename> ");
		filename=reader.readLine();
		
		return filename;
	}
	
	public void write(String text) throws IOException {
		mySerializableClass.set(text);
		String filename=readFilename();

		FileOutputStream oStream = new FileOutputStream (filename);
		ObjectOutputStream myStream = new ObjectOutputStream (oStream);

		myStream.writeObject(mySerializableClass);
	}
	
	public String read() throws IOException, ClassNotFoundException {
		String filename=readFilename();
		
		FileInputStream iStream = new FileInputStream(filename);
		ObjectInputStream myStream = new ObjectInputStream(iStream);
		MySerializableClass mySerializableClass = (MySerializableClass) myStream.readObject();
		
		return mySerializableClass.toString();
	}
} 
	