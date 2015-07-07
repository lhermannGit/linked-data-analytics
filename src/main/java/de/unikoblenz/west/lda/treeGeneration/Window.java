package de.unikoblenz.west.lda.treeGeneration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Window {
	public static final int WINDOW_SIZE = 100;

	private static int size;
	private static List<Rootnode> rootnodes;
	private static Iterator<Rootnode> rootnodesIterator;

	public static void main(String[] args) throws IOException {

		int testcount = 0;

		rootnodes = new ArrayList<Rootnode>();
		List<ChildNode> insertedNodes = new ArrayList<ChildNode>();
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String line;
		String[] splitRDF;
		int rdfSubject;
		int rdfObject;
		Rootnode newRootnode;
		ChildNode newNode;
		size = 0;
		while ((line = br.readLine()) != null) {
			System.out.println("----- line read: " + testcount++ + " -----"); // just
																				// for
																				// output/testing
			splitRDF = line.split("\\s+");
			rdfSubject = Integer.parseInt(splitRDF[0]);
			rdfObject = Integer.parseInt(splitRDF[2]);

			// look through all nodes in all trees if new subject already exists
			// as an object and add it if found
			for (Rootnode rootnode : rootnodes) {
				rootnode.addIfInside(rdfSubject, Integer.parseInt(splitRDF[1]),
						rdfObject, insertedNodes);
			}

			// if node does not exist yet, create new Rootnode and ChildNode and
			// link them
			if (insertedNodes.isEmpty()) {
				newRootnode = new Rootnode(rdfSubject);
				rootnodes.add(newRootnode);
				newNode = new ChildNode(Integer.parseInt(splitRDF[2]),
						Integer.parseInt(splitRDF[1]));
				newRootnode.addChild(newNode);
				insertedNodes.add(newNode);
				System.out.println("added new Root " + rdfSubject);
				System.out.println("added new Child " + newNode.getName()
						+ " with predicate " + newNode.getPredicate()
						+ " to rootnode " + newRootnode.getName());
				size = size + 2;

				// TODO: fstm.sendSubtree(newNode, newRootnode)
			} else {
				// needs to be tested if size + 1 is close enough to actual size
				size++;
			}

			combineTrees(rdfObject, insertedNodes);

			if (size > WINDOW_SIZE) {
				// TODO: prune tree
			}
			insertedNodes.clear();
		}
		br.close();
		System.out.println("size = " + size);
	}

	// combine a tree with added Nodes if needed
	public static void combineTrees(int rdfObject, List<ChildNode> insertedNodes) {
		Rootnode root;
		rootnodesIterator = rootnodes.iterator();
		ChildNode existingSubject;
		Rootnode clonedTree;

		// look for rootnodes with the same name as rdfObject
		while (rootnodesIterator.hasNext()) {
			root = rootnodesIterator.next();
			if (rdfObject == root.getName()) {

				// clone only, if more than 1 Node was added this round
				while (!insertedNodes.isEmpty()) {
					existingSubject = insertedNodes.get(0);
					if (insertedNodes.size() > 1) {

						// clone tree, link children of old rootnode to new big
						// tree
						// and delete old rootnode
						clonedTree = new Rootnode(root.getName());
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

					// link children of old rootnode to new big tree and delete
					// old rootnode
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
						rootnodesIterator.remove();
						size--;
					}
					insertedNodes.remove(0);
				}
			}
		}
	}

}
