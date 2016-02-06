package de.unikoblenz.west.lda.treeGeneration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

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
	static String DB_URL = "jdbc:mysql://localhost:3306/datamining";
	// database credentials
	static String dbName = "datamining";
	static String user = "root";
	static String password = "root";//""root
	private Connection connection;
	private Statement stmt;
	private int bagid;
	private int crawlid=0;
	static String tableName="subtree_path";
	
	private class MyNode{
	    Node node;
	    int lvl;

	    private MyNode(Node node,int lvl) {
	        this.node = node;
	        this.lvl = lvl;
	    }
	}
	
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

			if (rootNode!=null)
				{ bagid=rootNode.bagID;
				this.serializeTree(rootNode);
				}
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
		if  (!tableExists("Crawls")){
			sqlString="CREATE TABLE Crawls "
					+ " (CrawlID INT NOT NULL AUTO_INCREMENT,"
					+ " Name VARCHAR(255) NOT NULL,"
					+ " PRIMARY KEY (CrawlID) );";
			stmt.executeUpdate(sqlString);
			System.out.println("Table Crawls was created.");
		} else
			System.out.println("No need to create table Crawls (already exists).");
		
		if  ( !tableExists("Trees")){
			sqlString="CREATE TABLE Trees "
					+ " (Id INT NOT NULL AUTO_INCREMENT,"
					//+ " TreeID INT NOT NULL,"
					+ " BagID INT NULL,"
					+ " Parent INT NULL,"
					+ " Predicate INT NULL,"
					+ " Current INT NOT NULL,"
					+ " Child_No INT NULL,"
					+ " Lvl INT NOT NULL,"
					+ " PRIMARY KEY (ID) );";
			stmt.executeUpdate(sqlString);
			System.out.println("Table Trees is created!");
			}
		else {System.out.println("You tried to create table Trees that already exist.");
		}
		
		if  (!tableExists(tableName)){
			sqlString="CREATE TABLE "+tableName+" "
					+ " (Id INT NOT NULL AUTO_INCREMENT,"
					+ " Crawl INT NULL,"
					+ " Bag INT NULL,"
					+ " StartLvl INT NOT NULL,"
					+ " EndLvl INT NOT NULL,"
					+ " Path VARCHAR(255) NOT NULL,"
					+ " PRIMARY KEY (Id) "
					//+ ", FOREIGN KEY (Crawl) REFERENCES Crawls (CrawlID) "
					+ ");";
			stmt.executeUpdate(sqlString);
			System.out.println("Table "+tableName+" is created!");
		} else
			System.out.println("No need to create table "+tableName+" (already exists).");
		
		
		
	}

	

	public void serializeTree(RootNode rootNode) {
		
		///get the tree according to the rootNode
		if (rootNode!=null)
		{	
			LinkedList<MyNode> queueNodes = new LinkedList<MyNode>();
			int lvl=0;
			//int treeID;
			int lastVisitedLvl=0;
			MyNode currentnode;
			LinkedList <Node> visited= new LinkedList <Node>();
			queueNodes.add(new MyNode(rootNode,lvl));
			
			try {
			
			/*
			//find the max id in the Table Tree and increment it in order to store new Tree
			String query="SELECT MAX(TreeID) FROM Trees ;";
			ResultSet res = this.stmt.executeQuery(query);
			res.last();
			treeID=res.getInt(1)+1;
			*/
				
			//save first line, root node
			String SqlString="INSERT INTO Trees ( BagID, Current, Lvl) VALUES("+
					bagid+","+rootNode.getName()+","+lvl+");";
			this.stmt.executeUpdate(SqlString);

			
			while(!queueNodes.isEmpty()){
				currentnode = queueNodes.getFirst();
				
				List<ChildNode> children = queueNodes.remove().node.getChildren();
				int child_n=0;
				
				if (currentnode.lvl!=lastVisitedLvl){
					lastVisitedLvl++;}
				
				if (!visited.contains(currentnode.node) )
				{if (children.size()!=0)
					{for (int n=0; n<children.size(); n++){
						
						if (!visited.contains(children.get(n)) )
						{queueNodes.add(new MyNode(children.get(n),currentnode.lvl+1));
						}
						///store to DB
						try {
							lvl=lastVisitedLvl+1;
							SqlString="INSERT INTO Trees ( BagID, Parent, Predicate, Current, Child_No, Lvl) VALUES("+
									bagid+","+currentnode.node.getName()+","+children.get(n).getPredicate()+","+children.get(n).getName()+","+child_n+","+lvl+");";
							this.stmt.executeUpdate(SqlString);
							child_n++;
						} catch (SQLException e) {
							System.out.println("Cannot save current_node,predicate,child_node into Table Trees!!");
							e.printStackTrace();
						}						
					}
				}
				visited.add(currentnode.node);
				}
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		
	}

	
	

	// runs any query string on the database
	public ResultSet query(String Pattern) {
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
		return this.query("SELECT StartLvl, EndLvl, Path, REGEXP_REPLACE(Path, '" + regexp2 + "', '\\\\0" + newVal + "\\(\\)') AS NewPath FROM subtree_path WHERE StartLvl = 0 AND Path REGEXP '" + regexp1 + "';");
	}
	
	// inserts a subtree into the database
	public boolean saveSubtree( int startLvl, int endLvl, String path) {
		ResultSet rs = this.query("INSERT INTO subtree_path (Crawl, Bag, StartLvl, EndLvl, Path) VALUES (" + crawlid + ", " + bagid + ", " + startLvl + ", " + endLvl + ", '" + path + "');");
		return rs != null;
	}
	
	// closes the database connection
	public void close() {
		try {
			this.stmt.close();
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
