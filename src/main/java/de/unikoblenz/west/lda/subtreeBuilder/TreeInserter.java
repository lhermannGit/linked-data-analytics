package de.unikoblenz.west.lda.subtreeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeInserter {
	//not needed right now, as createTrees gets the full list of trees
	//Getting or querying found Subtrees and new Path from TreeFilter
	//List<String> list = TreeFilter.getList();
	
	//Finding correct Positions for new Path
	
	public static void createTrees (String path, List<String> list){
		
		
		for(String s : list) {
			
			if(s.matches("^" +Pattern.quote(path) + ".*"))  {
				List<String> newSubtrees = new ArrayList<String>();
			    newSubtrees.add(s);
			}
			    //String nodeToAdd = path.substring(path.length()-1);
			System.out.println("TI - Path: "+path);
			System.out.println("TI - Liste: "+list.toString());
			
		}
	}
	
	
	
	public static String insertNodeToTree(String path, String tree, int depth) {
		
		String nodeToAdd = path.substring(path.length()-1);
		return null;
	}
			
		
	
	
	
	//Discarding found Subtrees and saving newly created ones
	
	
	public static void main(String[] args) {
		String path0 = "1/2";
		List <String> relevantPath = new ArrayList<String>();
		String[] paths = {"1/4/6/7", "2/4/8.3", "1.2(3.9)"};
		for( String path : paths)
			relevantPath.add(path);
		createTrees(path0, relevantPath);
		
		for(String s : relevantPath) {
		System.out.println(s);
		
		System.out.println(s.substring(s.length()-1));

		//System.out.println("end");
		
	}
	
	
	
}}