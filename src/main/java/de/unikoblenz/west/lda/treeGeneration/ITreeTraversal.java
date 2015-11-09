package de.unikoblenz.west.lda.treeGeneration;

public interface ITreeTraversal {

	public void Initialize(RootNode rootNode);

	// Returns null if all paths are exhausted
	public String getNextPath();
}
