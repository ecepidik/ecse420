package ca.mcgill.ecse420.a1;

import java.util.concurrent.*;

public class Question3 {


	public static void main(String[] args){

		Thread p1 = new Thread(new Philosopher(0));
		Thread p2 = new Thread(new Philosopher(1));
		Thread p3 = new Thread(new Philosopher(2));
		Thread p4 = new Thread(new Philosopher(3));
		Thread p5 = new Thread(new Philosopher(4));
		
		p1.start();
		p2.start();
		p3.start();
		p4.start();
		p5.start();

	}


	private static class Philosopher implements Runnable {

		private int PHILOSOPHER_NUMBER = 5;
		private int PHILOSOPHER_ID;

		private enum State {THINKING, EATING, WAITING};
		private State[] states = new State[PHILOSOPHER_NUMBER];

		private Semaphore mutex = new Semaphore(1);
		private Semaphore[] s = new Semaphore[] {new Semaphore(1),
			     new Semaphore(1), new Semaphore(1), new Semaphore(1), new Semaphore(1)};

		Philosopher(int i) {
			this.PHILOSOPHER_ID = i;
		}

		@Override
		public void run() {
			while(true) {
				try {
					think();
					System.out.println("Philosopher " + PHILOSOPHER_ID + " THINKING");
					takeFork(PHILOSOPHER_ID);
					eat();
					System.out.println("Philosopher " + PHILOSOPHER_ID + " EATING");
					putFork(PHILOSOPHER_ID);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void takeFork(int i) {
			try {
				mutex.acquire();
				states[i] = State.WAITING;
				System.out.println("Philosopher " + PHILOSOPHER_ID + " WAITING");
				test(i);
				mutex.release();
				s[i].acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public void putFork(int i) {
			try {
				mutex.acquire();
				states[i] = State.THINKING;
				test((i+PHILOSOPHER_NUMBER-1)%PHILOSOPHER_NUMBER);
				test((i+1)%PHILOSOPHER_NUMBER);
				mutex.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		public static void think() throws InterruptedException {
			Thread.sleep(((int) Math.random() * 10000) + 1000);
		}

		public static void eat() throws InterruptedException {
			Thread.sleep(((int) Math.random() * 10000) + 1000);
		}

		public void test(int i) {
			int leftPhilosopher = (i+PHILOSOPHER_NUMBER-1)%PHILOSOPHER_NUMBER;
			int rightPhilosopher = (i+1)%PHILOSOPHER_NUMBER;
			if(states[i] == State.WAITING 
					&& states[leftPhilosopher] != State.EATING
					&& states[rightPhilosopher] != State.EATING) {
				states[i] = State.EATING;
				s[i].release();
			}
		}
	}
}


