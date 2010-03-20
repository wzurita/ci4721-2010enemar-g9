package DataList;

public class Container {

	private String Name;
	private Container Next;
	
	void setName(String Nombre){
		Name = Nombre;
	}
	
	String getName(){
		return Name;
	} 
	
	void setNext(Container data){
		Next = data;
	}
	
	Container getNext(){
		return Next;
	}
}
