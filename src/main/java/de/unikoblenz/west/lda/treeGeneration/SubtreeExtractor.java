package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.storm.http.impl.SocketHttpServerConnection;

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

	public List<Subtree> extractSubtrees(RootNode rootNode) {
		List<Subtree> extractedSubtrees = new ArrayList<Subtree>();

		if (rootNode == null) {
			return extractedSubtrees;
		}
		List<ChildNode> sortedChildren = this.sortByPredicate(rootNode
				.getChildren());

		for (ChildNode child : sortedChildren) {
			List<Subtree> currentSubtrees = this.extendSubtreeWithChildNode(
					new ArrayList<Subtree>(), child,rootNode.getName());

			//combining the trees on the top level results in an exponential number of trees which is not feasible for us
			
			//List<Subtree> combinedSubtrees = new ArrayList<Subtree>();
			////combine subtrees that contain the root node
			//for (Subtree currentSubtree : currentSubtrees) {
			//	if (!currentSubtree.wasExtended()) {
			//		for (Subtree extractedSubtree : extractedSubtrees) {
			//			if (!extractedSubtree.wasExtended()&&extractedSubtree.getNumberOfPredicates()+
			//					currentSubtree.getNumberOfPredicates()<=maximumSize) {
			//				
			//				
			//				
			//				Subtree combinedSubtree = extractedSubtree.clone();
			//				combinedSubtree.addTreeAfter(currentSubtree);
			//				combinedSubtrees.add(combinedSubtree);
			//			}
			//		}
			//	}
			//}
			//System.out.println("extracted: "+extractedSubtrees.size());
			//System.out.println("combined: "+ combinedSubtrees.size());
			//extractedSubtrees.addAll(combinedSubtrees);

			extractedSubtrees.addAll(currentSubtrees);

		}

		return extractedSubtrees;
	}

	private List<ChildNode> sortByPredicate(List<ChildNode> childNodes) {
		Collections.sort(childNodes, new ChildNodeComparator());
		return childNodes;
	}

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

		if(currentChildNode.getRdfCount()>=this.minimumCount){
			// add predicate to subtrees which have not been extended
			List<Subtree> currentLevelSubtrees = new ArrayList<Subtree>();
			for (Subtree previousSubtree : extendedSubtrees) {
				if (!previousSubtree.wasExtended()&&previousSubtree.getNumberOfPredicates()<maximumSize) {
					Subtree extendedSubtree = previousSubtree.clone();
					previousSubtree.setExtended();
					//get tripleID
					String tripleID=this.getTripleID(parentName, currentChildNode.getPredicate(),currentChildNode.getName());
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
