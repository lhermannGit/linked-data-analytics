package de.unikoblenz.west.lda.treeGeneration;

//import java.io.BufferedReader;
//import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class Window {
	public static final int WINDOW_SIZE = 100;

	private int size;
	private List<RootNode> rootNodes;
	private Iterator<RootNode> rootNodesIterator;

	/**
	 * Arguments: Path to input file
	 *
	 * @param args
	 * @throws IOException
	 */
	
	//public void createTrees(int[] rdfQuads) throws IOException {
	public static void main(String[] args) throws IOException {
		//input looks like [subject,predicate,object,count,......]
		
		//old test array
//		int[] testArray = new int[] {1,2,3,1, 	1,4,5,1, 	9,3,2,1,
//									 3,1,9,1,	1,9,6,1, 	11,12,13,1,
//									 13,1,14,1, 13,2,15,1,	6,3,2,1,
//									 14,16,2,1,	2,3,11,1,	13,3,7,1};
		int[] testArray = new int[] {1,99,3,1,	11,98,5,1,	9,97,2,1,
									 3,96,9,1,	1,95,6,1,	11,94,13,1,
									 13,93,14,1,13,92,15,1,	6,91,2,1,
									 14,90,2,1,	2,89,11,1,	13,87,7,1};
//		int[] testArray = new int[] {1,99,3,1,	3,98,5,1,	5,97,6,1,
//									 3,96,8,1	};

		Window window=new Window();
		List<RootNode>rootNodes=window.buildTree(testArray);
		for (RootNode rootNode : rootNodes) {
			System.out.println("\nTree structure:");
			TreePrinter.printTree(rootNode);
			List<Subtree>subtrees=window.extractSubtrees(rootNode);
			for(Subtree subtree:subtrees){
				System.out.println("-----");
				System.out.println(subtree);	
				System.out.println(subtree.tripleIDsToString());
			}
			System.out.println("Size of list: "+subtrees.size());
		}
	}
	public List<RootNode> buildTree(int[] inputArray){
		
		int rdfQuadsCount = 0;
		
		rootNodes = new ArrayList<RootNode>();
		List<ChildNode> preventLoop = new ArrayList<ChildNode>();
		int rdfSubject;
		int rdfObject;
		boolean inserted = false;
		RootNode newRootNode = null;
		ChildNode newChildNode = null;
		size = 0;
		while (rdfQuadsCount < inputArray.length) {
			// just for output/testing
			System.out.println("----- line read: " + rdfQuadsCount/4 + " -----");

			rdfSubject = inputArray[rdfQuadsCount];
			rdfObject = inputArray[rdfQuadsCount + 2];
			
			newChildNode = new ChildNode(rdfObject, inputArray[rdfQuadsCount+1], 
										inputArray[rdfQuadsCount+3]);
			// look through all nodes in all trees if new subject already 
			// exists as an object and if found, add newChildNode there
			for (RootNode rootnode : rootNodes) {
				if ((!inserted) & (rootnode.addIfInside(newChildNode, rdfSubject, preventLoop))){
					inserted = true;
				}
			}
			
			// if we inserted newChildNode only inside a tree with a rootNode.name == rdfObject,
			// we don't need/want to combine trees
			if (!inserted) {
				if (preventLoop.isEmpty()){
					// if newChildNode was not inserted yet, create new RootNode and
					// link newChildNode to it
					newRootNode = new RootNode(rdfSubject);
					rootNodes.add(newRootNode);
					newRootNode.addChildNode(newChildNode);
					System.out.println("added new Root " + rdfSubject);
					System.out.println("added new Child " + newChildNode.getName()
							+ " with predicate " + newChildNode.getPredicate()
							+ " to rootnode " + newRootNode.getName());
					size = size + 2;
					combineTrees(newChildNode, rdfObject, preventLoop);
				}					
			} 
			else {
				size++;
				combineTrees(newChildNode, rdfObject, preventLoop);
			}
			preventLoop.clear();
			rdfQuadsCount = rdfQuadsCount + 4;
			inserted = false;
		}
		//br.close();
		System.out.println("size = " + size);

		return this.rootNodes;
	}

	public List<Subtree> extractSubtrees(RootNode rootNode){
		System.out.println("\ngenerate subtrees:");

		LinkedHashSet<Subtree> subtrees = new LinkedHashSet<Subtree>();
		int numberOfSubtrees = 0;

		// test printouts for SubtreeExtraction
		SubtreeExtractor subtreeExtractor = new SubtreeExtractor(1);
		List<Subtree> extractedSubtrees = subtreeExtractor
				.extractSubtrees(rootNode);
		for (Subtree subtree : extractedSubtrees) {
			subtrees.add(subtree);
			//System.out.println(subtree.toString());
			numberOfSubtrees += 1;
		}
		System.out.println("Number of subtrees added: " + numberOfSubtrees);
		System.out.println("Number of unique subtrees: " + subtrees.size());
		
		
		List<Subtree>subtreesList=new ArrayList<Subtree>();
		subtreesList.addAll(subtrees);
		return subtreesList;
	
	}
	
	// combine a tree with added Nodes if needed
	public void combineTrees(ChildNode newChildNode, int rdfObject, 
									List<ChildNode> preventLoop) {
		RootNode root;
		rootNodesIterator = rootNodes.iterator();
		RootNode rootToDelete = null; //try to get rid of
		
		// look for rootNodes with the same name as rdfObject
		while (rootNodesIterator.hasNext()) {
			root = rootNodesIterator.next();
			if (rdfObject == root.getName()) {
				System.out.println("[start combining trees]");
				// remove links if needed to prevent loops
				for (ChildNode child: preventLoop){
					child.removeChildNode(newChildNode);				
					System.out.println("deleted link between ChildNode " + newChildNode.getName() 
							+ " and parent ChildNode " + child.getName());
				}
				// link all children of old rootNode to newChildNode and
				// delete old rootNode
				for (ChildNode child : root.getChildren()){
					newChildNode.addChildNode(child);
					System.out.println("linked child "+ child.getName()+ " to node " 
										+ newChildNode.getName());
				}
				rootToDelete = root;
			}
		}
		// TODO: find a better way to delete root inside 
		if (rootToDelete != null){
			System.out.println("deleted RootNode "+ rootToDelete.getName());
			rootNodes.remove(rootToDelete);
			rootToDelete = null;
		}
	}
}
