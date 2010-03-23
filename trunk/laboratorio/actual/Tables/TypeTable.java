package Tables;

import DataList.*;
import utils.*;

public class TypeTable {

	private List Lista;
	
	public TypeTable(String name, Type Tipo){
		Lista = new List(new ContType(name,Tipo));
	}
		
	public boolean add(String name, Type Tipo){
		return Lista.add(new ContType(name,Tipo));
	}
	
	public DefType search(String name){		
		ContType tmp = (ContType) Lista.search(name);		
		return tmp.getType();
	}
	
}
