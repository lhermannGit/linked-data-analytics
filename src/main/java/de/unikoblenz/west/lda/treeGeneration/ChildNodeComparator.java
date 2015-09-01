package de.unikoblenz.west.lda.treeGeneration;

import java.util.Comparator;

public class ChildNodeComparator implements Comparator<ChildNode> {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	// compares childNodes based on their predicates
	public int compare(ChildNode o1, ChildNode o2) {
		// ascending order of predicates
		return o1.getPredicate() - o2.getPredicate();
	}
}
