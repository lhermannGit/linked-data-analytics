package de.unikoblenz.west.lda.subtreeBuilder;

import java.util.List;

public class TreeInserter {
	//not needed right now, as createTrees gets the full list of trees
	//Getting or querying found Subtrees and new Path from TreeFilter
	//List<String> list = TreeFilter.getList();
	
	//Finding correct Positions for new Path
	public void createTrees (String path, List<String> list){
		System.out.println("TI - Path: "+path);
		System.out.println("TI - Liste: "+list.toString());
		
	}
	
	//Discarding found Subtrees and saving newly created ones
	
}
