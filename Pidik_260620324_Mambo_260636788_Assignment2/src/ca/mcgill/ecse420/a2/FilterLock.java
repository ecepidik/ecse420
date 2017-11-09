package ca.mcgill.ecse420.a2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FilterLock implements Lock{
	
	private int threadCount;
	List<Integer> level;
	List<Integer> victim;
	
	public FilterLock(int threadCount){
		this.threadCount = threadCount;
		level = new ArrayList<>(threadCount);
		victim = new ArrayList<>(threadCount);
		for (int i = 0 ; i<threadCount ; i++){
			level.set(i,0);
		}
	}

	public void lock(int pid) {
		for (int l = 0; l<threadCount;l++){ //one level @ a time
			level.set(pid,l);
			victim.set(l,pid);
			for(int k = 0 ; k < threadCount ; k++){
				while((k!=pid) && (level.get(k) >= l && victim.get(l) == pid)){
					//do nothing
				}
			}	
		}	
	}

	public void unlock(int pid) {
		level.set(pid,0);	
	}

	@Override
	public void lock() {
		// TODO Auto-generated method stub
		
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
	public void unlock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
