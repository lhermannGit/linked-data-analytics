package de.unikoblenz.west.lda.treeGeneration;

import java.util.List;

interface Node {

	public int getName();
	public List<ChildNode> getChildren();
	public void addChildNode(ChildNode childNode);
	public void removeChildNode(ChildNode child);

	public boolean addIfInside(ChildNode newChildNode, int rdfSubject, List<ChildNode> preventLoop);
	

}
