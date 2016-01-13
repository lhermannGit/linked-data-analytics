package de.unikoblenz.west.lda.treeGeneration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SubtreeBuilder implements ISubtreeBuilder {
	
	public SubtreeBuilder() {
		
	}

	// subpattern of regular expression for nested int & parenthesis
	String nes =
		"(" +					// start group 1 (for recursion)
			"(" +				// start group 2 (allow neighbours)
	         	"[0-9]+" +		// match the int
	         	"\\\\(" +		// match the literal "("
	            	"(|(?1))" +	// empty or start recursively at group 1 again
	          	"\\\\)" +		// match the literal ")"
	        ")*" +				// end group 2
		")";					// end group 1
	
	// creates all new subtrees from a given path and inserts them into the database
	public void buildTrees(ArrayList<Integer> path, Database db) {
		/*
		 * below is an approach to make use of a named subroutine to shorten the whole query string
		 * instead of the long nes-string above only (?&nes) would have to be inserted into the regular expression
		 * 
		 * this error is thrown: "...two named subpatterns have the same name..."
		 * 
		 * syntax can be seen here: https://mariadb.com/kb/en/mariadb/pcre/
		 * scroll down to "Defining subpatterns for use by reference"
		 * 
		 * I don't see the problem, feel free to give it a go and ask me (Adrian)
		 * 
		 * set define_subpattern to true to activate it
		 */
		boolean defineSubpattern = false;
		String n;
		if (defineSubpattern) {
			n = "(?&nes)";
			nes = "(?(DEFINE)(?<nes>" + nes + "))" + n;
		} else
			n = nes;
		
		// creates the regular expression from the given path which allows nested elements in between
		// e.g. for {5, 1, 3} something like:
		// ^{anyNestedElementsHere}5({anyNestedElementsHere}1({anyNestedElementsHere}3(
		String re = "^" + nes;
		for (int i = 0; i < path.size() - 1; i++) {
			re += path.get(i) + "\\\\(";
			if (i != path.size() - 2)
				re += n;
		}
		
		System.out.println("\nInsert: " + path.toString() + ", Regexp: " + re);
		
		// get all relevant subtrees and already create its new subtree via REGEXP_REPLACE
		ResultSet res = db.anyQuery("SELECT StartLvl, EndLvl, Path, REGEXP_REPLACE(Path, '" + re + n + "', '\\\\0" + path.get(path.size() - 1) + "\\(\\)') AS NewPath FROM subtree_path WHERE StartLvl = 0 AND Path REGEXP '" + re + "';");
		if (res == null)
			System.out.println("ERROR on SELECT");
		else
			try {
				// for each relevant subtree
				while (res.next()) {
					// increment EndLvl if necessary
					int EndLvl = res.getInt("EndLvl");
					if (EndLvl < path.size() - 1)
						EndLvl++;
					
					// insert new subtree
					ResultSet res2 = db.anyQuery("INSERT INTO subtree_path (StartLvl, EndLvl, Path) VALUES (0, " + EndLvl + ", '" + res.getString("NewPath") + "');");
					if (res2 == null)
						System.out.println("ERROR on Insert: " + res.getString("NewPath"));
					else
						System.out.println("Selected: " + res.getString("Path") + " - Inserted: " + res.getString("NewPath"));
					
					// insert subtrees with 0 < StartLvl < EndLvl
					// TODO
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		// insert new subtree if path has only one element
		if (path.size() == 1) {
			String newPath = path.get(0) + "()";
			ResultSet res2 = db.anyQuery("INSERT INTO subtree_path (StartLvl, EndLvl, Path) VALUES (0, 0, '" + newPath + "')");
			if (res2 == null)
				System.out.println("ERROR on Create: " + newPath);
			else
				System.out.println("Created: " + newPath);
		}
	}
	
	public void Initialize(Database db) {
		// empty table
		db.anyQuery("TRUNCATE `subtree_path`;");
		System.out.println("Emptied table 'subtree_path'. Create subtrees...");
	}

}
