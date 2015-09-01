package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;
import java.util.List;

public class RootNode implements Node {

	private int name;
	private List<ChildNode> children;
//	private int priority;

	public RootNode(int name) {
		this.name = name;
		this.children = new ArrayList<ChildNode>();
//		this.priority = 100;
	}

	public int getName() {
		return this.name;
	}

//	public int getPriority() {
//		return this.priority;
//	}

	public List<ChildNode> getChildren() {
		return this.children;
	}

	public void addChildNode(ChildNode childNode) {
		if (!this.children.contains(childNode)){
			this.children.add(childNode);
			System.out.println("added new Child " + childNode.getName() + " with predicate "
				+ childNode.getPredicate() + " to rootnode " + this.name);			
		}
	}
	
	public void removeChildNode(ChildNode childNode){
		this.children.remove(childNode);
	}

	// look if subject already exists as an object in this tree
	// if found: add newChildNode as a child
	public boolean addIfInside(ChildNode newChildNode, int rdfSubject,
								List<ChildNode> preventLoop){		
		boolean inserted = false;
		if (this.name == rdfSubject) {
			this.addChildNode(newChildNode);
			inserted = true;
		}
		if (this.name == newChildNode.getName()){			
			//if newChildNode is added in this tree, parent ChildNode needs to 
			//add itself to preventLoop
			for (ChildNode child : this.children) {
				if ((!inserted) & (child.addIfInside(newChildNode, rdfSubject, preventLoop))){
					inserted = true;
				}
			}
		}
		else {			
			//no need to prevent this kind of loops
			for (ChildNode child : this.children) {
				if ((!inserted) & (child.addIfInside(newChildNode, rdfSubject))){
					inserted = true;
				}
			}
		}
		return inserted;
	}

}
