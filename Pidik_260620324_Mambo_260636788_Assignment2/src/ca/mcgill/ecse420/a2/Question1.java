package ca.mcgill.ecse420.a2;

import java.util.concurrent.locks.Lock;


public class Question1{
	
	public static int threadCount = 5;
	public static int maxCount = 50;
		
		
	public static void main (String args []){
		
		Counter Bakery= new Counter(maxCount,"Bakery",threadCount);
		Counter Peterson=new Counter(maxCount,"FilterLock",threadCount);
		long BakeryRuntime = 0;
		long FilterLockRuntime = 0;
		
		//Run lock test Bakery
		try{
			BakeryRuntime = lockTest(Bakery,threadCount);
			
		}catch(InterruptedException e){
			System.out.println("Error for BakeryTest");
		}
		
		//Run lock test Peterson
		try{
			FilterLockRuntime = lockTest(Peterson,threadCount);
			
		}catch(InterruptedException e){
			System.out.println("Error for FilterTest");
		}
		
		System.out.println("BakeryTest Runtime:" + BakeryRuntime);
		System.out.println("FilterLockTest Runtime:" + FilterLockRuntime);
		
	}
	
	private static long lockTest(Counter counter, int threadCount) throws InterruptedException{
		//ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		long startTime = System.nanoTime()/1000000;
		
		Thread[] counters = new Thread[threadCount];
		for(int i = 0; i < threadCount; i++) {
			counters[i] = new Thread(counter);
			counters[i].setName(Integer.toString(i));
			System.out.println(Integer.toString(i));
		}
		
		for(Thread t : counters){
			t.start();
		}
		
		for(Thread t : counters){
			t.join();
		}
		
		long endTime = System.nanoTime()/1000000;
		long duration = endTime - startTime;
		
		return duration;
	}


	//create simple counter
	private static class Counter implements Runnable {
		private int initValue;
		private int maxValue;
		private Lock lock;
		private int threadCount;
		private int result = 0;
		
		public Counter(int max, String locktype, int threadCount){
			this.initValue = 0;
			this.maxValue = max;
			this.threadCount = threadCount;
			switch (locktype){
			case "Bakery":
				lock = new Bakery (threadCount);
				break;
			case "FilteLock":
				lock = new FilterLock (threadCount);
				break;
			}
			
		}
		
		public int increment() {
			int temp;
			lock.lock();
			try{
				if(result >= maxValue){
					return result;
				}
				else{
					temp = result;
					result = temp + 1;
				}
			}finally{
				lock.unlock();
			}
			return temp;
		}

		@Override
		public void run() {
			while(increment() < this.maxValue){
				//do nothing
			}
			
		}
		
	}
	

}
