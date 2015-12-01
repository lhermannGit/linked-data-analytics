package treeGeneration;

public class Tuple<startLvl, parent, current> {
	
    public int startLvl;
    public String parent;
    public String current;    
    
    public Tuple(int startLvl, String parent, String current) {
    	this.startLvl = startLvl;
    	this.parent = parent;
    	this.current = current;
    }
}