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
        //int startLvl = 0;
        //int endLvl = 0;
		if(path.contains("/")) {
			int index = path.lastIndexOf("/");
			parent = path.substring(0, index);
			nodeToAdd = path.substring(index+1);
			
			
			
			//endLvl = endLvl + 1; 
			
		}
		//else {
			//parent = path;       // if path doesn't contain a "/", his ancestor is the root node.
			//nodeToAdd = path;
			
			//startLvl = path.lastIndexOf("/");
			//endLvl = startLvl + 1;
			
			
			
		
		//String regex = ("^" + "\\(*" +Pattern.quote(parent) + ".*");
		int index = 0;
		String part1 = "";
		String part2 = "";
		String result = "";
		

		for(String s : list) {
			index = s.indexOf(parent);
			
			//check the level to add the new path.
			part1 = s.substring(0, index + 1);
			part2 = s.substring(index + 1);
			if(part1.matches("^" +Pattern.quote(parent) + ".*")) {
				
			 result = part1 +"/" + nodeToAdd + part2;
				newTrees.add(result);
				System.out.println(newTrees.toString());
				
				
			}
			else {
				part1 = s +".";
				result = part1 +nodeToAdd;
				newTrees.add(result);
				//newTrees.add(nodeToAdd);
				System.out.println(nodeToAdd);
				System.out.println(part1);
				System.out.println(result);
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
		String path = "1/1/8/4/7";
		List<String> list = new ArrayList<String>();
		list.add("1/1/8/4.2.3.5");
		list.add("3");
		list.add("1");
		list.add("2");
		
		
		createTrees(path, list);
		
		
		
	}
	
		
		
		}