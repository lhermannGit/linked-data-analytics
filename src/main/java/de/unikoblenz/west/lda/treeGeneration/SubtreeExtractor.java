package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubtreeExtractor {

	private final char levelSeparator = '^';

	public static void main(String[] args) {

	}

	public List<String> extractSubtrees(RootNode rootNode) {
		List<String> extractedSubtrees = new ArrayList<String>();
		// HashSet to store all nodes for which all subtrees have been created

		// extract all subtrees with rootNode as root
		extractedSubtrees.addAll(extractSubtreesWithRoot(rootNode));
		List<ChildNode> sortedChildNodes = sortByPredicate(rootNode
				.getChildren());
		for (ChildNode childNode : sortedChildNodes) {
			// extract all subtrees with childNode as root
			extractedSubtrees.addAll(extractSubtreesWithRoot(childNode));
		}
		return extractedSubtrees;
	}

	private List<ChildNode> sortByPredicate(List<ChildNode> childNodes) {
		Collections.sort(childNodes, new ChildNodeComparator());
		return childNodes;
	}

	private List<String> extractSubtreesWithRoot(Node root) {
		List<String> extractedSubtrees = new ArrayList<String>();

		// initialize tree
		String subtree = "";
		List<ChildNode> sortedChildren = sortByPredicate(root.getChildren());
		for (ChildNode child : sortedChildren) {
			// extract all subtrees with child as root
			extractedSubtrees.addAll(extractSubtreesWithRoot(child));

			extractedSubtrees
					.addAll(extendSubtreeWithChildNode(subtree, child));
		}

		return extractedSubtrees;
	}

	private List<String> extendSubtreeWithChildNode(String subtree,
			ChildNode childNode) {
		List<String> extractedSubtrees = new ArrayList<String>();
		List<ChildNode> sortedChildren = sortByPredicate(childNode
				.getChildren());
		addPredicate(subtree, childNode.getPredicate());
		subtree = addPredicate(subtree, childNode.getPredicate());
		extractedSubtrees.add(subtree);
		for (ChildNode child : sortedChildren) {
			extendSubtreeWithChildNode(subtree, child);

		}

		return extractedSubtrees;
	}

	private String addPredicate(String subtree, int predicate) {
		if (!subtree.isEmpty()) {
			subtree += ',';
		}
		subtree += predicate;
		return subtree;
	}
}
