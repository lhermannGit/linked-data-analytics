package de.unikoblenz.west.lda.subtreeBuilder;

import java.util.ArrayList;
import java.util.List;

public class TreeInserterAlternative {
	//not needed right now, as createTrees gets the full list of trees
	//Getting or querying found Subtrees and new Path from TreeFilter
	//List<String> list = TreeFilter.getList();
	
	//Finding correct Positions for new Path
	public List<String> createTrees (String path, List<String> list){
		System.out.println("TI - Path: "+path);
		System.out.println("TI - Liste: "+list.toString());
		
		List<String> newSubtrees = new ArrayList<String>();
		for(String l: list)
			newSubtrees.add(insertNodeToTree(path, l, 4));
		return newSubtrees;
	}
	
	//Discarding found Subtrees and saving newly created ones
	
	public static void main(String[] args) {
		String newSubtree = insertNodeToTree("1/3/5", "1(3(6.7.))2(4.)", 4);	// 1(3(>5.<6.7.))2(4.)
		System.out.println(newSubtree);
	}

	public static String insertNodeToTree(String path, String tree, int depth) {
		System.out.println(createRegex(path, depth));
		return "";
	}
	
	public static String createRegex(String path, int depth)  {
		String reg = "";
		String[] nested = new String[depth];
		for (int i = nested.length - 1; i >= 0; i--)
			if (i == nested.length - 1)
	            reg = "[[^\\.]+.]+";					// [Number.]+
	        else
	        	reg = "[[^\\.]+.]+"						// [Number.]+
	        		+ "|"								// OR
	        		+ "[[^\\)]+\\(" + reg + "\\)]*";	// [Number(...)]*
		System.out.println(reg);
		String ret = "";
		String[] split = path.split("/");
		for (int i = split.length - 1; i >= 0; i--)
			if (i == split.length - 1)
	            ret = split[i] + ".";
	        else
	        	ret = split[i] + "(" + ret + ")";
	    //System.out.println("TF - Path Transformed: "+ret);
	    return ret;
	}

}
