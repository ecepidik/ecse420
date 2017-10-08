package ca.mcgill.ecse420.a1;

public class Question2 {

	public static Object tvLock = new Object();
	public static Object remoteLock = new Object();

	public static void main(String[] args){
		Thread bT = new Thread(new Brother());
		Thread sT = new Thread(new Sister());
		bT.start();
		sT.start();
	}

	private static class Brother implements Runnable{
		public void run(){
			synchronized(tvLock){
				System.out.println("Brother acquires Tv");

				try{Thread.sleep(5);}
				catch(InterruptedException e){}
				
				/*Now the Brother will request the Remote*/
				System.out.println("Brother wants the Remote");
		
				synchronized(remoteLock){
					System.out.println("Brother now has Remote & Tv");
				}

			}
		}
	}

	private static class Sister implements Runnable{
		public void run(){
			synchronized(remoteLock){
				System.out.println("Sister acquires remote");
			
				try{Thread.sleep(5);}
				catch(InterruptedException e){}
				
				/*Now the Sister will request the TV*/
				System.out.println("Sister wants the Tv");

				synchronized(tvLock){
					System.out.println("Sister now has Remote & Tv");
				}

			}
		}	
	}

}
