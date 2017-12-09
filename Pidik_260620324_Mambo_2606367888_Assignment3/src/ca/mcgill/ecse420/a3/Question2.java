package ca.mcgill.ecse420.a3;

public class Question2 {
	
	public static int threadCount = 10;
	
	public static void main (String args[]) {
		FineGrainedList<Integer> list = new FineGrainedList<Integer>();
		
		Tester test = new Tester(list,threadCount);
		
		//Run Fine Grained Test
		try{
			fineGrainedTest(test,threadCount);
			
		}catch(InterruptedException e){
			System.out.println("Error from Fine Grained test");
		}
		
	}
	
	private static void fineGrainedTest(Tester test, int threadCount) throws InterruptedException {
		
		//create array of threads
		Thread[] testers = new Thread[threadCount];
		
		//initialize each thread as an implementation of Tester Class and set thread name
		for(int i = 0; i < threadCount; i++) {
			testers[i] = new Thread(test);
			testers[i].setName(Integer.toString(i));
		}
		
		for(Thread t : testers){
			t.start();
		}
		
		for(Thread t : testers){
			t.join();
		}

		
	}
	
	//create Tester Threads
		private static class Tester implements Runnable {
			private FineGrainedList<Integer> list;
			private int threadCount;
			
			
			public Tester(FineGrainedList<Integer> list, int threadCount){
				this.threadCount = threadCount;
				this.list = list;
				
			}
			
			public boolean add() {
				//get Thread Name and convert to Integer
				Integer item = Integer.parseInt(Thread.currentThread().getName());
				
				//add item to FineGrainedList
				boolean added = list.add(item);
				if(added) {
					System.out.println("Successfully added: " + item + " by Thread: " + item);
				}
				else {
					System.out.println("Item was not added to List");
				}
				return added;
			}
			
			public boolean check() {
				//get Thread Name and convert to Integer
				Integer item = Integer.parseInt(Thread.currentThread().getName());
				//check if item is now contained in FineGrainedList
				Integer checkItem = ((item+1)%threadCount);
				boolean contains = list.contains(checkItem);
				if(contains) {
					System.out.println("List contains item: " + checkItem + " by Thread " + item);
				}
				else {
					System.out.println("List does not contain item " + checkItem + " by Thread " + item);
				}
				return contains;
			}
			
			public boolean remove() {
				//get Thread Name and convert to Integer
				Integer item = Integer.parseInt(Thread.currentThread().getName());
				//remove item from FineGrainedList
				boolean removed = list.remove(item);
				if(removed) {
					System.out.println("Successfully removed: " + item + " by Thread: " + item);
				}
				else {
					System.out.println("List still contains item or item was never in List");
				}
					
				return removed;
			}

			@Override
			public void run() {
				//run correctness check functions
				add();
				check();
				remove();
			}
			
		}
			
}
