package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;

interface Node {

	public int getName();

	public ArrayList<ChildNode> getChildren();

	// public void addNodesToList(int node, List<Node> result);
	public void addChild(ChildNode childNode);

	public void addIfInside(int subject, int pred, int object,
			ArrayList<ChildNode> insertedNodes);

}
