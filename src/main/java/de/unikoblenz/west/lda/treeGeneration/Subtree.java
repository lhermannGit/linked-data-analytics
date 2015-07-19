package de.unikoblenz.west.lda.treeGeneration;

import java.util.LinkedList;

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
		return this.subtreeList.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return this.subtreeList.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.subtreeList.hashCode();
	}
}
