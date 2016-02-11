import java.util.*;

public class Test{
	public static void main(String[] args){
		int id = 3;
		TreeMap<Integer, String> fingerTable = new TreeMap(); 
		fingerTable.put(1,"oneeee");
		fingerTable.put(2,"twoooo");
		//fingerTable.put(3,"threee");
		fingerTable.put(4,"fouuur");
		fingerTable.put(5,"fiveee");
		for(int i = 0; i < fingerTable.size(); i++) {
			//update each finger table by remove the reference to this.id
			int prevNode =  id -(int)Math.pow((double)2, (double)(i-1));
			System.out.println(prevNode);
		}
	}
}