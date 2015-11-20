package de.unikoblenz.west.lda.subtreeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TreeInserter 
{
	//not needed right now, as createTrees gets the full list of trees
	//Getting or querying found Subtrees and new Path from TreeFilter
	//List<String> list = TreeFilter.getList();
	
	//Finding correct Positions for new Path
	static List<String> newTrees = new ArrayList<String>();
	
	public static void createTrees (String path, List<String> list) {
		String parent = "";
        String nodeToAdd = "";
		if(path.contains("/")) {
			int index = path.lastIndexOf("/");
			parent = path.substring(0, index);
			nodeToAdd = path.substring(index+1);
			
		}
		else {
			parent = path;
			nodeToAdd = path;
		}
		//String regex = ("^" + "\\(*" + Pattern.quote(parent) + ".*");
		int index = 0;
		

		for(String s : list) {
			index = s.indexOf(parent);
			String part1 = s.substring(0, index + 1);
			
			if(part1.matches("^" +Pattern.quote(parent) + ".*")) {
			
			String part2 = s.substring(index + 1);
			String result = part1 + nodeToAdd + part2;
			newTrees.add(result);
			System.out.println(newTrees.toString());
			
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
		String path = "1/2/8/4/(7.2(2.1))";
		int index = path.lastIndexOf("/") ;
		String parent = path.substring(0, index);
		String child = path.substring(index+1);
		String s = "";
		
		System.out.println(parent);
		System.out.println(child);
		System.out.println(s.indexOf(parent));
		
	}
	
		
		
		}