package de.unikoblenz.west.lda.subtreeBuilder;

import java.util.List;

public class TreeFilter {

	// Getting Path from TreeTraversal
	String path = "";
	// String path = getPath();

	// Querying Cache for Paths including just received Path

	List<String> list = null;

	public void queryCache(String path) {
		// first item is the new path
		list.add(path);
		// following items are the results from the cache

		// handing created list over to TreeInserter
		handToTreeInserter(list);
	}

	// Giving found Subtrees and Path to the TreeInserter
	private void handToTreeInserter(List<String> list) {
		if (list != null) {
			TreeInserter.createTrees(list);
		}
	}
	/*
	 * public List<String> getList(){ return list; }
	 */
}
