package de.unikoblenz.west.lda.treeGeneration;

import java.util.List;


class MySubTree {
    int startLvl;
    String path;

    public MySubTree(int startLvl, String path) {
        this.startLvl = startLvl;
        this.path = path;
    }
}


public interface Storage {
	
	public List<MySubTree> query (String searchPath);
	
	
	/*
	 * store only path and startLvl
	 */
	
	public void save (List <MySubTree> subTrees);
	
	
	/*
	 * store all fields in the Table including bag and crawl id
	 */
	
	public void save (List <MySubTree> subTrees, int crawl, int bag, int endLvl);
}
