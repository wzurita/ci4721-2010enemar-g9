package DataList;

public class List {

	private Container First;
	private Container Last;
	private int Length;
	
	
	List(Container con){
		First = con;
		Last = con;		
		Length = 0;
	}
	
	boolean add(Container con){
		if(search(con.getName())== null){
			Last.setNext(con);
			Last = con;
			Length++;
			return true;
		}else{
			return false;
		}
	}
	
	Container get(int index){
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
	
	Container search(String Name){
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
	
	Container[] toArray(){
		Container[] tmp = new Container[Length+1];
		Container Iterator = First;
			
		for(int Pointer = 0; Pointer <=Length; Pointer++ ){
			tmp[Pointer] = Iterator;
			Iterator = Iterator.getNext();
		}		
		
		return tmp;
	}
	
	int Length(){
		return Length+1;
	}
	
	public static void main(String[] args){
		List Lista = new List(new ContStr("Hola"));
		System.out.println(Lista.get(0).getName());		
		Lista.add(new ContStr("Prueba"));
		System.out.println(Lista.get(1).getName());	
		Lista.add(new ContStr("Louise"));
		System.out.println(Lista.search("Louise").getName());	
		System.out.println(Lista.search("Prueba").getName());	
		System.out.println(Lista.add(new ContStr("Hola")));
		
		Container[] tmp = Lista.toArray();
		
		for(int i = 0; i < Lista.Length(); i++){
			System.out.print(tmp[i].getName() + " ");
		}
		
	}
	
}
