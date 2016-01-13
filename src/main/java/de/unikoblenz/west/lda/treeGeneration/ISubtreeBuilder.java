package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;

public interface ISubtreeBuilder {

	/* Sets the DB and Cache to be used by the SubtreeBuilder */
	public void Initialize(Database db);

	/*
	 * 1) queries the database for all relevant subtrees for the parameter path
	 * 2) builds new subtrees from the selected subtrees, adding the last connection in path
	 * 3) builds new subtrees with adjusted start level
	 */
	public void buildTrees(ArrayList<Integer> path, Database db);

}
