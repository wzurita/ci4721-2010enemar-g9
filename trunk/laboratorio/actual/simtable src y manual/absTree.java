package simtable;

public class absTree extends instruction{
	
	private data Value;
	
	absTree nodeLeft;
	
	absTree nodeRight;
	
	absTree(data value){
		Value = value;		
	}
	
	data Value(){
		return Value;
	}
	
	void expandLeft(data lvalue, data rvalue){
		
		if(nodeLeft == null && nodeRight ==null){
			nodeLeft = new absTree(lvalue);
			nodeRight = new absTree(rvalue);
		}else{
			nodeLeft.expandLeft(lvalue, rvalue);			
		}
	}
	
	void expandRight(data lvalue, data rvalue){
		
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
