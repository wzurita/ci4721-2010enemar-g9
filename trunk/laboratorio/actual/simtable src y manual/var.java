package simtable;

public class var extends data{

	private String Name;
	private absTree Dimension;
	
	var(String name, absTree dimension){
		Name = name;
		Dimension = dimension;
	}
	
	String Name(){
		return Name;
	}

	absTree Dimension(){
		return Dimension;
	}
		
}
