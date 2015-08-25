package de.unikoblenz.west.lda.treeGeneration;

import simplemysql.SimpleMySQL;
import simplemysql.SimpleMySQLResult; 

/*
 * This class provide connection to DB, store triples into DB and provide access to the DB data
 * Notes MySQL DB should be set up and run 
 *  
 * @author Olga Zagovora
 */


/*steps for adding data into DB:[read: array=List]
 * 1. add all triples of the tree : 
 * 		-for one triple then: function storeTripleToDB(mysql, subj, pred, obj) 
 * 			1) function AddTriple(mysql, subj, pred, obj);
 * 			2) return ID of this triple
 *  
 * - form Subtree Array from this IDs [then did the same as with predicate ]
 * [ 2nd variant : store IDs into int array]
 * 
 * 2. add data to table subtree_structure :
 * 		1) use IDs (of recently added triples) we need it to fill field Structure (i.e. variable val1):
 * 				- get Subtree Array
 * 				- convert array to String (val1)
 * 		2) use predicates (of recently added triples) we need it to fill field StructurePred (i.e. variable val2)
 * 				- get Subtree Array
 * 				- convert Array to String (val2)
 * 		3) add data into table : function AddSubtreeStructure( val1, val2);
 */


//Example: function store triple "998 7 3" to DB and return ID of this triple 
//PARAMETERs:  subject,predicate, object
//
//SubtreeToDB x=new SubtreeToDB();
//System.out.println("====ID :"+x.storeTripleToDB(2, 2, 2));   

//Example:this function store tree of IDs and tree of predicates into DB
//PARAMETERs: String_tree_of_IDs, String_tree_of_predicates
//
//SubtreeToDB x=new SubtreeToDB();
//x.AddSubtreeStructure("1,2,3","1,2,^,3");

//x.close();


//SubtreeExtractor line 115 [up to 25.08.2015 14:45]:
//SubtreeToDB x=new SubtreeToDB();
//String result=x.storeTripleToDB(subject, predicate, object);
//x.close();


/*steps for retrieving data from DB:
 * 1. find all rows from subtree_structure where StructurePred is particular value:
 * 		1) convert array to string
 * 		2) SQL query SELECT 
 * 2. find add all triples from table rdf_triple :
 * 		1)	convert string with IDS into array
 * 		2) 	For each value in array: if value != -1 then SQL query SELECT
 * 		3) return all triples
 */

/*
 * 
 * 
 */

public class SubtreeToDB {
	// JDBC driver name and database URL
	//static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	//static final String DB_URL = "jdbc:mysql://localhost:3306/datamining";
	static final String DB_URL_Simple = "localhost:3306";
	static final String DB_NAME = "datamining"; //"rdf_schema";
	//  Database credentials
	static final String USER = "admin";
	static final String PASS = "admin";
	private SimpleMySQL mysql;
	
	public SubtreeToDB(){ 
		
		//Initialize 
		//SimpleMySQL mysql;
		mysql = new SimpleMySQL(); 
		//Connect to the Database 
		mysql.connect(DB_URL_Simple,USER, PASS, DB_NAME); 
		
		this.mysql=mysql;
		//create two tables (one with RDF triples another with graph structure)
		this.CreatTables("rdf_triple","tree_pair","subtree_structure");
		
		}
/*	
	public static void main(String[] args) {
		

		*/
		
	
		///==========test area ======
		//PrintAll(mysql);
		//AddTriple(mysql, 9, 21, 3);
		//DelTriple(mysql, 127);
		//PrintAll(mysql);
		//DelTable(mysql,"rdf_triple2");
		//========================================
		
		 /*
		
	}

*/

	
	public void AddTriple( int subj, int pred, int obj) {
		AddTriple(subj, pred, obj, "rdf_triple");
	}	
	
	public void AddTriple( int subj, int pred, int obj, String TableName) {
		//TODO: check if this triple exist then skip SQL query 
		
		String SqlString="INSERT INTO "+TableName+" (TripleID, Predicate, Subject, Object) VALUES(null,"+pred+","+subj+","+obj+");";
		mysql.Query(SqlString);		
	}
	
	public int storeTripleToDB( int subj, int pred,  int obj) {
		int ID;
		AddTriple( subj, pred,  obj, "rdf_triple");
		ID=getID( subj, pred, obj, "rdf_triple");
		return ID;
	}
	
	public int getID(int subj, int pred,  int obj, String TableName) {
		return PrintSelected( subj, pred, obj);
	}
	
	
	//====================================this function is not used anymore
	public void AddPair(int id1, int id2) {
		AddPair( id1, id2, "tree_pair");
	}
	//====================================this function is not used anymore
	public void AddPair(int id1, int id2, String TableName) {
		
		String SqlString="INSERT INTO "+TableName+" (TripleID1, TripleID2) VALUES("+id1+","+id2+")";
		//System.out.println(SqlString);
		mysql.Query (SqlString);
	}
	

	public void AddSubtreeStructure( String graphString, String PredicateArray) {	
		String TableName="subtree_structure"
		String SqlString="INSERT INTO "+TableName+" ( Structure, StructurePred) VALUES('"+graphString+"','"+PredicateArray+"')";
		//System.out.println(SqlString);
		mysql.Query (SqlString);
	}
	
	
	public void DelTriple(int id) {
		//delete from table tree_pair firstly and then from pair table
		DelPair(id);
		//DELETE FROM 'rdf_triple' WHERE 'TripleID'=some_value;
		String SqlString="DELETE FROM rdf_triple WHERE TripleID="+id+";";
		mysql.Query (SqlString);
	}

	public void DelPair(int id) {
		//delete both cases where this id used as TripleID1 and TripleID2
		String SqlString="DELETE FROM tree_pair WHERE TripleID1="+id+";";
		mysql.Query (SqlString);
		SqlString="DELETE FROM tree_pair WHERE TripleID2="+id+";";
		mysql.Query (SqlString);
	}

	public void PrintAll() {
		
		//Do a SELECT Query
		SimpleMySQLResult result;
		result = mysql.Query ("SELECT * FROM rdf_triple"); 
		
		//Print all of the Results
		try{
			while (result.next()){
				System.out.println(result.getString("Subject")+"\n"+result.getString("Predicate")
						+"\n"+result.getString("Object"));}
			result.close();
		}
		catch(Exception x) {
			x.printStackTrace();}	
	}
	
	public int PrintSelected(int subj, int pred, int obj) {
		int id = -20;
		///print values with particular value from the table rdf_triple
		//Do a SELECT Query
		SimpleMySQLResult result;
		
		String SqlString="SELECT * FROM rdf_triple WHERE (Predicate="+pred+" AND Subject="+subj+" AND Object="+obj+" );";
		result = mysql.Query (SqlString); 
		
		//Print all of the Results
		try{
			while (result.next()){
				//System.out.println("Predicate:"+result.getString("Predicate"));
				id=Integer.parseInt(result.getString("TripleID"));
				}
			result.close();
		}
		catch(Exception x) {
			x.printStackTrace();}
		return id;	
	}
	

	public void CreatTables(String TableName1, String TableName2, String TableName3) {
		String SqlString;
		if  ( !CheckIfExist(TableName1)){
			SqlString="CREATE TABLE "+TableName1+" "
					+ "(TripleID INT NOT NULL AUTO_INCREMENT,"
					+ " Predicate INT NOT NULL,"
					+ " Subject INT NOT NULL,"
					+ "  Object INT NOT NULL,"
					+ "  PRIMARY KEY (TripleID) );";
			mysql.Query (SqlString);
			System.out.println("Table "+TableName1+" is created!");
			}
		else {System.out.println("You tried to creat table "+TableName1+" that already exist.");}
		if ( !CheckIfExist( TableName2)){
			SqlString="CREATE TABLE "+TableName2+" "
					+ "(TripleID1 INT NOT NULL,"
					+ " TripleID2 INT NOT NULL,"
					+ " pairID INT NOT NULL AUTO_INCREMENT,"
					+ " INDEX TripleID1_idx (TripleID1 ASC),"
					+ " PRIMARY KEY (pairID),"
					+ " INDEX TripleID2key_idx (TripleID2 ASC),"
					+ " CONSTRAINT TripleID1key"+TableName1+" "
					+ " FOREIGN KEY (TripleID1)"
					+ " REFERENCES "+TableName1+" (TripleID)"
					+ " ON DELETE NO ACTION"
					+ " ON UPDATE NO ACTION,"
					+ " CONSTRAINT TripleID2key"+TableName1+" "
					+ " FOREIGN KEY (TripleID2)"
					+ " REFERENCES "+TableName1+" (TripleID)"
					+ " ON DELETE NO ACTION"
					+ " ON UPDATE NO ACTION);";
			mysql.Query (SqlString);	
			System.out.println("Table "+TableName2+" is created!");
			}
		else {System.out.println("You tried to creat table "+TableName2+" that already exist.");}
		
		if  ( !CheckIfExist( TableName3)){
			SqlString="CREATE TABLE "+TableName3+" "
					+ "(SubtreeID INT NOT NULL AUTO_INCREMENT, "
					+ " Structure VARCHAR(255) NOT NULL,"
					+ " StructurePred VARCHAR(255) NOT NULL,"
					+ "  PRIMARY KEY (SubtreeID) );";
			mysql.Query (SqlString);
			System.out.println("Table "+TableName3+" is created!");
			}
		else {System.out.println("You tried to creat table "+TableName3+" that already exist.");}
	}
	
	
	public void DelTable(String TableName) {
		if (CheckIfExist(TableName))
		{	try
			{
				String SqlString="DROP TABLE "+TableName+";";
				mysql.Query(SqlString);
				//System.out.println("Table "+TableName+" is deleted!");
			}
			catch(Exception x) {
				System.out.println("We cannot del this table. try again");
				x.printStackTrace();
				}	
		}
		else{System.out.println("The table "+TableName+" does not exist. So there is nothing to del.");}
					
		
	}
	public boolean CheckIfExist(String TableName) 
	{
		//check if table exist
		SimpleMySQLResult res;
		String str="SELECT * FROM information_schema.tables "
				+ "WHERE table_schema = '"+DB_NAME+"' AND table_name = '"+TableName+"' "
				+ "LIMIT 1;";
		res=mysql.Query(str);
		if (res.getNumRows()>0) return true;
		else return false;
	}


	public void close() {
		this.mysql.close();
	}
}

