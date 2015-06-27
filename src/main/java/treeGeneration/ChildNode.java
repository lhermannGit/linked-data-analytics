package treeGeneration;

import java.util.ArrayList;
import java.util.List;

public class ChildNode implements Node{

	private int name;
	private int predicate;
	private List<ChildNode> children;
	//private Rootnode root; //really necessary??
	
	
	//public ChildNode(int name, int predicate, Rootnode root){
	public ChildNode(int name, int predicate){
		this.name = name;
		this.predicate = predicate;
		children = new ArrayList<ChildNode>();
		//this.root = root;
	}
	
	public int getName(){
		return name;
	}
	
	public int getPredicate(){
		return predicate;
	}
	
	public void addChild(ChildNode child){
		children.add(child);
//		root.sucSize();
	}
	
	// not yet needed
	public List<ChildNode> getChildren(){
		return children;
	}

	// look if subject already exists as an object in children
	// if found: create and add new ChildNode and add it to insertedNodes 
	public void addIfInside(int subject, int pred, int object, List<ChildNode> insertedNodes){
		if (this.name == subject){
			ChildNode newNode = new ChildNode(object,pred);
			children.add(newNode);
			insertedNodes.add(newNode);
//			root.sucSize();
			// TODO: fstm.sendSubtree(newNode,root);
			System.out.println("added new Child "+ object + " with predicate "+ pred + " to node " + this.name);
		}
		for (ChildNode k: children){
			k.addIfInside(subject, pred, object, insertedNodes);
		}
	}
}

