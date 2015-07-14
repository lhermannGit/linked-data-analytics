package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;
import java.util.List;

public class ChildNode implements Node {

	private int name;
	private int predicate;
	private int rdfCount;
	private List<ChildNode> children;

	// private Rootnode root; //really necessary??

	// public ChildNode(int name, int predicate, Rootnode root){
	public ChildNode(int name, int predicate, int count) {
		this.name = name;
		this.predicate = predicate;
		this.rdfCount = count;
		this.children = new ArrayList<ChildNode>();
		// this.root = root;
	}

	public int getName() {
		return this.name;
	}

	public int getPredicate() {
		return this.predicate;
	}

	public int getRdfCount(){
		return this.rdfCount;
	}
	public void addChild(ChildNode child) {
		this.children.add(child);
		// root.sucSize();
	}

	public List<ChildNode> getChildren() {
		return this.children;
	}

	// look if subject already exists as an object in children
	// if found: create and add new ChildNode and add it to insertedNodes
	public void addIfInside(int subject, int pred, int object,
			List<ChildNode> insertedNodes, int rdfCount) {
		if (this.name == subject) {
			ChildNode newNode = new ChildNode(object, pred, rdfCount);
			this.addChild(newNode);
			insertedNodes.add(newNode);
			System.out.println("added new Child " + object + " with predicate "
					+ pred + " to node " + this.name);
		}
		for (ChildNode k : this.children) {
			k.addIfInside(subject, pred, object, insertedNodes, rdfCount);
		}
	}

	// look if subject already exists as an object in children
	// if found: create and add new ChildNode; don't add it to insertedNodes to prevent loops
	public void addIfInside(int subject, int pred, int object, int rdfCount) {
		if (this.name == subject) {
			ChildNode newNode = new ChildNode(object, pred,rdfCount);
			this.addChild(newNode);
			System.out.println("added new Child " + object + " with predicate "
					+ pred + " to node " + this.name);
		}
		for (ChildNode k : this.children) {
			k.addIfInside(subject, pred, object, rdfCount);
		}
	}
	
	public void cloneTree(ChildNode clonedTree) {
		ChildNode newNode;
		for (ChildNode child : this.children) {
			newNode = new ChildNode(child.getName(), child.getPredicate(), child.getRdfCount());
			clonedTree.addChild(newNode);
			child.cloneTree(newNode);
		}

		System.out.println("cloned Childnode " + this.name);
	}
}
