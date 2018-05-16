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
		
		// Implementierung erforderlich
		// Serialisiere mySerializableClass in Datei
		
	}
	
	public String read() throws IOException, ClassNotFoundException {
		String filename=readFilename();
		
		// Implementierung erforderlich
		// Serialisiere mySerializableClass von Datei
		
		return mySerializableClass.toString();
	}
} 
	