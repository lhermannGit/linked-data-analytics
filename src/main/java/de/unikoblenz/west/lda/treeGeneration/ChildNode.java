package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;

public class ChildNode implements Node {

	private int name;
	private int predicate;
	private ArrayList<ChildNode> children;

	// private Rootnode root; //really necessary??

	// public ChildNode(int name, int predicate, Rootnode root){
	public ChildNode(int name, int predicate) {
		this.name = name;
		this.predicate = predicate;
		this.children = new ArrayList<ChildNode>();
		// this.root = root;
	}

	public int getName() {
		return this.name;
	}

	public int getPredicate() {
		return this.predicate;
	}

	public void addChild(ChildNode child) {
		this.children.add(child);
		// root.sucSize();
	}

	// not yet needed
	public ArrayList<ChildNode> getChildren() {
		return this.children;
	}

	// look if subject already exists as an object in children
	// if found: create and add new ChildNode and add it to insertedNodes
	public void addIfInside(int subject, int pred, int object,
			ArrayList<ChildNode> insertedNodes) {
		if (this.name == subject) {
			ChildNode newNode = new ChildNode(object, pred);
			this.addChild(newNode);
			insertedNodes.add(newNode);
			// TODO: fstm.sendSubtree(newNode,root);
			System.out.println("added new Child " + object + " with predicate "
					+ pred + " to node " + this.name);
		}
		for (ChildNode k : this.children) {
			k.addIfInside(subject, pred, object, insertedNodes);
		}
	}

	public void cloneTree(ChildNode clonedTree) {
		ChildNode newNode;
		for (ChildNode child : this.children) {
			newNode = new ChildNode(child.getName(), child.getPredicate());
			clonedTree.addChild(newNode);
			child.cloneTree(newNode);
		}

		System.out.println("cloned Childnode " + this.name);
	}
}
