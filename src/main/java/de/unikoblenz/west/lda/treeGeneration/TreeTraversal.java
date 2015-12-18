package de.unikoblenz.west.lda.treeGeneration;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeTraversal implements ITreeTraversal {

	public TreeTraversal(){
		queueNodes = new LinkedList<Node>();
		queuePairs = new LinkedList<String>();
		queuePath = new LinkedList<String>();
		children = new LinkedList<ChildNode>();
	}
	
	private Queue<Node> queueNodes;
	private Queue<String> queuePath;
	private Queue<String> queuePairs;
	private List<ChildNode> children;
	private String currentPath;
	
	
	public void Initialize(RootNode rootNode) {
		queueNodes.clear();
	    queuePath.clear();
	    queuePairs.clear();
	    children.clear();
		queueNodes.add(rootNode);
		queuePath.add("");
	    currentPath = "";
	}

	public String getNextPath() {
		if (queuePairs.isEmpty()){
			while(true){
				if (queueNodes.isEmpty()){
					return null;
				}
				children = queueNodes.remove().getChildren();
				currentPath = queuePath.remove();
				if (children.size()==0)
					continue;
				for (int n=0; n<children.size(); n++){
					queueNodes.add(children.get(n));
					queuePath.add(currentPath + n + "/");
					queuePairs.add(currentPath + n + "/");
				}
				break;
			}
		}
		return queuePairs.remove();
	}

}
