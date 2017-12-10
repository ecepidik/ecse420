package ca.mcgill.ecse420.a3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue {
	
		private ReentrantLock enqLock, deqLock;
		private Condition notEmptyCondition, notFullCondition;
		private AtomicInteger size;
		private int capacity;
		
		//bounded queue implemented as array
		private Object[] Q;
		private int enqIndex,deqIndex;
		
		public BoundedQueue(int capacity) {
			this.capacity = capacity;
			size = new AtomicInteger(0);
			enqLock = new ReentrantLock();
			notFullCondition = enqLock.newCondition();
			deqLock = new ReentrantLock();
			notEmptyCondition = deqLock.newCondition();
			
			//init queue implemented as array
			Q = new Object[capacity];
			//implement indices
			enqIndex = 0;
			deqIndex = 0;
		}
		
		public <T> void enqueue(T a) throws InterruptedException {
			boolean mustWakeDequeuers = false;
			 enqLock.lock();
			try {
				while (size.get() == capacity)
						notFullCondition.await();
				//put item at enqueue Index
				Q[enqIndex]= a;
				//increment index
				enqIndex++;
				//reset index if reached cap
				if(enqIndex == capacity) {
					enqIndex = 0;
				}
				if (size.getAndIncrement() == 0)
					mustWakeDequeuers = true;
			} finally {
				enqLock.unlock();
			}
			if (mustWakeDequeuers) {
				deqLock.lock();
				try {
					notEmptyCondition.signalAll();
				} finally {
					deqLock.unlock();
				}
			}		
		}
		
		public <T> T dequeue() throws InterruptedException {
			T result = null;
			boolean mustWakeEnqueuers = true;
			deqLock.lock();
			try {
				while (size.get() == 0)
					notEmptyCondition.await();
				//remove item from dequeue index
				Q[deqIndex] = 0;
				//increment dequeue index
				deqIndex++;
				//reset dequeue index if cap reached
				if(deqIndex == capacity) {
					deqIndex = 0;
				}
				if (size.getAndIncrement() == capacity) {
					mustWakeEnqueuers = true;
				}
			} finally {
				deqLock.unlock();
			}
			if (mustWakeEnqueuers) {
				enqLock.lock();
				try {
					notFullCondition.signalAll();
				} finally {
					enqLock.unlock();
				}
			}
			return result;
		}
}
