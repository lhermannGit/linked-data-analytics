package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for extracting subtrees from a given rootNode
 *
 * @author Martin Koerner <info@mkoerner.de>
 *
 */
public class SubtreeExtractor {

	private int minimumCount;
	
	public SubtreeExtractor(int minimumCount){
		this.minimumCount=minimumCount;
	}

	public List<Subtree> extractSubtrees(RootNode rootNode) {
		List<Subtree> extractedSubtrees = new ArrayList<Subtree>();

		if (rootNode == null) {
			return extractedSubtrees;
		}
		List<ChildNode> sortedChildren = this.sortByPredicate(rootNode
				.getChildren());

		for (ChildNode child : sortedChildren) {
			List<Subtree> currentSubtrees = this.extendSubtreeWithChildNode(
					new ArrayList<Subtree>(), child);
			List<Subtree> combinedSubtrees = new ArrayList<Subtree>();
			for (Subtree currentSubtree : currentSubtrees) {
				if (!currentSubtree.wasExtended()) {
					for (Subtree extractedSubtree : extractedSubtrees) {
						if (!extractedSubtree.wasExtended()) {
							Subtree combinedSubtree = extractedSubtree.clone();
							combinedSubtree.addTreeAfter(currentSubtree);
							combinedSubtrees.add(combinedSubtree);
						}
					}
				}
			}
			extractedSubtrees.addAll(currentSubtrees);
			extractedSubtrees.addAll(combinedSubtrees);
		}

		return extractedSubtrees;
	}

	private List<ChildNode> sortByPredicate(List<ChildNode> childNodes) {
		Collections.sort(childNodes, new ChildNodeComparator());
		return childNodes;
	}

	private List<Subtree> extendSubtreeWithChildNode(
			List<Subtree> extendedSubtrees, ChildNode currentChildNode) {

		List<ChildNode> sortedChildren = this.sortByPredicate(currentChildNode
				.getChildren());

		// copy all lower level subtrees and add current child to them
		// go one level deeper
		for (ChildNode child : sortedChildren) {
			extendedSubtrees.addAll(this.extendSubtreeWithChildNode(
					this.cloneList(extendedSubtrees), child));
		}

		if(currentChildNode.getRdfCount()>=this.minimumCount){
			// add predicate to subtrees which have not been extended
			List<Subtree> currentLevelSubtrees = new ArrayList<Subtree>();
			for (Subtree previousSubtree : extendedSubtrees) {
				if (!previousSubtree.wasExtended()) {
					Subtree extendedSubtree = previousSubtree.clone();
					previousSubtree.setExtended();
					if (sortedChildren.size() == 0) {
						extendedSubtree.addAfter(currentChildNode.getPredicate());
					} else {
						extendedSubtree.addBefore(currentChildNode.getPredicate());
					}

					currentLevelSubtrees.add(extendedSubtree);
				}

			}
			extendedSubtrees.addAll(currentLevelSubtrees);

			// at new subtree with predicate of current child
			Subtree subtree = new Subtree();
			subtree.addAfter(currentChildNode.getPredicate());

			extendedSubtrees.add(subtree);
		} else {
			// set all wasExtended in all extendedSubtrees because they
			// can not be further extended since the count for this
			// predicate is too low
			for (Subtree previousSubtree : extendedSubtrees) {
				if (!previousSubtree.wasExtended()) {
					previousSubtree.setExtended();
				}
			}
		}
		return extendedSubtrees;
	}

	private List<Subtree> cloneList(List<Subtree> subtrees) {
		List<Subtree> clone = new ArrayList<Subtree>();
		for (Subtree subtree : subtrees) {
			clone.add(subtree.clone());
		}
		return clone;
	}
}
