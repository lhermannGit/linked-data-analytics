package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;
import java.util.List;

public interface ISubtreeBuilder {

	/* Sets the DB and Cache to be used by the SubtreeBuilder */
	public void Initialize(Database db, Cache cache);

	/*
	 * 1) Queries the Cache/DB for all relevant Subtrees for the parameter path
	 * 2) Filter the returned Subtrees for the actually needed Subtrees 3) Build
	 * the new Subtrees, adding the last connection in path 4) Return all new
	 * Subtrees
	 */
	public List<Subtree> buildTrees(ArrayList<Integer> path);

}
