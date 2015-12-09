package de.unikoblenz.west.lda.treeGeneration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.unikoblenz.west.lda.input.MySQLConnectionInfo;

/*
 * This class provide connection to MariaDB, store subtrees into DB and provide access to the DB data
 * Notes: - MariaDB should be set up and run [use port 3307, while port 3306 is occupied by MySQL DB ]
 * 		  - "datamining" DB should be created in advance
 * 		  - in order to get access to DB create config.properties file
 * 		  - DB_URL = "jdbc:mysql://localhost:3307/datamining" - change it if needed
 *  	  - Add to build Path MariaDBConnector: libs/mariadb-java-client-1.2.3.jar
 *  
 * @author Olga Zagovora <zagovora@uni-koblenz.de>
 */


/*adding data into DB:
 * 
 * 
 * StoreToMariaDB x=new StoreToMariaDB();
 * 
 * List <MySubTree> subTrees= new ArrayList<MySubTree>();
 * subTrees.add(new MySubTree (0,0,"12.(1345).34"));
 * subTrees.add(new MySubTree (0,0,"12(167).34"));
 * x.save( subTrees, 1, 1); //subTrees, int crawl, int bag
 * OR: x.save(subTrees);
 * x.close();
 */

/*
 * Perform the Update query[for Adrian's implementation]:
 * 
 * StoreToMariaDB x=new StoreToMariaDB();
 * String YourQuery="";//add necessary query
 * x.updateQuery(YourQuery); 
 *  x.close();
 */

/*steps for retrieving data from DB:
 * 
 * 
 * 
 * StoreToMariaDB x=new StoreToMariaDB();
 * 
 * String SqlString="SELECT * FROM subtree_path WHERE Path LIKE \"12.(1%\" ;";
 * 
 * List <MySubTree> subTrees = x.query (SqlString); //String Query
 * 
 * 
 * for (MySubTree iterator : subTrees) {
 * System.out.println(iterator.path);}
 * 
 * x.close();
 * 
 */


public class StoreToMariaDB implements Storage {

	// JDBC driver name and database URL
	//static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static String DB_URL = "jdbc:mysql://localhost:3307/datamining";
	//  Database credentials
	static String dbName = "datamining";
	static String user = "root";
	static String passwort = "1234";
	private Connection connection;
	private Statement stmt;
	
	static String TableName="subtree_path";
	
	public StoreToMariaDB() throws SQLException{
		
		//Initialize
		
		//commented while you need to have the config.properties file
		//MySQLConnectionInfo config = new MySQLConnectionInfo();
		//dbName=config.getDatabaseName();
		//user=config.getUser();
		//passwort=config.getPassword();
		// TODO Add DB_URL in config.properties

		//Connect to the Database 		
		try {
			connection = DriverManager.getConnection(DB_URL, user, passwort);
			stmt = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//create two tables (one with RDF triples another with graph structure)
		this.CreatTables(TableName);
		
	}

	public void close() throws SQLException {
		this.stmt.close();
		this.connection.close();
	}
	
	
	private void CreatTables(String TableName1) throws SQLException {
		String SqlString;
		//Statement stmt = connection.createStatement();
		if  ( !CheckIfExist("Bags")){
			SqlString="CREATE TABLE Bags "
					+ " (BagID INT NOT NULL AUTO_INCREMENT,"
					+ " Name VARCHAR(255) NOT NULL,"
					+ " PRIMARY KEY (BagID) );";
			stmt.executeUpdate(SqlString);
			System.out.println("Table Bags is created!");
			}
		else {System.out.println("You tried to create table Bags that already exist.");
		}
		
		if  ( !CheckIfExist(TableName1)){
			SqlString="CREATE TABLE "+TableName1+" "
					+ " (Id INT NOT NULL AUTO_INCREMENT,"
					+ " Crawl INT NULL,"
					+ " Bag INT NULL,"
					+ " StartLvl INT NOT NULL,"
					+ " EndLvl INT NOT NULL,"
					+ " Path VARCHAR(255) NOT NULL,"
					+ " PRIMARY KEY (Id) ,"
					+ " FOREIGN KEY (Bag) REFERENCES Bags (BagID) );";
			stmt.executeUpdate(SqlString);
			System.out.println("Table "+TableName1+" is created!");
			}
		else {System.out.println("You tried to create table "+TableName1+" that already exist.");}	
		
		//stmt.close();
	}
/*
 * FOREIGN KEY (Bag) REFERENCES Bags(BagID)
 * 
 * 
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

	private boolean CheckIfExist(String TableName) throws SQLException {
			//check if table exist
			
			String str="SELECT * FROM information_schema.tables "
					+ "WHERE table_schema = '"+dbName+"' AND table_name = '"+TableName+"' "
					+ "LIMIT 1;";
			ResultSet res = this.stmt.executeQuery(str);
			
			
			//get number of rows
			res.last();
			
			if (res.getRow()>0) return true;
			else return false;
		}
		

	
	public void AddPath( int crawl, int bag, int startLvl, int endLvl, String path) throws SQLException {
		//Statement stmt = connection.createStatement();
		String SqlString="INSERT INTO "+TableName+" (Id, Crawl, Bag, StartLvl, EndLvl, Path) VALUES(null,"+
		crawl+","+bag+","+startLvl+","+endLvl+",\""+path+"\");";
		this.stmt.executeUpdate(SqlString);
		//stmt.close();
	}
	
	public void AddPath( int startLvl, int endLvl, String path) throws SQLException {
		//Statement stmt = connection.createStatement();
		String SqlString="INSERT INTO "+TableName+" (StartLvl, EndLvl, Path) VALUES( "+startLvl+","+endLvl+",\""+path+"\");";
		this.stmt.executeUpdate(SqlString);
		//stmt.close();
	}
	
	
	
	/*
	 * Retrieve SubTrees from DB
	 */
	
	public List<MySubTree> query (String SqlString){
		List <MySubTree> SubTrees= new ArrayList<MySubTree>();
		//store all of the Results
		try{
			//Statement stmt = connection.createStatement();
			//query DB 
			ResultSet result = this.stmt.executeQuery(SqlString);
			//stmt.close();
			
			while (result.next()){
				int startLvl=result.getInt("StartLvl");
				int endLvl=result.getInt("EndLvl");
				String path=result.getString("Path");
				SubTrees.add(new MySubTree(startLvl,endLvl,path));
				}
		}
		catch(Exception x) {
			x.printStackTrace();}
		
		return SubTrees;
	}
	
	public void updateQuery (String SqlString){
		try{
			//updateQuery 
			this.stmt.executeUpdate(SqlString);	
		}
		catch(Exception x) {
			x.printStackTrace();}
		
	}
	
	
	/*
	 * Method to store to DB only path and startLvl
	 */
	
	public void save (List <MySubTree> subTrees){
		for (MySubTree element : subTrees) {
			try {
				AddPath(element.startLvl, element.endLvl, element.path);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	/*
	 * Method to store all fields in the Table including bag and crawl id
	 */
	
	public void save (List <MySubTree> subTrees, int crawl, int bag){
		/*
		* int crawl=0;
		* int bag=0;
		* int endLvl=0;
		*/
		
		for (MySubTree element : subTrees) {
			try {
				AddPath(crawl, bag, element.startLvl, element.endLvl, element.path);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public ResultSet anyQuery(String SqlString){
		ResultSet result = null;
		try{
			//Statement stmt = connection.createStatement();
			//query DB 
			result = this.stmt.executeQuery(SqlString);
		}
		catch(Exception x) {
			x.printStackTrace();}
		
		return result;
	}
	
}


