package ca.mcgill.ecse420.a3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue {
	
		ReentrantLock enqLock, deqLock;
		Condition notEmptyCondition, notFullCondition;
		AtomicInteger size;
//		Node head, tail;
		int capacity;
		
		
		public BoundedQueue(int _capacity) {
		capacity = _capacity;
//		head = new Node(null);
//		tail = head;
		size = new AtomicInteger(0);
		enqLock = new ReentrantLock();
		notFullCondition = enqLock.newCondition();
		deqLock = new ReentrantLock();
		notEmptyCondition = deqLock.newCondition();
	}
	
	
	
	
	

}
