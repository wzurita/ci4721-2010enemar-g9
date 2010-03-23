package Tables;

import DataList.*;
import utils.DefVar;
import utils.Type;

public class SimTable {
	
	private List Lista;
	
	SimTable(String name, Type Tipo){
		Lista = new List(new ContSim(new DefVar(name,Tipo)));
	}
		
	boolean add(String name, Type Tipo){
		return Lista.add(new ContSim(new DefVar(name,Tipo)));
	}
	
	DefVar search(String name){		
		ContSim tmp = (ContSim) Lista.search(name);		
		return tmp.getVar();
	}
	
}
