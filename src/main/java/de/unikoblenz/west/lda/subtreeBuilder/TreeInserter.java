package de.unikoblenz.west.lda.subtreeBuilder;

import java.util.List;

public class TreeInserter {
	static //not needed right now, as createTrees gets the full list of trees
	//Getting or querying found Subtrees and new Path from TreeFilter
	//List<String> list = TreeFilter.getList();
	
	//Finding correct Positions for new Path
	String parent;
	
	//Generate new subtrees containing the new path.
	
	public static void createTrees (String path, List<String> list){
		String nodeToAdd = path.substring(path.length()-1);
		for(String s : list) {
			if(!s.contains("/"))  {
			s = insertNodeToTree(nodeToAdd);
		}
		else {
			s = s.substring(0, s.lastIndexOf("/"));
			s = insertNodeToTree(nodeToAdd);
		}
		}   
			
			//if(s.matches("^" +Pattern.quote(path) + ".*"))  
			    
			System.out.println("TI - Path: "+path);
			System.out.println("TI - Liste: "+list.toString());
			
	}
	
	//  add path to existing subtrees.
	public static String insertNodeToTree(String path) {
		if(!path.contains("/")) {
			 String nodeToAdd = path.substring(path.length()-1);
			
		}
		
			
				return null;
	
	
		
	}
			
		
	
	
	
	//Discarding found Subtrees and saving newly created ones
	
	
	public static void main(String[] args) {
		String path = "1(/2/8/4)7";
		String s = path.substring(0, path.lastIndexOf("/"));
		
		System.out.println(s);
		System.out.println(path.substring(path.length()-1));
	}
	
	
	
	//Discarding found Subtrees and saving newly created ones
	
}