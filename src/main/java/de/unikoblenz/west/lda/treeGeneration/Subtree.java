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

	// contains the subtree structure based on the predicates. Also includes the level separators
	private LinkedList<Integer> subtreePredicateList;
	
	// contains the triple ids according to the predicate subtree structure. Does not include level separators
	//TODO: change type to LinkedList<int>
	private LinkedList<String> subtreeTripleList;
	private final int levelSeparator = -1;
	private boolean wasExtended;

	public Subtree() {
		this.subtreePredicateList = new LinkedList<Integer>();
		this.subtreeTripleList = new LinkedList<String>();
		this.wasExtended = false;
	}

	@SuppressWarnings("unchecked")
	private Subtree(LinkedList<Integer> subtreePredicateList,LinkedList<String>subtreeTripleList) {
		this.subtreePredicateList = (LinkedList<Integer>)subtreePredicateList.clone();
		this.subtreeTripleList = (LinkedList<String>)subtreeTripleList.clone();
		this.wasExtended = false;
	}

	public boolean wasExtended() {
		return this.wasExtended;
	}

	public void setExtended() {
		this.wasExtended = true;
	}

	public int[] toArray(){
		int[] subtreeArray=new int[subtreePredicateList.size()];
		for(int index=0;index<subtreePredicateList.size();index++){
			subtreeArray[index]=subtreePredicateList.get(index);
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
		this.subtreePredicateList=new LinkedList<Integer>();
		if(subtreeAsString.length()<2){
			return;
		}
		String[] subtreeAsStringSplit=subtreeAsString.split(",");
		for(String subtreeAsStringElement:subtreeAsStringSplit){
			subtreePredicateList.add(Integer.parseInt(subtreeAsStringElement));
		}
	}

	public List<Integer> toList(){
		return this.subtreePredicateList;
	}

	public void addBefore(int predicate, String tripleID) {
		this.subtreePredicateList.add(0, predicate);
		this.subtreePredicateList.add(this.levelSeparator);
		this.subtreeTripleList.add(0,tripleID);
	}
	
	public void addAfter(int predicate, String tripleID) {
		this.subtreePredicateList.add(predicate);
		this.subtreePredicateList.add(this.levelSeparator);
		this.subtreeTripleList.add(tripleID);
	}

	public void addTreeAfter(Subtree subtree) {
		this.subtreePredicateList.addAll(subtree.subtreePredicateList);
		this.subtreeTripleList.addAll(subtree.subtreeTripleList);
	}

	@Override
	public Subtree clone() {
        return new Subtree(this.subtreePredicateList, this.subtreeTripleList);
	}

	@Override
	public String toString() {
		String subtreeAsString="";
		for(int subtreeElement:subtreePredicateList){
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
			return this.subtreePredicateList.equals(((Subtree)obj).toList());
		}else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.subtreePredicateList.hashCode();
	}

	public String tripleIDsToString() {
		String tripleIDsString="";
		for(String tripleID:this.subtreeTripleList){
			if(tripleIDsString.length()>0){
				tripleIDsString+=",";
			}
			tripleIDsString+=tripleID;
		}
		return tripleIDsString;
	}
}
