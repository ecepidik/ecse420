package ca.mcgill.ecse420.a1;

import java.util.concurrent.*;

public class Question3 {

	public static int PHILOSOPHER_NUMBER = 5;
	
	public static void main(String[] args){
		
		Semaphore[] chopSticks = new Semaphore[PHILOSOPHER_NUMBER];
		for(int i = 0; i < PHILOSOPHER_NUMBER; i++) {
	        chopSticks[i] = new Semaphore(1);
	    }; 
	 		
		for(int i=0; i<PHILOSOPHER_NUMBER; i++) {
			(new Thread(new Philosopher(i, chopSticks[i], chopSticks[(i+1)%PHILOSOPHER_NUMBER]))).start();
		}
	}

	private static class Philosopher implements Runnable {

		private int PHILOSOPHER_ID;		
		private Semaphore LEFT_CHOPSTICK;
		private Semaphore RIGHT_CHOPSTICK;
		
		Philosopher(int id, Semaphore leftChopstick, Semaphore rightChopstick) {
			this.PHILOSOPHER_ID = id;
			this.LEFT_CHOPSTICK = leftChopstick;
			this.RIGHT_CHOPSTICK = rightChopstick;
		}

		@Override
		public void run() {
			while(true) {
					try {
						think(PHILOSOPHER_ID);
					} catch (InterruptedException e1) {}
					try {
						takeChopstick(PHILOSOPHER_ID);
					} catch (InterruptedException e) {}
			}
		}

		public void takeChopstick(int i) throws InterruptedException {
			System.out.println("Philosopher " + (PHILOSOPHER_ID) + " trying to acquire chopstick");
			if(LEFT_CHOPSTICK.tryAcquire()) {
				if(RIGHT_CHOPSTICK.tryAcquire()) {
					System.out.println("Philosopher " + (PHILOSOPHER_ID) + " acquired chopstick");
					eat(PHILOSOPHER_ID);
					LEFT_CHOPSTICK.release();
					RIGHT_CHOPSTICK.release();
				} else {
					System.out.println("Philosopher " + (PHILOSOPHER_ID) + " couldn't acquire RIGHT chopstick");
					LEFT_CHOPSTICK.release();				}
			} else {
				System.out.println("Philosopher " + PHILOSOPHER_ID +" couldn't acquire LEFT chopstick");
			}
		}
		
		public static void think(int i) throws InterruptedException {
			System.out.println("Philosopher " + (i) + " THINKING");
			Thread.sleep(((int) Math.random() * 10000) + 5000);
			System.out.println("Philosopher " + (i) + " finished thinking");

		}

		public static void eat(int i) throws InterruptedException {
			System.out.println("Philosopher " + (i) + " EATING");
			Thread.sleep(((int) Math.random() * 10000) + 5000);
			System.out.println("Philosopher " + (i) + " finished EATING and putdown chopsticks");
		}
	}
}


