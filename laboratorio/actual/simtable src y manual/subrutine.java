package simtable;

public class subrutine extends data{

	private String Name;
	private var[] InVariables;
	
	subrutine(String name, var[] invariables){
		Name = name;
		InVariables = invariables;
	}
	
	String Name(){
		return Name;
	}
	
	var[] InVariables(){
		return InVariables;
	}

}
