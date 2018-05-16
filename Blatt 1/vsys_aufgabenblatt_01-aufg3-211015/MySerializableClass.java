import java.io.Serializable;

public class MySerializableClass implements Serializable{
	private static final long serialVersionUID=1;
	private int id;
	private String string;
	
	MySerializableClass() {
		id=1234;
	}
	
	public void set(String string) {
		this.string=string;
	}
	
	public String toString() {
		return "id: "+id+"; string: "+string;
	}
} 
	