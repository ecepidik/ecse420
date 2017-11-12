package ca.mcgill.ecse420.a2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FilterLock implements Lock{
	
	private int threadCount;
	List<Integer> level = new ArrayList<Integer>(threadCount);
	List<Integer> victim = new ArrayList<Integer>(threadCount);
//	private AtomicInteger[] level;
//  private AtomicInteger[] victim;
	
	public FilterLock(int threadCount){
		this.threadCount = threadCount;
//		level = new AtomicInteger[threadCount];
//		victim = new AtomicInteger[threadCount];
		for (int i = 0 ; i<threadCount ; i++){
//			level[i] = new AtomicInteger();
//			victim[i] = new AtomicInteger();
			level.add(i,0);
			victim.add(i,0);
		}
	}
	
	@Override
	public void lock() {
		int pid = Integer.parseInt(Thread.currentThread().getName());
		System.out.println("FilterLock Thread: " +pid + " wants to enter");
		for (int l = 0; l<threadCount;l++){ //one level at a time
//			level[pid].set(l);
//			victim[l].set(pid);
			level.set(pid,l);
			victim.set(l,pid);
			for(int k = 0 ; k < threadCount ; k++){
				//while((k!=pid) && (level[k].get() >= l && victim[l].get() == pid)){
				while((k!=pid) && (level.get(k) >= l && victim.get(l) == pid)){
					//do nothing
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
	
	@Override
	public void unlock() {
		int pid = Integer.parseInt(Thread.currentThread().getName());
//		level[pid].set(0);
		level.set(pid,0);
		System.out.println("FilterLock Thread: " + pid +" exits CS");
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
