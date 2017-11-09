package ca.mcgill.ecse420.a2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Bakery implements Lock {
	
	private int threadCount;
	List<Integer> ticket = new ArrayList<>(threadCount);
	List<Boolean> enter =  new ArrayList<>(threadCount);
	
	public Bakery(int threadCount){
		this.threadCount = threadCount;
	}
	
	@Override
	public void lock(){
		int pid = (int) Thread.currentThread().getId();
		enter.set(pid, true);
		int max = Collections.max(ticket);
		ticket.set(pid,max+1);
		enter.set(pid,false);
		for(int i = 0; i<ticket.size(); i++){
			if( i != pid){ //skip current thread
				while(enter.get(i)){
					//do nothing while current thread is in CS
				}
				
				while(ticket.get(i) != 0 && ((ticket.get(i) < ticket.get(pid)) || (ticket.get(i) == ticket.get(pid) && pid>i))){
					//thread does nothing while it waits
				}	
			}	
		}
		//now enter CS
	}
	
	public void unlock(){
		int pid = (int) Thread.currentThread().getId();
		enter.set(pid,false);
		System.out.println("Unlock Thread:" + pid);
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
