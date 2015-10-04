package de.unikoblenz.west.lda.learning;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Statistician {
	
	private ConnectionManager conManager;
	private static final String rdfType = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	
	public Statistician(){
		conManager = new ConnectionManager();
	}
	
	/**
	 * This methods computes the count of the different classes
	 * that the RDF-Type predicate ranges over in a SINGLE given pattern.
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
			
			conn.close();

		} catch (SQLException e) {
			System.err
					.println("Exception occured during exection of the Method countRDFTypes in Statistician, "
							+ "while trying to get the instances for "
							+ tableName);
		} 

		return result;
	}
	
	/**
	 * This method computes the occurences of the different classes
	 * that the RDF-Type predicate ranges over in multiple patterns.
	 * Using this method with a List with only one pattern in it, makes
	 * the 1.countRDFTypes method unnecessary
	 * 
	 * @param tableNames - the table names of the different pattern in a db
	 * @return result - the computed occurences of the different classes across
	 * theese patterns
	 */
	public Map<String, Integer> countRDFTypes(List<String> tableNames) {
		Map<String, Integer> result = new HashMap();
		Connection conn = conManager.getConnection();

		try {
			for (String tableName : tableNames) {
				Statement stm = conn.createStatement();
				String query = "SELECT (*) FROM " + tableName;
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
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;

	}
	
	/**
	 * This method computes the occurrences of all the different classes
	 * within the provided patterns. It then computes the likelihood for
	 * the occurrence of a class following an RDF-Type predicate.
	 * Example: In the computed results you have the entry ("rdf/Student",0.4),
	 * this means that with a likelihood of 0.4 an RDF class student will always appear
	 * after an RDF type predicate. The sum of all the likelihood values should be 1.
	 * 
	 * @param tableNames - The table names of the patterns to analyze in a db
	 * @return The computed distribution across the patterns.
	 */
	public Map<String,Double> getDistribution(List<String> tableNames){
		Map<String,Double> distribution = new HashMap<String,Double>();
		Map<String,Integer> values = this.countRDFTypes(tableNames);
		Set<Entry<String,Integer>> mappings = values.entrySet();
		double total = 0;
		
		for(Entry e: mappings){
			total += (Integer)e.getValue();
		}
		
		for(Entry e: mappings){
			Integer v = (Integer)e.getValue();
			Double d = total / v;
			String key = (String)e.getKey();
			distribution.put(key, d);
			
		}
		
		return distribution;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
