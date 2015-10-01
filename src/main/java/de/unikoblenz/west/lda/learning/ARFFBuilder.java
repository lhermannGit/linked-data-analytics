package de.unikoblenz.west.lda.learning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;



public class ARFFBuilder {
	
	private static final String database = "test";
	private static final String dbpassword = "test";
	private static final String user = "LinkedDataAnalytics";
	private static final int treeMarker = Integer.MIN_VALUE;
	private static final String JDBCDriver = "com.mysql.jdbc.Driver";
	
	private int num_of_coloumns = 0;
	
	

	public String createFile(int[] subtree, String tableName) throws IOException {
		
		tableName = tableName.toLowerCase();
		String fileName = tableName + ".arff";
		String[] comment = createComment(tableName);
		LinkedList<String> header = createHeader(subtree);
		num_of_coloumns = header.size();
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		String sub = java.util.Arrays.toString(subtree);
		
		for(String line : comment){
			out.write(line);
			out.newLine();
		}
		
		out.write("% " + sub);
		out.newLine();
		out.write("@relation '" + tableName + "'");
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

	/*
	 * This method creates the header section of an ARFF-File.
	 * 
	 * @Param subtree: The array representation of an subtree
	 */
	private LinkedList<String> createHeader(int[] subtree){
		
		LinkedList<String> header = new LinkedList<String>();
		
		for(int i=0; i<subtree.length; i++){
			
			if(i == 0)
				header.add(i,"@attribute Root" + subtree[i] + " string");
			
			if(i != 0 && subtree[i] != treeMarker)
				header.add(i, "@attribute Child" + subtree[i] + " string");
		}
		return header;
	}
	
	
	private LinkedList<String> readInstances(String tableName){
		
		LinkedList<String> rows = new LinkedList<String>();
		Connection conn = getConnection();
		
		try {
			
			Statement stm = conn.createStatement();
			String query = "SELECT (*) FROM " + tableName;
			ResultSet rs = stm.executeQuery(query);
			
			while(rs.next()){
				String row = "";
				for(int i=0; i<num_of_coloumns;i++){
					if(i != num_of_coloumns - 1)
						row = row + "'" + rs.getString(i) + "'" + ", ";
					else
						row = row + "'" + rs.getString(i) + "'";
				}
				rows.add(row);
			}
			
			stm.close();
			conn.close();
			
		} catch (SQLException e) {
			
			System.err.println("Reading of Instances failed");
		} 
		
		return rows;
	}

	private Connection getConnection() {
		Connection conn = null;
		
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(database, user, dbpassword);
			
		} catch (ClassNotFoundException e){
			System.err.println("Could not load JDBC Driver");
		} catch (SQLException e) {
			System.err.println("Connection to Database failed");
		}
		
		return conn;
		
	}
	
}
