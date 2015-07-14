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

	private static int size;
	private static List<RootNode> rootNodes;
	private static Iterator<RootNode> rootNodesIterator;

	/**
	 * Arguments: Path to input file
	 *
	 * @param args
	 * @throws IOException
	 */
	
	//public static void main(int[] rdfQuads) throws IOException {
	public static void main(String[] args) throws IOException {
		//input looks like [subject,predicate,object,count,......]
		int[] testArray = new int[] {1,2,3,0, 	1,4,5,0, 	9,3,2,0,
									 3,1,9,0,	1,9,6,0, 	11,12,13,0,
									 13,1,14,0, 13,2,15,0,	6,3,2,0,
									 14,16,2,0,	2,3,11,0,	13,3,7,0};
		
		int rdfQuadsCount = 0;
		
		rootNodes = new ArrayList<RootNode>();
		List<ChildNode> insertedNodes = new ArrayList<ChildNode>();
		//BufferedReader br = new BufferedReader(new FileReader(args[0]));
		//String line;
		//String[] splitRDF;
		int rdfSubject;
		int rdfObject;
		RootNode newRootNode = null;
		ChildNode newNode;
		size = 0;
		while (rdfQuadsCount < testArray.length) {
			// just for output/testing
			System.out.println("----- line read: " + rdfQuadsCount/4 + " -----");

			rdfSubject = testArray[rdfQuadsCount];
			rdfObject = testArray[rdfQuadsCount + 2];

			// look through all nodes in all trees if new subject already
			// exists as an object and add it if found
			for (RootNode rootnode : rootNodes) {
				rootnode.addIfInside(rdfSubject, testArray[rdfQuadsCount+1], rdfObject, insertedNodes, testArray[rdfQuadsCount+3] );
			}

			// if node does not exist yet, create new RootNode and ChildNode and
			// link them
			if (insertedNodes.isEmpty()) {
				newRootNode = new RootNode(rdfSubject);
				rootNodes.add(newRootNode);
				newNode = new ChildNode(rdfObject,testArray[rdfQuadsCount+1], testArray[rdfQuadsCount+3]);
				newRootNode.addChild(newNode);
				insertedNodes.add(newNode);
				System.out.println("added new Root " + rdfSubject);
				System.out.println("added new Child " + newNode.getName()
						+ " with predicate " + newNode.getPredicate()
						+ " to rootnode " + newRootNode.getName());
				size = size + 2;

			} else {
				// needs to be tested if size + 1 is close enough to actual size
				size++;
			}

			combineTrees(rdfObject, insertedNodes);

			insertedNodes.clear();
			rdfQuadsCount = rdfQuadsCount + 4;
		}
		//br.close();
		System.out.println("size = " + size);

		for (RootNode rootNode : rootNodes) {
			System.out.println("\nTree structure:");
			TreePrinter.printTree(rootNode);
		}

		System.out.println("\ngenerate subtrees:");

		LinkedHashSet<String> subtrees = new LinkedHashSet<String>();
		int numberOfSubtrees = 0;
		for (RootNode rootNode : rootNodes) {
			// test printouts for SubtreeExtraction
			SubtreeExtractor subtreeExtractor = new SubtreeExtractor();
			List<Subtree> extractedSubtrees = subtreeExtractor
					.extractSubtrees(rootNode);
			for (Subtree subtree : extractedSubtrees) {
				subtrees.add(subtree.toString());
				System.out.println(subtree);
				numberOfSubtrees += 1;
			}
		}
		System.out.println("Number of Rootnodes: " + rootNodes.size());
		System.out.println("Number of subtrees added: " + numberOfSubtrees);
		System.out.println("Numbre of unique subtrees: " + subtrees.size());

	}

	// combine a tree with added Nodes if needed
	public static void combineTrees(int rdfObject, List<ChildNode> insertedNodes) {
		RootNode root;
		rootNodesIterator = rootNodes.iterator();
		ChildNode existingSubject;
		RootNode clonedTree;

		// look for rootNodes with the same name as rdfObject
		while (rootNodesIterator.hasNext()) {
			root = rootNodesIterator.next();
			if (rdfObject == root.getName()) {

				// clone only, if more than 1 Node was added this round
				while (!insertedNodes.isEmpty()) {
					existingSubject = insertedNodes.get(0);
					if (insertedNodes.size() > 1) {

						// clone tree, link children of old rootNode to new big
						// tree
						// and delete old rootNode
						clonedTree = new RootNode(root.getName());
						root.cloneTree(clonedTree);
						for (ChildNode oldTreeChild : clonedTree.getChildren()) {
							existingSubject.addChild(oldTreeChild);
							System.out.println("linked child "
									+ oldTreeChild.getName()
									+ " to existing node "
									+ existingSubject.getName());
						}
						System.out.println("deleted Root "
								+ clonedTree.getName());
						clonedTree = null;
						size--;
					}

					// link children of old rootNode to new big tree and delete
					// old rootNode
					else {
						for (ChildNode oldTreeChild : root.getChildren()) {
							existingSubject.addChild(oldTreeChild);
							System.out.println("linked child "
									+ oldTreeChild.getName()
									+ " to existing node "
									+ existingSubject.getName());
						}
						System.out.println("deleted last root "
								+ root.getName());
						rootNodesIterator.remove();
						size--;
					}
					insertedNodes.remove(0);
				}
			}
		}
	}
}
