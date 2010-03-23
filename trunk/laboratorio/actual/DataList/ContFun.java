package DataList;

import utils.DefFun;

public class ContFun extends Container{
		private DefFun Function;
		
		public ContFun(DefFun function){
			this.setName(function.getName());
			Function = function;
		}
		
		public DefFun getFun(){
			return Function;
		}
}
