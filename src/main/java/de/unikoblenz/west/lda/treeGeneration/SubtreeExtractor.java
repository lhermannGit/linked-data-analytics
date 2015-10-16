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
	private int maximumSize;
	
	/**
	 * 
	 * @param minimumCount; minimum frequency of predicates in the original dataset
	 * @param maximumSize: maximum number of predicates in created subtrees
	 */
	public SubtreeExtractor(int minimumCount,int maximumSize){
		this.minimumCount=minimumCount;
		this.maximumSize=maximumSize;
	}

	/**
	 * construct a list of subtrees from a given rootNode using the specifications that are set in the constructor
	 * @param rootNode
	 * @return
	 */
	public List<Subtree> extractSubtrees(RootNode rootNode) {
		List<Subtree> extractedSubtrees = new ArrayList<Subtree>();

		if (rootNode == null) {
			return extractedSubtrees;
		}

		// Sort children by predicate to ensure a unambiguous tree representation
		List<ChildNode> sortedChildren = this.sortByPredicate(rootNode
				.getChildren());

		for (ChildNode child : sortedChildren) {
			// recursively create all subtrees of a child and its children
			List<Subtree> currentSubtrees = this.extendSubtreeWithChildNode(
					new ArrayList<Subtree>(), child,rootNode.getName());



			//TODO: combining the trees on the top level results in an exponential number of trees which is not feasible for us
			List<Subtree> combinedSubtrees = new ArrayList<Subtree>();

			// iterate over currentSubtrees which is the list of subtrees created following child
			for (Subtree currentSubtree : currentSubtrees) {
				// only consider trees which are not fully extended yet (and thereby include a link to the rootNode)
				if (!currentSubtree.wasExtended()) {
					// iterate over all trees that were previously created
					for (Subtree extractedSubtree : extractedSubtrees) {
						// only consider trees which are not fully extended yet and where the resulting tree would not be bigger than maximumSize
						if (!extractedSubtree.wasExtended()&&extractedSubtree.getNumberOfPredicates()+
								currentSubtree.getNumberOfPredicates()<=maximumSize) {
							// clone subtree to prevent an overwriting later on
							// TODO: is this cloning necessary?
							Subtree combinedSubtree = extractedSubtree.clone();

							// combine the two trees
							combinedSubtree.addTreeAfter(currentSubtree);
							combinedSubtrees.add(combinedSubtree);
						}
					}
				}
			}
//			System.out.println("extracted: "+extractedSubtrees.size());
//			System.out.println("combined: "+ combinedSubtrees.size());
			extractedSubtrees.addAll(combinedSubtrees);

			extractedSubtrees.addAll(currentSubtrees);

		}

		return extractedSubtrees;
	}

	/**
	 * this method is called by extractSubtrees or by itself. It generates the subtrees including the currentChildNode by
	 * considering its predicate count
	 * @param extendedSubtrees
	 * @param currentChildNode
	 * @param parentName
	 * @return
	 */
	private List<Subtree> extendSubtreeWithChildNode(
			List<Subtree> extendedSubtrees, ChildNode currentChildNode,int parentName) {

		List<ChildNode> sortedChildren = this.sortByPredicate(currentChildNode
				.getChildren());

		// copy all lower level subtrees and add current child to them
		// go one level deeper
		for (ChildNode child : sortedChildren) {
			extendedSubtrees.addAll(this.extendSubtreeWithChildNode(
					this.cloneList(extendedSubtrees), child, currentChildNode.getName()));
		}

		// only add currentChildNode if the predicate count is at least as big as minimumCount
		if(currentChildNode.getRdfCount()>=this.minimumCount){
			List<Subtree> currentLevelSubtrees = new ArrayList<Subtree>();
			for (Subtree previousSubtree : extendedSubtrees) {
				// add predicate to subtrees which have not been extended and the previousSubtree is not bigger than maximumSize
				if (!previousSubtree.wasExtended()&&previousSubtree.getNumberOfPredicates()<maximumSize) {
					Subtree extendedSubtree = previousSubtree.clone();
					// set the wasExtended flag in the previousSubtree since it was extended by childNode
					previousSubtree.setExtended();

					// get tripleID for storing it in the data base
					String tripleID=this.getTripleID(parentName, currentChildNode.getPredicate(),currentChildNode.getName());
					
					// where to add the currentChildNode depends on if currentChildNode has children or not
					if (sortedChildren.size() == 0) {
						extendedSubtree.addAfter(currentChildNode.getPredicate(),tripleID);
					} else {
						extendedSubtree.addBefore(currentChildNode.getPredicate(),tripleID);
					}

					currentLevelSubtrees.add(extendedSubtree);
				}

			}
			extendedSubtrees.addAll(currentLevelSubtrees);

			// at new subtree with predicate of current child
			Subtree subtree = new Subtree();
			String tripleID=this.getTripleID(parentName, currentChildNode.getPredicate(),currentChildNode.getName());
			subtree.addAfter(currentChildNode.getPredicate(),tripleID);
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

	private List<ChildNode> sortByPredicate(List<ChildNode> childNodes) {
		Collections.sort(childNodes, new ChildNodeComparator());
		return childNodes;
	}

	private String getTripleID(int subject, int predicate, int object) {
		//TODO: check local list if triple was already added and if not, call mysql function and store triple ID in local list
		String result=subject+" "+predicate+" "+object;
		return result;
	}

	private List<Subtree> cloneList(List<Subtree> subtrees) {
		List<Subtree> clone = new ArrayList<Subtree>();
		for (Subtree subtree : subtrees) {
			clone.add(subtree.clone());
		}
		return clone;
	}
}
