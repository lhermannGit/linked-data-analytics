package de.unikoblenz.west.lda.subtreeBuilder;

import java.util.LinkedList;
import java.util.List;

public class TreeFilter {
	// Getting Path from TreeTraversal
	static String path = "1/2";

	// String path = getPath();

	// Querying Cache for Paths including just received Path

	List<String> list = new LinkedList<String>();

	public void queryCache(String path) {
		// first item is the new path
		list.add(path);
		// following items are the results from the cache
		if (!path.contains("/")){
			//return everything b/c we are looking for root node
			//Cache.query(Nil);
		} else
			path = path.substring(0, path.lastIndexOf("/"));
		System.out.println("TF: "+path);
		
		//query Cache for existing trees
		//Cache.query(path)
		
		//Adding results from Cache to List
		//list.addAll(Cache.query(ancestors)); 
		

		// handing created list over to TreeInserter
		handToTreeInserter(list);
	}

	// Giving found Subtrees and Path to the TreeInserter
	private void handToTreeInserter(List<String> list) {
		if (!list.isEmpty()) {
			TreeInserter ti = new TreeInserter();
			ti.createTrees(list);
		}
	}
	/*
	 * public List<String> getList(){ return list; }
	 */

	public static void main(String[] args) {
		TreeFilter tf = new TreeFilter();
		tf.queryCache(path);
	}
}