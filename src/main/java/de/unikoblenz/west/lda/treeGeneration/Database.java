package de.unikoblenz.west.lda.treeGeneration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * This class provide connection to MariaDB, store subtrees into DB and provide access to the DB data
 * Notes: - MariaDB should be set up and run [use port 3307, while port 3306 is occupied by MySQL DB]
 * 		  - "datamining" DB should be created in advance
 * 		  - in order to get access to DB create config.properties file
 * 		  - DB_URL = "jdbc:mysql://localhost:3307/datamining" - change it if needed
 *  	  - add to build Path MariaDBConnector: libs/mariadb-java-client-1.2.3.jar
 *  
 * @author Olga Zagovora <zagovora@uni-koblenz.de>
 */

public class Database {

	// JDBC driver name and database URL
	//static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static String DB_URL = "jdbc:mysql://localhost:3307/datamining";
	// database credentials
	static String dbName = "datamining";
	static String user = "root";
	static String password = "1234";
	private Connection connection;
	private Statement stmt;
	
	static String tableName="subtree_path";
	
	public Database(RootNode rootNode) {
		// initialize
		
		// commented because you need to have the config.properties file
		//MySQLConnectionInfo config = new MySQLConnectionInfo();
		//dbName=config.getDatabaseName();
		//user=config.getUser();
		//password=config.getPassword();
		// TODO Add DB_URL in config.properties

		// connect to the database 		
		try {
			connection = DriverManager.getConnection(DB_URL, user, password);
			stmt = connection.createStatement();

			// create two tables (one with RDF triples another with graph structure)
			this.createTables(tableName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// checks if a database table exists
	private boolean tableExists(String tableName) throws SQLException {
		String str="SELECT * FROM information_schema.tables "
				+ "WHERE table_schema = '"+dbName+"' AND table_name = '"+tableName+"' "
				+ "LIMIT 1;";
		ResultSet res = this.stmt.executeQuery(str);
		
		//get number of rows
		res.last();
		
		return res.getRow() > 0;
	}
	
	// creates necessary tables
	private void createTables(String tableName) throws SQLException {
		String sqlString;
		if  (!tableExists("Bags")){
			sqlString="CREATE TABLE Bags "
					+ " (BagID INT NOT NULL AUTO_INCREMENT,"
					+ " Name VARCHAR(255) NOT NULL,"
					+ " PRIMARY KEY (BagID) );";
			stmt.executeUpdate(sqlString);
			System.out.println("Table Bags was created.");
		} else
			System.out.println("No need to create table Bags (already exists).");
		
		if  (!tableExists(tableName)){
			sqlString="CREATE TABLE "+tableName+" "
					+ " (Id INT NOT NULL AUTO_INCREMENT,"
					+ " Crawl INT NULL,"
					+ " Bag INT NULL,"
					+ " StartLvl INT NOT NULL,"
					+ " EndLvl INT NOT NULL,"
					+ " Path VARCHAR(255) NOT NULL,"
					+ " PRIMARY KEY (Id) ,"
					+ " FOREIGN KEY (Bag) REFERENCES Bags (BagID) );";
			stmt.executeUpdate(sqlString);
			System.out.println("Table "+tableName+" is created!");
		} else
			System.out.println("No need to create table "+tableName+" (already exists).");
	}
	/*
	 * FOREIGN KEY (Bag) REFERENCES Bags(BagID)
	 * 
	 *  CREATE TABLE `datamining`.`new_table` (
	  `ID` INT NOT NULL AUTO_INCREMENT COMMENT '',
	  `Path` VARCHAR(45) NOT NULL COMMENT '',
	  `Bag` INT NULL COMMENT '',
	  `EndLvl` INT NOT NULL COMMENT '',
	  `Crawl` INT NULL COMMENT '',
	  `StartLvl` INT NOT NULL COMMENT '',
	  PRIMARY KEY (`ID`)  COMMENT '',
	  INDEX `BagID_idx` (`Bag` ASC)  COMMENT '',
	  CONSTRAINT `BagID`
	    FOREIGN KEY (`Bag`)
	    REFERENCES `datamining`.`Bags` (`BagID`)
	    ON DELETE NO ACTION
	    ON UPDATE NO ACTION);
	 */

	// runs any query string on the database
	public ResultSet anyQuery(String Pattern) {
		ResultSet result = null;
		try{
			result = this.stmt.executeQuery(Pattern);
		}
		catch(Exception x) {
			x.printStackTrace();
		}
		return result;
	}

	// returns all relevant subtrees and already create its new subtree via REGEXP_REPLACE
	public ResultSet getSubtrees(String regexp1, String regexp2, int newVal) {
		return this.anyQuery("SELECT StartLvl, EndLvl, Path, REGEXP_REPLACE(Path, '" + regexp2 + "', '\\\\0" + newVal + "\\(\\)') AS NewPath FROM subtree_path WHERE StartLvl = 0 AND Path REGEXP '" + regexp1 + "';");
	}
	
	// inserts a subtree into the database
	public boolean saveSubtree(int crawl, int bag, int startLvl, int endLvl, String path) {
		ResultSet rs = this.anyQuery("INSERT INTO subtree_path (Crawl, Bag, StartLvl, EndLvl, Path) VALUES (" + crawl + ", " + bag + ", " + startLvl + ", " + endLvl + ", '" + path + "');");
		return rs != null;
	}
	
	// closes the database connection
	public void close() throws SQLException {
		this.stmt.close();
		this.connection.close();
	}
}
