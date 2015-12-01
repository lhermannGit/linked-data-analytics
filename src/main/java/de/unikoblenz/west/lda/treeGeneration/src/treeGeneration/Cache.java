package treeGeneration;

import java.util.LinkedList;
import java.util.List;

public class Cache {
	
	public static void main(String[] args) {
		// temporary method for testing the cache
		Cache myCache = new Cache(1000, 0, 0.75, 0.5);
		myCache.input(0, "test01", "test12");
		myCache.input(1, "test11", "test12");
		myCache.input(2, "test21", "test22");
		myCache.input(3, "test31", "test32");
		myCache.input(4, "test11", "test12");
		myCache.input(5, "test21", "test22");
		myCache.input(6, "test31", "test32");
		myCache.input(7, "test11", "test12"); 
		myCache.input(8, "test21", "test22");
		myCache.input(9, "test31", "test32");
		myCache.flush(0.5);
	}
	
	private List<Tuple<Integer, String, String>> myCache; // List<startLvl, parent, current>
	private List<Tuple<Integer, String, String>> tempList; // tempList for storing the results
	private int capacity; // the maximum number of entries in the cache
	private int currentSize; // the current number of entries in the cache
	private double flushThreshold; // if the VALUE % capacity of the cache is reached it will flush
	private double minimumThreshold; // the % on how much of the cache should be flushed

	public Cache(int capacity, int currentSize, double flushThreshold, double minimumThreshold) {
		initialize(capacity, currentSize, flushThreshold, minimumThreshold);
	}
	
	private void initialize(int capacity, int currentSize, double flushThreshold, double minimumThreshold) {
		this.capacity = capacity;
		this.currentSize = currentSize;
		this.flushThreshold = flushThreshold;
		this.minimumThreshold = minimumThreshold;
		myCache = new LinkedList<Tuple<Integer, String, String>>();
		tempList = new LinkedList<Tuple<Integer, String, String>>();
	}
	
	public void flush(double minimumThreshold) {
		currentSize = 0;
		Tuple<Integer, String, String> currentTuple;
		int size = (int) (capacity * minimumThreshold);
		for (int i = 1; i < size; i++) { // iterate over the Cache and flush the elements
			if (myCache.size() == 0) {
				break;
			}
			currentTuple = myCache.get(0);
			System.out.println(currentTuple.current);
			System.out.println(currentTuple.parent);
			System.out.println(currentTuple.startLvl);
			// write startLvl into the database
			// write parent into the database
			// write current into the database
			myCache.remove(0);		
		}
	}
	
	public void input(int startLvl, String parent, String current) {
		if (currentSize >= capacity * flushThreshold) { // flush the cache if the threshold is reached
			flush(minimumThreshold);
		}
		currentSize++;
		// add the tuple with the FIFO method	
		Tuple<Integer, String, String> newTuple = new Tuple<>(startLvl, parent, current);
		myCache.add(newTuple);
	}
	
	public List<Tuple<Integer, String, String>> get(String parent) { // return all tuples that share the arguments
		tempList.clear();
		for (Tuple<Integer, String, String> currentTuple :myCache) {
			if (currentTuple.parent == parent ) { // to be changed
				tempList.add(currentTuple);
			}
		}
		return tempList;
	}
	
	public void close() {
		flush(1); // flush all elements to empty the cache
	}
}
