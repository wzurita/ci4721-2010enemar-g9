package simtable;

public class subrutine extends data{

	private String Name;
	private absTree[] InVariables;
	
	subrutine(String name, absTree[] invariables){
		Name = name;
		InVariables = invariables;
	}
	
	String Name(){
		return Name;
	}
	
	absTree[] InVariables(){
		return InVariables;
	}

}
