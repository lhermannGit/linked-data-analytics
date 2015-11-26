package de.unikoblenz.west.lda.subtreeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeInserterE {

	static List<String> newPaths = new ArrayList<String>();
	
	public static void addPaths (String path, List<String> list){
		String child = "";
		String parent = "";
		if (path.contains("/")){
			int index = path.lastIndexOf("/");
			parent = path.substring(0, index);
			child = path.substring(index+1);
			
		}
		
		list.add("(1/1/1/4).2((1.(2/1))).3(1.2)");
		int index = 0;
		
		String regex = ("^" + "\\(*" + Pattern.quote(parent) + ".*");
		for (String entry : list){
			//insertChild(entry,regex);
			//need to find index of last digit, or the one after
			index = entry.indexOf(parent);
			//System.out.println(index);
			String part1 = entry.substring(0,index+1);
			String part2 = entry.substring(index+1);
			
			//best case
			String res = part1 + child + part2;
			newPaths.add(res);
			System.out.println(part1);
			System.out.println(child);
			System.out.println(part2);
			System.out.println(res);
			
		}
		
			    //String nodeToAdd = path.substring(path.length()-1);
			//System.out.println("TI - Path: "+path);
			//System.out.println("TI - Liste: "+list.toString());
			
		}
	
	
	public static void insertChild(String text, String regex) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    // Check all occurrences
	    while (matcher.find()) {
	       // System.out.print("Start index: " + matcher.);
	      
	    }
	}

			
		
	
	
	
	
	
	public static void main(String[] args) {
		String path = "1/1/6";
		List<String> list = new ArrayList<String>();
	
		addPaths(path, list);
		//System.out.println("end");
		
	}
	
	
	
}