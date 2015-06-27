package treeGeneration;

import java.util.List;

interface Node {
	//public void addNodesToList(int node, List<Node> result);
	public void addChild(ChildNode childNode);
	public int getName();
	public void addIfInside(int subject, int pred, int object, List<ChildNode> insertedNodes);
}
