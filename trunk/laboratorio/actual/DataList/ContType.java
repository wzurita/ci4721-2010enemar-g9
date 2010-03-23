package DataList;

import utils.DefType;
import utils.Type;

public class ContType extends Container{
	
	private DefType Tipo;
	
	public ContType(String Nombre, Type tipo){
		this.setName(Nombre);
		Tipo = new DefType(Nombre,tipo);
	}
	
	public DefType getType(){
		return Tipo;
	}

}
