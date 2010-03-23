package DataList;

public class List {

	private Container First;
	private Container Last;
	private int Length;
	
	
	public List(Container con){
		First = con;
		Last = con;		
		Length = 0;
	}
	
	public boolean add(Container con){
		if(search(con.getName())== null){
			Last.setNext(con);
			Last = con;
			Length++;
			return true;
		}else{
			return false;
		}
	}
	
	public Container get(int index){
		Container Iterator = First;
		int Pointer = 0;
		
		if(index > Length || index < 0){
			return null;
		}
			
		while(Pointer != index){			
			Iterator = Iterator.getNext();
			Pointer++;
		}
		
		return Iterator;
	}
	
	public Container search(String Name){
		Container Iterator = First;
		int Pointer = 0;
		
		while(Pointer <= Length){
			if(Iterator.getName().equals(Name)){
				return Iterator;
			}
			
			Iterator = Iterator.getNext();
			Pointer++;
		}		
		return null;
	}
	
	public Container[] toArray(){
		Container[] tmp = new Container[Length+1];
		Container Iterator = First;
			
		for(int Pointer = 0; Pointer <=Length; Pointer++ ){
			tmp[Pointer] = Iterator;
			Iterator = Iterator.getNext();
		}		
		
		return tmp;
	}
	
	public int Length(){
		return Length+1;
	}
}
