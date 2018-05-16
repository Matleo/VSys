package Aufgabe3;

import java.io.Serializable;

public class MySerializableClass implements Serializable{
	private static final long serialVersionUID=1;
	private int id;
	private String string;
	private transient MyNonSerializableClass myNSClass;

	MySerializableClass(MyNonSerializableClass aNSClass) {
		id=1234;
		myNSClass = aNSClass;
	}
	
	public void set(String string) {
		this.string=string;
	}
	
	public String toString() {
		return "id: "+id+"; string: "+string+"; myNSClass: "+myNSClass;
	}
} 
	