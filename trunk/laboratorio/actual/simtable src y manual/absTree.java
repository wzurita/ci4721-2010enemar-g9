package simtable;

public class absTree extends instruction{
	
	private String Value;
	
	absTree nodeLeft;
	
	absTree nodeRight;
	
	absTree(String value){
		Value = value;		
	}
	
	String Value(){
		return Value;
	}
	
	void expandLeft(String lvalue, String rvalue){
		
		if(nodeLeft == null && nodeRight ==null){
			nodeLeft = new absTree(lvalue);
			nodeRight = new absTree(rvalue);
		}else{
			nodeLeft.expandLeft(lvalue, rvalue);			
		}
	}
	
	void expandRight(String lvalue, String rvalue){
		
		if(nodeLeft == null && nodeRight ==null){
			nodeLeft = new absTree(lvalue);
			nodeRight = new absTree(rvalue);
		}else{
			nodeRight.expandLeft(lvalue, rvalue);			
		}
	}
	
	void insertTreeLeft(absTree left){
		nodeLeft = left;
	}
	
	void insertTreeRight(absTree right){
		nodeRight = right;
	}
	
}
