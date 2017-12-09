package ca.mcgill.ecse420.a3;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/*
 * The Add and Remove methods are adapted from the methods given in the Textbook Chapter 9
 */

public class FineGrainedList<T> {
	
	private Node<Integer> head;
	
	public FineGrainedList() {
		head = new Node<Integer>(Integer.MIN_VALUE);
		head.next = new Node<Integer>(Integer.MAX_VALUE);
	}
	
	//add method
	public boolean add(T item) {
		int key = item.hashCode();
		head.lock.lock();
		Node pred = head;
		try {
			Node curr = pred.next;
			curr.lock.lock();
			try {
				while (curr.key < key) {
					pred.lock.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock.lock();
				}
				if (curr.key == key) {
					return false;
				}
				Node<T> newNode = new Node(item);
				newNode.next = curr;
				pred.next = newNode;
				return true;
			} finally {
				curr.lock.unlock();
			}
		} finally {
			pred.lock.unlock();
		}
	}
	
	//remove method
	public boolean remove(T item) {
		Node pred = null, curr = null;
		int key = item.hashCode();
		head.lock.lock();
		try {
			pred = head;
			curr = pred.next;
			curr.lock.lock();
			try {
				while (curr.key < key) {
					pred.lock.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock.lock();
				}
				if (curr.key == key) {
					pred.next = curr.next;
					return true;
				}
				return false;
			} finally {
				curr.lock.unlock();
			}
		} finally {
			pred.lock.unlock();
		}
	}
	
	//contains method
	public boolean contains(T item) {
		//get key value of item
		int key = item.hashCode();
		//acquire lock and set predecessor to head
		head.lock.lock();
		Node pred = head;
		try {
			//set next node as current node
			Node curr = pred.next;
			//acquire lock
			curr.lock.lock();
			try {
				//search for key of item we are looking for and keep moving through the list two nodes at a time 
				while(curr.key < key) {
					pred.lock.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock.lock();
				}
				//after exiting the while loop the method will either return true if the current item equals the search item
				if(curr.key == key) {
					return true;
				}
				//else return false if it is not equal
				return false;
			}finally {
				//unlock current node
				curr.lock.unlock();
			}
		}finally {
			//unlock predecessor node
			pred.lock.unlock();
		}
	}
	
	private class Node<T> {
		private T item;
		int key;
		private Node<T> next;
		private Lock lock = new ReentrantLock();
		
		private Node (T item) {
			this.item = item;
			this.key = item.hashCode();
		}
		
	}
	
}
