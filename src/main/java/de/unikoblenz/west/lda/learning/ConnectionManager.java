package de.unikoblenz.west.lda.learning;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

	/*
	 * Adjust the values of theese static fields, to point
	 * to the desired database!
	 */
	private static final String JDBCDriver = "com.mysql.jdbc.Driver";
	private static final String dbName = "DatabaseName";
	private static final String dbPassword = "DatabasePassword";
	private static final String dbUser = "DatabaseUser";
	
	
	
	public ConnectionManager(){
		
	}
	
	
	



	/**
	 * Returns a new to connection for the database specified within
	 * the state of the ConnectionManager
	 * 
	 * @return A new SQL-Database Connection
	 */
	public Connection getConnection(){
		
		Connection conn = null;
		try {
			Class.forName(JDBCDriver);
			conn = DriverManager.getConnection(dbName, dbUser, dbPassword);
		} catch (ClassNotFoundException e){
			System.err.println("Could not load JDBC Driver");
		} catch (SQLException e) {
			System.err.println("Connection to Database failed");
		}
		
		return conn;
		
	}

}
