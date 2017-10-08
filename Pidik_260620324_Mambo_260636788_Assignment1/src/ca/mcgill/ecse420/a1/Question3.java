package ca.mcgill.ecse420.a1;

import java.util.concurrent.*;

public class Question3 implements Runnable {
	
	private int PHILOSOPHER_NUMBER = 5;
	
	private enum State {THINKING, EATING, WAITING};
	private State[] states = new State[PHILOSOPHER_NUMBER];
	
	private Semaphore mutex = new Semaphore(1);
	private Semaphore[] s = new Semaphore[PHILOSOPHER_NUMBER];
	
	public void Philosopher (int i) throws InterruptedException {
		while(true) {
			think();
			takeFork(i);
			eat();
			putFork(i);
		}
	}
	
	public void takeFork(int i) {
		try {
			mutex.acquire();
			states[i] = State.WAITING;
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
		Thread.sleep(((int) Math.random() * 10) + 10);
	}
	
	public static void eat() throws InterruptedException {
		Thread.sleep(((int) Math.random() * 10) + 10);
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
	
	public static void main(String[] args) {
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}


