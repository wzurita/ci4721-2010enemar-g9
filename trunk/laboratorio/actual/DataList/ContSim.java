package DataList;

import utils.DefVar;

public class ContSim extends Container{

		private DefVar Var;
		
		public ContSim(DefVar var){
			this.setName(var.getName());
			Var = var; 
		}
		
		public DefVar getVar(){
			return Var;
		}
}
 