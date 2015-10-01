package de.unikoblenz.west.lda.learning;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Statistician {
	
	private ConnectionManager conManager;
	
	private static final String rdfType = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	
	public Statistician(){
		conManager = new ConnectionManager();
	}
	
	/**
	 * This methods computes the count of the different classes
	 * that the RDF-Type predicate ranges over in a given pattern.
	 * 
	 * @param tableName - The table's name of the pattern
	 * @return The computed count for each RDF-Class found.
	 */
	public Map<String, Integer> countRDFTypes(String tableName) {
		Map<String, Integer> result = new HashMap();
		Connection conn = conManager.getConnection();

		try {
			Statement stm = conn.createStatement();
			String query = "SELECT (*) " + "FROM " + tableName;
			ResultSet rs = stm.executeQuery(query);
			int columns = rs.getMetaData().getColumnCount();
			boolean next;

			while (next = rs.next()) {
				if (!next)
					break;

				for (int i = 0; i < (columns - 1); i++) {
					String predicate = rs.getString(i);
					if (predicate.equals(rdfType)) {
						String typeClass = rs.getString(i + 1);
						if (result.containsKey(typeClass)) {
							int value = result.get(typeClass);
							result.put(typeClass, value + 1);

						} else
							result.put(typeClass, 1);

					}

				}

			}

		} catch (SQLException e) {
			System.err
					.println("Exception occured during exection of the Method countRDFTypes in Statistician, "
							+ "while trying to get the instances for "
							+ tableName);
		}

		return result;
	}

}
