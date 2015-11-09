package de.unikoblenz.west.lda.subtreeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TreeFilter {
	// Getting Path from TreeTraversal
	static String path = "1/2/2/2/2/2";

	// String path = getPath();

	// Querying Cache for Paths including just received Path

	List<String> relevantTrees = new ArrayList<String>();
	String parent = "";

	public void queryCache(String path) {

		// getting results from the cache
		if (!path.contains("/")) {
			// Query Cache with "parent" matched
			List<String> queriedPaths = new ArrayList<String>();
			// queriedPaths = Cache.query(path)

			for (String queried : queriedPaths) {
				//have to split by points on highest level
				String[] split = queried.split(".");
				for (String splitQuery : split) {
					if (splitQuery.matches("^" + "\\(*" + Pattern.quote(parent) + ".*")) {
						relevantTrees.add(queried);
					}
				}
			}

			handToTreeInserter(relevantTrees);
		} else {
			path = path.substring(0, path.lastIndexOf("/"));
			// if parent does not exist, create one time
			if (parent.isEmpty())
				parent = path;

			// query Cache for new path
			List<String> queriedPaths = new ArrayList<String>();
			// queriedPaths = Cache.query(path)

			for (String queried : queriedPaths) {
				if (queried.matches("^" + "\\(*" + Pattern.quote(parent) + ".*")) {
					relevantTrees.add(queried);
				}
			}

			System.out.println("TF - Path: " + path);
			// System.out.println("TF - Parent: "+parent);
			queryCache(path);
		}

	}

	public String transformPath(String path) {
		String ret = "";
		String[] split = path.split("/");
		for (int i = split.length - 1; i >= 0; i--) {
			if (i == split.length - 1)
				ret = split[i] + ".";
			else
				ret = split[i] + "(" + ret + ")";
		}
		// System.out.println("TF - Path Transformed: "+ret);
		return ret;
	}

	// Giving found Subtrees and Path to the TreeInserter
	private void handToTreeInserter(List<String> list) {
		TreeInserter ti = new TreeInserter();
		// transforming into alternative coding
		transformPath(path);
		// System.out.println("TF - Liste: "+list.toString());
		ti.createTrees(path, list);
	}

	/*
	 * public List<String> getList(){ return list; }
	 */

	public static void main(String[] args) {
		TreeFilter tf = new TreeFilter();
		tf.queryCache(path);
		// String bla = "((((1/1/3/2))))))))";
		// String parent = "1/1";
		// System.out.println(bla.matches("^" + "\\(*" + Pattern.quote(parent) +
		// ".*"));
	}
}