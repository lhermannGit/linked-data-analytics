package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeTraversal implements ITreeTraversal {

	public TreeTraversal(){
		queueNodes = new LinkedList<Node>();
		queuePairs = new LinkedList<ArrayList<Integer>>();
		queuePath = new LinkedList<ArrayList<Integer>>();
		children = new LinkedList<ChildNode>();
		currentPath = new ArrayList<Integer>();
	}
	
	private Queue<Node> queueNodes;
	private Queue<ArrayList<Integer>> queuePath;
	private Queue<ArrayList<Integer>> queuePairs;
	private List<ChildNode> children;
	private ArrayList<Integer> currentPath;
	
	
	public void Initialize(RootNode rootNode) {
		queueNodes.clear();
	    queuePath.clear();
	    queuePairs.clear();
	    children.clear();
		queueNodes.add(rootNode);
		queuePath.clear();
	    currentPath.clear();
	}

	public ArrayList<Integer> getNextPath() {
		ArrayList<Integer> helper;
		if (queuePairs.isEmpty()){
			while(true){
				if (queueNodes.isEmpty()){
					return null;
				}
				children = queueNodes.remove().getChildren();
				if (!queuePath.isEmpty())
					currentPath = queuePath.remove();
				else
					currentPath = null;
				if (children.size()==0)
					continue;
				for (int n=0; n<children.size(); n++){
					queueNodes.add(children.get(n));
					if (currentPath == null){
						helper = new ArrayList<Integer>();
					}
					else{					
						helper = new ArrayList<Integer>(currentPath);
					}
					helper.add(n);
					queuePath.add(helper);
					queuePairs.add(helper);
				}
				break;
			}
		}
		return queuePairs.remove();
	}
}
