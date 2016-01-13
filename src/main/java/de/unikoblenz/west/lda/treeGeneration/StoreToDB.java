package de.unikoblenz.west.lda.treeGeneration;

import java.util.ArrayList;
import java.util.List;

import de.unikoblenz.west.lda.input.MySQLConnectionInfo;
import simplemysql.SimpleMySQL;
import simplemysql.SimpleMySQLResult;

/*
 * This class provide connection to DB, store trees into DB and provide access to the DB data
 * Notes: - MySQL DB should be set up and run 
 *  	  - Add to build Path library: libs/SimpleMySQL.jar
 *  
 * @author Olga Zagovora <zagovora@uni-koblenz.de>
 */


/*adding data into DB:
 * 
 * 
 * StoreToDB x=new StoreToDB();
 * 
 * List <MySubTree> subTrees= new ArrayList<MySubTree>();
 * subTrees.add(new MySubTree (0,0,"12.(1345).34"));
 * subTrees.add(new MySubTree (0,0,"12(167).34"));
 * x.save( subTrees, 1, 1); //subTrees, int crawl, int bag
 * OR: x.save(subTrees);
 * x.close();
 */


/*steps for retrieving data from DB:
 * 
 * 
 * StoreToDB x=new StoreToDB();
 * List <MySubTree> subTrees = x.query ("12.(1%"); //String searchPath
 * x.close();
 * 
 * for (MySubTree iterator : subTrees) {
 * System.out.println(iterator.path);}
 * 
 * 
 * 
 */


class StoreToDB implements IDatabase {

	// JDBC driver name and database URL
	//static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	//static final String DB_URL = "jdbc:mysql://localhost:3306/datamining";
	static String dbServer = "localhost:3306";
	static String dbName = "datamining";
	//  Database credentials
	static String user = "admin";
	static String passwort = "admin";
	private SimpleMySQL mysql;
	
	
	static String TableName="subtree_path";
	
	public StoreToDB(){
		
		//Initialize 
		//SimpleMySQL mysql;
		this.mysql = new SimpleMySQL(); 
		//Connect to the Database 
		MySQLConnectionInfo config = new MySQLConnectionInfo();
		dbServer=config.getServer();
		user=config.getUser();
		passwort=config.getPassword();
		dbName=config.getDatabaseName();
		mysql.connect(dbServer,user, passwort, dbName); 
		

		//create two tables (one with RDF triples another with graph structure)
		this.CreatTables(TableName);
		
	}

	public void close() {
		this.mysql.close();
	}
	
	
	private void CreatTables(String TableName1) {
		String SqlString;
		
		if  ( !CheckIfExist("Bags")){
			SqlString="CREATE TABLE Bags "
					+ " (BagID INT NOT NULL AUTO_INCREMENT,"
					+ " Name VARCHAR(255) NOT NULL,"
					+ " PRIMARY KEY (BagID) );";
			mysql.Query (SqlString);
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
			mysql.Query (SqlString);
			System.out.println("Table "+TableName1+" is created!");
			}
		else {System.out.println("You tried to create table "+TableName1+" that already exist.");}		
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

	private boolean CheckIfExist(String TableName) {
			//check if table exist
			SimpleMySQLResult res;
			String str="SELECT * FROM information_schema.tables "
					+ "WHERE table_schema = '"+dbName+"' AND table_name = '"+TableName+"' "
					+ "LIMIT 1;";
			res=mysql.Query(str);
			if (res.getNumRows()>0) return true;
			else return false;
		}
		

	
	public void AddPath( int crawl, int bag, int startLvl, int endLvl, String path) {
		
		String SqlString="INSERT INTO "+TableName+" (Id, Crawl, Bag, StartLvl, EndLvl, Path) VALUES(null,"+
		crawl+","+bag+","+startLvl+","+endLvl+",\""+path+"\");";
		mysql.Query(SqlString);		
	}
	
	public void AddPath( int startLvl, int endLvl, String path) {
		String SqlString="INSERT INTO "+TableName+" (StartLvl, EndLvl, Path) VALUES( "+startLvl+","+endLvl+",\""+path+"\");";
		mysql.Query(SqlString);		
	}
	


	
	
	
	/*
	 * Retrieve SubTrees from DB
	 */
	
	public List<MySubTree> query (String Pattern){
		List <MySubTree> SubTrees= new ArrayList<MySubTree>();
		

		SimpleMySQLResult result;
		//query DB where Path consist of Pattern
		String SqlString="SELECT * FROM "+TableName+" WHERE Path LIKE \""+Pattern+"\" ;";
		result=mysql.Query(SqlString);
		//store all of the Results
		try{
			while (result.next()){
				//System.out.println("StartLvl:"+result.getString("StartLvl"));
				int startLvl=Integer.parseInt(result.getString("StartLvl"));
				int endLvl=Integer.parseInt(result.getString("EndLvl"));
				String path=result.getString("Path");
				SubTrees.add(new MySubTree(startLvl,endLvl,path));
				}
			result.close();
		}
		catch(Exception x) {
			x.printStackTrace();}
		
		return SubTrees;
	}
	
	
	/*
	 * Method to store to DB only path and startLvl
	 */
	
	public void save (List <MySubTree> subTrees){
		
		for (MySubTree element : subTrees) {
			AddPath(element.startLvl, element.endLvl, element.path);
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
			AddPath(crawl, bag, element.startLvl, element.endLvl, element.path);
		}
	}
	
}

