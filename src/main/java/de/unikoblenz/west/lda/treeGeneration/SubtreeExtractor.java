package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SubtreeExtractor {

	private final String levelSeparator = "^";
	private final String itemSeparator = ",";

	@SuppressWarnings("unchecked")
	public List<String> extractSubtrees(RootNode rootNode) {
		ArrayList<String> extractedSubtrees = new ArrayList<String>();

		// initialize tree
		List<ChildNode> sortedChildren = this.sortByPredicate(rootNode
				.getChildren());
		List<LinkedHashSet<String>> extractedChildrenSubtrees = new ArrayList<LinkedHashSet<String>>();
		for (ChildNode child : sortedChildren) {
			// get children subtrees in separate lists
			extractedChildrenSubtrees.add(new LinkedHashSet<String>(this
					.extendSubtreeWithChildNode(
							(ArrayList<String>) extractedSubtrees.clone(),
							child)));
		}

		// we need this more complicated check because otherwise we would
		// aggregate different subtrees which do not have the same root
		// Remark: every node id should appear not more than once in a tree
		HashSet<Integer> extractedChildPredicates = new HashSet<Integer>();
		for (int childTreesIndex = 0; childTreesIndex < extractedChildrenSubtrees
				.size(); childTreesIndex++) {
			extractedChildPredicates.add(sortedChildren.get(childTreesIndex)
					.getPredicate());

			ArrayList<String> combinedExtractedSubtrees = new ArrayList<String>();
			for (String previousTree : extractedSubtrees) {
				// subtree contains root of original tree
				if (previousTree.contains(this.itemSeparator)
						&& this.containsRoot(extractedChildPredicates,
								previousTree)) {

					// add current subtrees that contain the root to previous
					// tree that contains the root
					for (String currentTree : extractedChildrenSubtrees
							.get(childTreesIndex)) {
						// subtree contains root of original tree
						if (currentTree.contains(this.itemSeparator)
								&& this.containsRoot(extractedChildPredicates,
										currentTree)) {
							combinedExtractedSubtrees.add(this.addAfter(
									previousTree, currentTree));
						}

					}

				}
			}
			extractedSubtrees.addAll(combinedExtractedSubtrees);
			for (String currentTree : extractedChildrenSubtrees
					.get(childTreesIndex)) {
				extractedSubtrees.add(currentTree);
			}
		}

		return extractedSubtrees;
	}

	private ArrayList<ChildNode> sortByPredicate(ArrayList<ChildNode> childNodes) {
		Collections.sort(childNodes, new ChildNodeComparator());
		return childNodes;
	}

	@SuppressWarnings("unchecked")
	private List<String> extendSubtreeWithChildNode(
			ArrayList<String> extractedSubtrees, ChildNode currentChildNode) {

		ArrayList<ChildNode> sortedChildren = (ArrayList<ChildNode>) this
				.sortByPredicate(currentChildNode.getChildren()).clone();

		// copy all lower level subtrees and add current child to them
		ArrayList<String> currentLevelSubtrees = new ArrayList<String>();
		if (sortedChildren.size() == 0) {
			// add predicate and levelSeparator after subtree
			for (String previousSubtree : extractedSubtrees) {
				previousSubtree = this.addAfter(previousSubtree,
						currentChildNode.getPredicate());

				previousSubtree = this.addAfter(previousSubtree,
						this.levelSeparator);
				currentLevelSubtrees.add(previousSubtree);
			}

		} else {
			// go one level deeper
			for (ChildNode child : sortedChildren) {
				extractedSubtrees.addAll(this.extendSubtreeWithChildNode(
						(ArrayList<String>) extractedSubtrees.clone(), child));
			}

			// add predicate before and levelSeparator after subtree
			for (String previousSubtree : extractedSubtrees) {
				previousSubtree = this.addBefore(previousSubtree,
						currentChildNode.getPredicate());

				previousSubtree = this.addAfter(previousSubtree,
						this.levelSeparator);
				currentLevelSubtrees.add(previousSubtree);
			}

		}
		extractedSubtrees.addAll(currentLevelSubtrees);

		// at new subtree
		String subtree = this.addAfter("", currentChildNode.getPredicate());
		subtree = this.addAfter(subtree, this.levelSeparator);

		extractedSubtrees.add(subtree);
		return extractedSubtrees;
	}

	private boolean containsRoot(Set<Integer> rootPredicateIds, String tree) {
		return rootPredicateIds.contains(Integer.parseInt(tree
				.split(this.itemSeparator)[0]));

	}

	private String addBefore(String subtree, int predicate) {
		return this.addBefore(subtree, String.valueOf(predicate));
	}

	private String addBefore(String subtree, String predicate) {
		if (!subtree.isEmpty()) {
			subtree = ',' + subtree;
		}
		subtree = predicate + subtree;
		return subtree;
	}

	private String addAfter(String subtree, int predicate) {
		return this.addAfter(subtree, String.valueOf(predicate));
	}

	private String addAfter(String subtree, String predicate) {
		if (!subtree.isEmpty()) {
			subtree = subtree + ',';
		}
		subtree = subtree + predicate;
		return subtree;
	}
}
