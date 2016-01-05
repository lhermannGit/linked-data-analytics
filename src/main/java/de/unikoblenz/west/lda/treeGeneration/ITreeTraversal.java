package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;

public interface ITreeTraversal {

	public void Initialize(RootNode rootNode);

	// Returns null if all paths are exhausted
	public ArrayList<Integer> getNextPath();
}
