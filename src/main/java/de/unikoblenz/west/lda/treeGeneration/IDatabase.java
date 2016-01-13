package de.unikoblenz.west.lda.treeGeneration;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDatabase {

	public void Initialize(RootNode rootNode);

	public ResultSet anyQuery(String Pattern);

	public ResultSet getSubtrees(String regexp1, String regexp2, int newVal);
	
	public boolean saveSubtree(int crawl, int bag, int startLvl, int endLvl, String path);
	
	public void close() throws SQLException;
}
