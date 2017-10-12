package ca.mcgill.ecse420.a1;

import java.util.concurrent.*;

public class Question3 {

	// Change the variable to add/decrease the number of philosophers
	public static int PHILOSOPHER_NUMBER = 5;

	public static void main(String[] args){

		// Creates the array of shared objects (chopsticks) as an array of semaphores
		// Each semaphore in the array is used by two threads (philosophers)
		// (e.g. LEFT_CHOPSTICK for philosopher 2 is also the RIGHT_CHOPSTICK for philosopher 1)
		Semaphore[] chopSticks = new Semaphore[PHILOSOPHER_NUMBER];
		for(int i = 0; i < PHILOSOPHER_NUMBER; i++) {
			chopSticks[i] = new Semaphore(1);
		}

		// creates and runs the threads for the deadlock free and starvation free solution to the Dining Philosophers Problem
		// uncomment/comment the following lines to run/block the Philosopher threads 
		for(int i=0; i<PHILOSOPHER_NUMBER; i++) {
			(new Thread(new Philosopher(i, chopSticks[i], chopSticks[(i+1)%PHILOSOPHER_NUMBER]))).start();
		}
		
		// creates and runs the threads for the solution with deadlock and starvation to the Dining Philosophers Problem
		// uncomment/comment the following lines to run/block the GreedyPhilosopher threads 
		/*
		for(int i=0; i<PHILOSOPHER_NUMBER; i++) {
			(new Thread(new GreedyPhilosopher(i, chopSticks[i], chopSticks[(i+1)%PHILOSOPHER_NUMBER]))).start();
		} 
 		*/
	}

	// This class is the correct implementation solution to the Dining Philosophers Problem with N Philosophers
	// NO DEADLOCKS & NO STARVATION
	private static class Philosopher implements Runnable {

		private int PHILOSOPHER_ID;		
		private Semaphore LEFT_CHOPSTICK;
		private Semaphore RIGHT_CHOPSTICK;

		// Philosopher (thread) constructor
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
			System.out.println("Philosopher " + (PHILOSOPHER_ID + 1) + " trying to acquire chopstick");
			// Tries to acquire the semaphore (chopstick); 
			// if true semaphore is decreased by 1 so that another thread can't acquire
			if(LEFT_CHOPSTICK.tryAcquire()) {
				// Tries to acquire the second semaphore (chopstick)
				if(RIGHT_CHOPSTICK.tryAcquire()) {
					System.out.println("Philosopher " + (PHILOSOPHER_ID + 1) + " acquired chopstick");
					// If both chopsticks can be acquired the philosopher 'eats' (thread sleeps for random time)
					eat(PHILOSOPHER_ID);
					// Both semaphores are released (their number values are increased) and can be acquired by another thread now
					LEFT_CHOPSTICK.release();
					RIGHT_CHOPSTICK.release();
				} else {
					// If the second semaphore can't be acquired the first one is released
					System.out.println("Philosopher " + (PHILOSOPHER_ID + 1) + " couldn't acquire RIGHT chopstick");
					LEFT_CHOPSTICK.release();				}
			} else {
				System.out.println("Philosopher " + (PHILOSOPHER_ID + 1) +" couldn't acquire LEFT chopstick");
			}
		}

		// Makes the thread sleep for a random amount of time while the philosopher is "thinking"
		public static void think(int i) throws InterruptedException {
			System.out.println("Philosopher " + (i+1) + " THINKING");
			Thread.sleep(((int) Math.random() * 10000) + 5000);
			System.out.println("Philosopher " + (i+1) + " finished thinking");

		}

		// Makes the thread sleep for a random amount of time while the philosopher is "eating"
		public static void eat(int i) throws InterruptedException {
			System.out.println("Philosopher " + (i+1) + " EATING");
			Thread.sleep(((int) Math.random() * 10000) + 5000);
			System.out.println("Philosopher " + (i+1) + " finished EATING and putdown chopsticks");
		}
	}

/* ----------------------------- DEADLOCK DINING PHILOSOPHER ---------------------------------------*/
	
	
	// This class is the wrong implementation solution to the Dining Philosophers Problem with N Philosophers
	// DEADLOCKS & STARVATION
	private static class GreedyPhilosopher implements Runnable {

		private int PHILOSOPHER_ID;		
		private Semaphore LEFT_CHOPSTICK;
		private Semaphore RIGHT_CHOPSTICK;

		GreedyPhilosopher(int id, Semaphore leftChopstick, Semaphore rightChopstick) {
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
					takeChopstickAndDontLetGo(PHILOSOPHER_ID);
				} catch (InterruptedException e) {}
			}
		}

		// T
		public void takeChopstickAndDontLetGo(int i) throws InterruptedException {
			// Tries to acquire the semaphore (chopstick); 
			// if true semaphore is decreased by 1 so that another thread can't acquire
			System.out.println("Greedy Philosopher " + (PHILOSOPHER_ID + 1) + " trying to acquire chopstick");
			if(LEFT_CHOPSTICK.tryAcquire()) {
				if(RIGHT_CHOPSTICK.tryAcquire()) {
					System.out.println("Greedy Philosopher " + (PHILOSOPHER_ID + 1) + " acquired chopstick");
					// If both chopsticks can be acquired the philosopher 'eats' (thread sleeps for random time)
					eat(PHILOSOPHER_ID);
					// Both semaphores are released (their number values are increased) and can be acquired by another thread now
					LEFT_CHOPSTICK.release();
					RIGHT_CHOPSTICK.release();
				} else {
					// If the thread can not acquire the second semaphore it doesn't release the first semaphore
					/* THIS CREATES DEADLOCK, because a philosophers keeps holding on to the chopstick
					 * until it acquires both of them and eats, eventually each philosopher will be holding
					 * one chopstick requested by the philosopher sitting next to them
					 */
					System.out.println("Greedy Philosopher " + (PHILOSOPHER_ID + 1) + " couldn't acquire RIGHT chopstick but will keep on holding LEFT chostick");
				}
			} else {
				System.out.println("Greedy Philosopher " + (PHILOSOPHER_ID +1) +" couldn't acquire LEFT chopstick");
			}
		}

		// Makes the thread sleep for a random amount of time while the philosopher is "thinking"
		public static void think(int i) throws InterruptedException {
			System.out.println("Greedy Philosopher " + (i+1) + " THINKING");
			Thread.sleep(((int) Math.random() * 10000) + 5000);
			System.out.println("Greedy Philosopher " + (i+1) + " finished thinking");

		}
		
		// Makes the thread sleep for a random amount of time while the philosopher is "eating"
		public static void eat(int i) throws InterruptedException {
			System.out.println("Greedy Philosopher " + (i+1) + " EATING");
			Thread.sleep(((int) Math.random() * 10000) + 5000);
			System.out.println("Greedy Philosopher " + (i+1) + " finished EATING and putdown chopsticks");
		}
	}
}


