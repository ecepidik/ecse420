package ca.mcgill.ecse420.a2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Bakery implements Lock {
	
	private int threadCount;
//	List<Integer> ticket = new ArrayList<Integer>(threadCount);
//	List<Boolean> enter =  new ArrayList<Boolean>(threadCount);
	private AtomicInteger[] ticket;
	private AtomicBoolean[] enter;
	
	public Bakery(int threadCount){
		this.threadCount = threadCount;
		ticket = new AtomicInteger[threadCount];
		enter = new AtomicBoolean[threadCount];
		for (int i = 0 ; i<threadCount ; i++){
//			ticket.add(i,0);
//			enter.add(i,false);
			ticket[i] = new AtomicInteger();
			enter[i]= new AtomicBoolean();
		}
	}
	
//	@Override
//	public void lock(){
//		int pid = Integer.parseInt(Thread.currentThread().getName());
//		System.out.println("BakeryLock Thread: " +pid + " wants to enter");
//		enter.set(pid, true);
//		int max = Collections.max(ticket);
//		ticket.set(pid,max+1);
//		enter.set(pid,false);
//		for(int i = 0; i<ticket.size(); i++){
//			if( i != pid){ //skip current thread
//				while(enter.get(i)){
//					//do nothing while another thread picks ticket
//				}
//				
//				while(ticket.get(i) != 0 && ((ticket.get(i) < ticket.get(pid)) || (ticket.get(i) == ticket.get(pid) && pid>i))){
//					try {
//						Thread.sleep(((int) Math.random() * 10000) + 5000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					System.out.println("Thread: " + pid + " Waiting");
//				}	
//			}	
//		}
//		//now enter CS
//		System.out.println("Thread: " + pid + " Executing");
//		
//	}
//	
	//Atomic Version of Lock
	@Override
	public void lock(){
		int pid = Integer.parseInt(Thread.currentThread().getName());
		System.out.println("BakeryLock Thread: " +pid + " wants to enter");
		enter[pid].set(true);
		int max = findMaxVal(ticket);
		ticket[pid].set(max+1);
		enter[pid].set(false);
		for(int i = 0; i<ticket.length; i++){
			if( i != pid){ //skip current thread
				while(enter[i].get()){
					//do nothing while another thread picks ticket
				}
				
				while(ticket[i].get() != 0 && ((ticket[i].get() < ticket[pid].get()) || (ticket[i].get() == ticket[pid].get() && pid>i))){
					try {
						Thread.sleep(((int) Math.random() * 10000) + 5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Thread: " + pid + " Waiting");
				}	
			}	
		}
		//now enter CS
		System.out.println("Thread: " + pid + " Executing");
		
	}
	
	
//	@Override
//	public void unlock(){
//		int pid = Integer.parseInt(Thread.currentThread().getName());
//		ticket.set(pid,0);
//		System.out.println("BakeryLock Thread: " + pid +" exits CS");
//	}
	
	//Atomic Version of unlock
	@Override
	public void unlock(){
		int pid = Integer.parseInt(Thread.currentThread().getName());
		ticket[pid].set(0);
		System.out.println("BakeryLock Thread: " + pid +" exits CS");
	}

	//Calc max value for atomic version
	private int findMaxVal(AtomicInteger[] a) {
		int currentMax = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i].get() > currentMax)
				currentMax = a[i].get();
		}
		return currentMax;
	}
	
	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
