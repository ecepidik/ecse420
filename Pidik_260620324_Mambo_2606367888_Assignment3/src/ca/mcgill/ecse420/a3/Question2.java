package ca.mcgill.ecse420.a3;

public class Question2 {
	
	public static void main (String args[]) {
		FineGrainedList<Integer> list = new FineGrainedList<Integer>();
		
		for(int i = 0 ; i<10; i++) {
			list.add((Integer)i);
		}
		
		boolean test = list.contains((Integer)7);
		if(test) {
			System.out.println("List Contains Item");
		}
		else {
			System.out.println("List does not contain item");
		}
		
	}
	
//	private static boolean syncTest() {
//		
//	}

}
