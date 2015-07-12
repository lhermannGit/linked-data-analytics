package de.unikoblenz.west.lda.treeGeneration;

import java.util.List;

interface Node {

	public int getName();

	public List<ChildNode> getChildren();

	// public void addNodesToList(int node, List<Node> result);
	public void addChild(ChildNode childNode);

	public void addIfInside(int subject, int pred, int object,
			List<ChildNode> insertedNodes, int rdfCount);

}
