package de.unikoblenz.west.lda.treeGeneration;

import java.util.List;

// interface to be implemented by Cache and DB
public interface Storage {

	public List<Subtree> query(String path);

	public void save(List<Subtree> subtrees);

}
