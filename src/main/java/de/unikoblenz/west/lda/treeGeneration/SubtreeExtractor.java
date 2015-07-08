package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class SubtreeExtractor {

	public List<Subtree> extractSubtrees(RootNode rootNode) {
		List<Subtree> extractedSubtrees = new ArrayList<Subtree>();

		if (rootNode == null) {
			return extractedSubtrees;
		}
		// initialize tree
		List<ChildNode> sortedChildren = this.sortByPredicate(rootNode
				.getChildren());
		List<LinkedHashSet<Subtree>> extractedChildrenSubtrees = new ArrayList<LinkedHashSet<Subtree>>();
		for (ChildNode child : sortedChildren) {
			// get children subtrees in separate lists
			extractedChildrenSubtrees.add(new LinkedHashSet<Subtree>(this
					.extendSubtreeWithChildNode(
							this.cloneList(extractedSubtrees), child)));
		}

		// we need this more complicated check because otherwise we would
		// aggregate different subtrees which do not have the same root
		// Remark: every node id should appear not more than once in a tree
		HashSet<Integer> extractedChildPredicates = new HashSet<Integer>();
		for (int childTreesIndex = 0; childTreesIndex < extractedChildrenSubtrees
				.size(); childTreesIndex++) {
			extractedChildPredicates.add(sortedChildren.get(childTreesIndex)
					.getPredicate());

			List<Subtree> combinedExtractedSubtrees = new ArrayList<Subtree>();
			for (Subtree previousTree : extractedSubtrees) {
				// subtree contains root of original tree
				if (previousTree.hasRoot(extractedChildPredicates)) {

					// add current subtrees that contain the root to previous
					// tree that contains the root
					for (Subtree currentTree : extractedChildrenSubtrees
							.get(childTreesIndex)) {
						// subtree contains root of original tree
						if (currentTree.hasRoot(extractedChildPredicates)) {
							// create new subtree as combination
							Subtree combinedSubtree = previousTree.clone();
							combinedSubtree.addTreeAfter(currentTree);
							combinedExtractedSubtrees.add(combinedSubtree);
						}

					}

				}
			}
			// add combined subtrees
			extractedSubtrees.addAll(this.cloneList(combinedExtractedSubtrees));

			// add current subtrees

			for (Subtree currentTree : extractedChildrenSubtrees
					.get(childTreesIndex)) {
				extractedSubtrees.add(currentTree.clone());
			}
		}

		return extractedSubtrees;
	}

	private List<ChildNode> sortByPredicate(List<ChildNode> childNodes) {
		Collections.sort(childNodes, new ChildNodeComparator());
		return childNodes;
	}

	private List<Subtree> extendSubtreeWithChildNode(
			List<Subtree> extractedSubtrees, ChildNode currentChildNode) {

		List<ChildNode> sortedChildren = this.sortByPredicate(currentChildNode
				.getChildren());

		// copy all lower level subtrees and add current child to them
		List<Subtree> currentLevelSubtrees = new ArrayList<Subtree>();
		if (sortedChildren.size() == 0) {
			// add predicate and levelSeparator after subtree
			for (Subtree previousSubtree : extractedSubtrees) {
				Subtree extendedSubtree = previousSubtree.clone();
				extendedSubtree.addAfter(currentChildNode.getPredicate());

				currentLevelSubtrees.add(extendedSubtree);
			}

		} else {
			// go one level deeper
			for (ChildNode child : sortedChildren) {
				extractedSubtrees.addAll(this.extendSubtreeWithChildNode(
						this.cloneList(extractedSubtrees), child));
			}

			// add predicate before and levelSeparator after subtree
			for (Subtree previousSubtree : extractedSubtrees) {
				Subtree extendedSubtree = previousSubtree.clone();
				extendedSubtree.addBefore(currentChildNode.getPredicate());

				currentLevelSubtrees.add(extendedSubtree);
			}

		}
		extractedSubtrees.addAll(currentLevelSubtrees);

		// at new subtree
		Subtree subtree = new Subtree();
		subtree.addAfter(currentChildNode.getPredicate());

		extractedSubtrees.add(subtree);
		return extractedSubtrees;
	}

	private List<Subtree> cloneList(List<Subtree> subtrees) {
		List<Subtree> clone = new ArrayList<Subtree>();
		for (Subtree subtree : subtrees) {
			clone.add(subtree.clone());
		}
		return clone;
	}
}
