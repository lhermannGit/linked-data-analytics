package de.unikoblenz.west.lda.treeGeneration;

import java.util.LinkedList;
import java.util.List;

/**
 *	Class for storing subtrees. 
 *
 * @author Martin Koerner <info@mkoerner.de>
 *
 */
public class Subtree {

	// we use LinkedList due to it's efficiency when adding nodes. Memory 
	// consumption is not an issue since we work with int array in later steps
	private LinkedList<Integer> subtreeList;
	private final int levelSeparator = -1;
	private boolean wasExtended;

	public Subtree() {
		this.subtreeList = new LinkedList<Integer>();
		this.wasExtended = false;
	}

	@SuppressWarnings("unchecked")
	private Subtree(LinkedList<Integer> subtree) {
		this.subtreeList = (LinkedList<Integer>)subtree.clone();
		this.wasExtended = false;
	}

	public boolean wasExtended() {
		return this.wasExtended;
	}

	public void setExtended() {
		this.wasExtended = true;
	}

	public int[] toArray(){
		int[] subtreeArray=new int[subtreeList.size()];
		for(int index=0;index<subtreeList.size();index++){
			subtreeArray[index]=subtreeList.get(index);
		}
		return subtreeArray;
	}
	
	/**
	 * Reads a String representation of an array and create a subtree based on it.
	 * Format of the String: "3,2,1,-1,-1,-1,4,-1"
	 * 
	 * @param subtreeAsString
	 */
	public void readString(String subtreeAsString){
		this.subtreeList=new LinkedList<Integer>();
		if(subtreeAsString.length()<2){
			return;
		}
		String[] subtreeAsStringSplit=subtreeAsString.split(",");
		for(String subtreeAsStringElement:subtreeAsStringSplit){
			subtreeList.add(Integer.parseInt(subtreeAsStringElement));
		}
	}

	public List<Integer> toList(){
		return this.subtreeList;
	}

	public void addBefore(int element) {
		this.subtreeList.add(0, element);
		this.subtreeList.add(this.levelSeparator);
	}
	
	public void addAfter(int element) {
		this.subtreeList.add(element);
		this.subtreeList.add(this.levelSeparator);
	}

	public void addTreeAfter(Subtree subtree) {
		this.subtreeList.addAll(subtree.subtreeList);
	}

	@Override
	public Subtree clone() {
        return new Subtree(this.subtreeList);
	}

	@Override
	public String toString() {
		String subtreeAsString="";
		for(int subtreeElement:subtreeList){
            if(subtreeAsString.length()>0)			{
                subtreeAsString+=",";
            }
            subtreeAsString+=subtreeElement;
		}
		return subtreeAsString;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == Subtree.class) {
			return this.subtreeList.equals(((Subtree)obj).toList());
		}else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.subtreeList.hashCode();
	}
}
