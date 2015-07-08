package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;
import java.util.List;

public class RootNode implements Node {

	private int name;
	private List<ChildNode> children;
	private int priority;
	private int size;

	public RootNode(int name) {
		this.name = name;
		this.children = new ArrayList<ChildNode>();
		this.size = 0;
		this.priority = 100;
	}

	public int getName() {
		return this.name;
	}

	public int getPriority() {
		return this.priority;
	}

	public int getSize() {
		return this.size;
	}

	public List<ChildNode> getChildren() {
		return this.children;
	}

	public void addChild(ChildNode childNode) {
		this.children.add(childNode);
		this.size++;
	}

	public void sucSize() {
		this.size++;
	}

	// look if subject already exists as an object in children
	// if found: create and add new ChildNode and add it to insertedNodes
	public void addIfInside(int subject, int pred, int object,
			List<ChildNode> insertedNodes) {
		if (this.name == subject) {
			ChildNode newNode = new ChildNode(object, pred);
			this.addChild(newNode);
			insertedNodes.add(newNode);
			// TODO: fstm.sendSubtree(newNode,this);
			System.out.println("added new Child " + object + " with predicate "
					+ pred + " to rootnode " + this.name);
		}

		for (ChildNode k : this.children) {
			k.addIfInside(subject, pred, object, insertedNodes);
		}
	}

	public void cloneTree(RootNode clonedTree) {
		ChildNode newNode;
		for (ChildNode child : this.children) {
			newNode = new ChildNode(child.getName(), child.getPredicate());
			clonedTree.addChild(newNode);
			child.cloneTree(newNode);
		}

		System.out.println("cloned root " + this.name);
	}
}