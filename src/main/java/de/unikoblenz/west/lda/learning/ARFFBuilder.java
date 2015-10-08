package de.unikoblenz.west.lda.learning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;



public class ARFFBuilder {
	
	private ConnectionManager conMan;
	
	public ARFFBuilder(){
		conMan = new ConnectionManager();
	}
	

	public String createFile(int[] subtree, String tableName) throws IOException {
		
		tableName = tableName.toLowerCase();
		String fileName = tableName + ".arff";
		String[] comment = createComment(tableName);
		LinkedList<String> header = createHeader(tableName);
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		
		
		for(String line : comment){
			out.write(line);
			out.newLine();
		}
		
		out.write("@relation " + tableName);
		out.newLine();
		
		for(String line : header){
			out.write(line);
			out.newLine();
		}
		
		out.write("@data");
		out.newLine();
		
		LinkedList<String> data = readInstances(tableName);
		
		for(String line: data){
			out.write(line);
			out.newLine();
		}
		
		out.close();
		
		return fileName;
		
	}
	
	/**
	 *  This method creates an fixed section of comments for each file.
	 *  There is only single alternation in the comment section between different 
	 *  ARFF-Files, namely the table name of the subtree.
	 *  
	 *  @param tableName: the name of the subtree in a Database
	 */
	private String[] createComment(String tableName) {
		
		String[] comment = new String[9];
		
		comment[0] = "%---------------------------------------------------";
		comment[1] = "% Title: " + tableName + " ARFF-File";
		comment[2] = "%";
		comment[3] = "% Source: Linked Data Analytics";
		comment[4] = "%";
		comment[5] = "% Usage: This file is used for further processing";
		comment[6] = "%        with the WEKA-Data mining tools";
		comment[7] = "%";
		comment[8] = "%---------------------------------------------------";
		
		return comment;
	}

	/**
	 * This method creates the header section of an ARFF-File. It basically creates
	 * one attribute for each column in the ARFF-File. The type of the attribute is
	 * always numeric.
	 * 
	 * @Param tableName: the name of a table in a database for a pattern
	 * @return returns the header of an ARFF-File as a List of Strings
	 */
	private LinkedList<String> createHeader(String tableName){
		
		LinkedList<String> header = new LinkedList<String>();
		Connection con = conMan.getConnection();
		try {
			Statement stm = con.createStatement();
			String query = "SELECT (*) FROM " + tableName;
			ResultSet rs = stm.executeQuery(query);
			ResultSetMetaData mD = rs.getMetaData();
			int count = mD.getColumnCount();
			int i = 0;
			int j = 1;
			
			while(i < count){
				String subject = "@attribute Subject" + j + " numeric";
				String predicate = "@attribute Predicate" + j + " numeric";
				String object = "@attribute Object" + j + " numeric";
				
				header.add(subject);
				header.add(predicate);
				header.add(object);
				
				i += 3;
				j++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return header;
	}
	
	/**
	 * Reads instances for a specific pattern out of a database and
	 * creates the data section of the ARFF-File, which is basically
	 * a comma seperated list of values.
	 * 
	 * @param tableName
	 * @return
	 */
	private LinkedList<String> readInstances(String tableName) {

		LinkedList<String> rows = new LinkedList<String>();
		Connection conn = conMan.getConnection();
		try {
			Statement stm = conn.createStatement();
			String query = "SELECT (*) FROM " + tableName;
			ResultSet rs = stm.executeQuery(query);
			ResultSetMetaData mD = rs.getMetaData();
			int count = mD.getColumnCount();

			while (rs.next()) {

				String row = new String();
				row = "";
				for (int i = 0; i < count; i++) {

					int value = rs.getInt(i);

					if (!(i == count - 1)) {
						row = row + value + ",";
					} else {
						row = row + value;
					}

					rows.add(row);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rows;
	}

}
