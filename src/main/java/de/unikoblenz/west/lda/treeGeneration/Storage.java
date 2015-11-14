package de.unikoblenz.west.lda.treeGeneration;

import java.util.List;


class MySubTree {
    int startLvl;
    int endLvl;
    String path;

    public MySubTree(int startLvl,int endLvl, String path) {
        this.startLvl = startLvl;
        this.endLvl = endLvl;
        this.path = path;
    }
}


public interface Storage {
	
	public List<MySubTree> query (int endLvl);
	
	
	/*
	 * store only path, startLvl and endlvl
	 */
	
	public void save (List <MySubTree> subTrees);
	
	
	/*
	 * store all fields in the Table including bag and crawl id
	 */
	
	public void save (List <MySubTree> subTrees, int crawl, int bag);
}
