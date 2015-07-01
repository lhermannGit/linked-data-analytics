package treeGeneration;

import java.util.ArrayList;
import java.util.List;

public class Rootnode implements Node{

	private int name;
	private List<ChildNode> children;
	private int priority;
	private int size;
	
	public Rootnode(int name){
		this.name = name;
		children = new ArrayList<ChildNode>();
		size = 0;
		priority = 100;
	}
	
	public int getName(){
		return name;
	}
	
	public int getPriority(){
		return priority;
	}
	
	public int getSize(){
		return size;
	}
	
	public List<ChildNode> getChildren(){
		return children;
	}
	
	public void addChild(ChildNode childNode){
		children.add(childNode);
		size++;
	}
	
	public void sucSize(){
		size++;
	}
	
	// look if subject already exists as an object in children
	// if found: create and add new ChildNode and add it to insertedNodes 
	public void addIfInside(int subject, int pred, int object, List<ChildNode> insertedNodes){
		if (this.name == subject) {
			ChildNode newNode = new ChildNode(object,pred);
			addChild(newNode);
			insertedNodes.add(newNode);
			// TODO: fstm.sendSubtree(newNode,this);
			System.out.println("added new Child "+ object + " with predicate "+ pred + " to rootnode "+ this.name);
		}
		
		for (ChildNode k: children){
			k.addIfInside(subject, pred, object, insertedNodes);
		}			
	}
	
	public void cloneTree(Rootnode clonedTree){
		ChildNode newNode;
		for (ChildNode child: children){
			newNode = new ChildNode(child.getName(), child.getPredicate());
			clonedTree.addChild(newNode);
			child.cloneTree(newNode);
		}

		System.out.println("cloned root "+ this.name);
	}
}
