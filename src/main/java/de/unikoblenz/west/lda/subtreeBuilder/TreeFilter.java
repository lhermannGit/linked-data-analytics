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
		
		// getting results results from the cache
		if (!path.contains("/")){
			//return everything b/c we are looking for root node
			//Cache.query(Nil);
		} else
			path = path.substring(0, path.lastIndexOf("/"));
		System.out.println("TF - Path: "+path);
		
		//query Cache for existing trees
		//Cache.query(path)
		
		//Adding results from Cache to List
		//list.addAll(Cache.query(ancestors)); 
		
		
		// handing created list over to TreeInserter
		handToTreeInserter(list);
	}
	
public String transformPath(String path)  {
        String ret = "";
        String[] split = path.split("/");
        for (int i = split.length - 1; i >= 0; i--) {
                if (i == split.length - 1)
                        ret = split[i] + ".";
                else
                        ret = split[i] + "(" + ret + ")";
        }
        System.out.println("TF - Path Transformed: "+ret);
        return ret;
}

	// Giving found Subtrees and Path to the TreeInserter
	private void handToTreeInserter(List<String> list) {
			TreeInserter ti = new TreeInserter();
			//transforming into alternative coding
			transformPath(path);
			System.out.println("TF - Liste: "+list.toString());
			ti.createTrees(path, list);
	}
	
	/*
	 * public List<String> getList(){ return list; }
	 */

	public static void main(String[] args) {
		TreeFilter tf = new TreeFilter();
		tf.queryCache(path);
	}
}