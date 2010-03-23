package Tables;


import DataList.*;
import utils.*;

public class FunTable {
	
	private List Lista;
	
	public FunTable(String n, Type t, DefVar[] v, Bloque b){
		Lista = new List(new ContFun(n,t,v,b));
	}
		
	public boolean add(String n, Type t, DefVar[] v, Bloque b){
		return Lista.add(new ContFun(n,t,v,b));
	}	
	
	public DefFun search(String name){
		ContFun tmp = (ContFun) Lista.search(name);		
		return tmp.getFun();
	}
	
}