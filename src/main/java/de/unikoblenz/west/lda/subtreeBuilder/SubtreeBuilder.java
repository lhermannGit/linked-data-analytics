package de.unikoblenz.west.lda.subtreeBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.io.PrintWriter;

import de.unikoblenz.west.lda.treeGeneration.*;

public class SubtreeBuilder {
	// random paths to simulate the tree traversal output
	static int[][] paths = {
		{0},
		{0, 0},
		{0, 1},
		{0, 1, 0},
		{0, 1, 1},
		{0, 1, 1, 0},
		{0, 1, 1, 1},
		{0, 2},
		{1},
		{1, 0},
		{1, 1},
		{1, 1, 0},
		{2},
		{2, 0},
		{2, 0, 0},
		{2, 1},
	};
	/*static int[][] paths = {
		{2},
		{2, 1},
		{2, 0},
		{2, 0, 0},
		{1},
		{1, 1},
		{1, 1, 0},
		{1, 0},
		{0},
		{0, 2},
		{0, 1},
		{0, 1, 1},
		{0, 1, 1, 1},
		{0, 1, 1, 0},
		{0, 1, 0},
		{0, 0},
	};*/
	/*static int[][] paths = {
		{1},
		{1, 3},
		{1, 3, 4},
		{1, 3, 4, 5},
		{1, 2},
	};*/

	public static void main(String[] args) {
		StoreToMariaDB x;
		try {
			x = new StoreToMariaDB();
			
			// empty table
			x.anyQuery("TRUNCATE `subtree_path`;");
			
			System.out.println("Create Subtrees...");
			
			try {
				PrintWriter writer = new PrintWriter("SubtreeBuilder.log", "UTF-8");
				
				// simulate the tree traversal
				for(int[] path : paths)
					// for every path from the tree traversal this method must be executed
					buildNewSubtrees(path, x, writer);

				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("Subtrees created. Details in file SubtreeBuilder.log");
			
			x.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// subpattern of regular expression for nested int & parenthesis
	static String nes =
		"(" +					// start group 1 (for recursion)
			"(" +				// start group 2 (allow neighbours)
	         	"[0-9]+" +		// match the int
	         	"\\\\(" +		// match the literal "("
	            	"(|(?1))" +	// empty or start recursively at group 1 again
	          	"\\\\)" +		// match the literal ")"
	        ")*" +				// end group 2
		")";					// end group 1
	
	// creates all new subtrees from a given path and inserts them into the database
	public static void buildNewSubtrees(int[] path, StoreToMariaDB x, PrintWriter writer) {
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
		boolean define_subpattern = false;
		String n;
		if (define_subpattern) {
			n = "(?&nes)";
			nes = "(?(DEFINE)(?<nes>" + nes + "))" + n;
		} else
			n = nes;

		// creates the regular expression from the given path which allows nested elements in between
		// e.g. for {5, 1, 3} something like:
		// ^{anyNestedElementsHere}5({anyNestedElementsHere}1({anyNestedElementsHere}3(
		String re = "^" + nes;
		for (int i = 0; i < path.length - 1; i++) {
			re += path[i] + "\\\\(";
			if (i != path.length - 2)
				re += n;
		}
		
		writer.println("\nInsert: " + Arrays.toString(path) + ", Regexp: " + re);
		
		// get all relevant subtrees and already create its new subtree via REGEXP_REPLACE
		ResultSet res = x.anyQuery("SELECT StartLvl, EndLvl, Path, REGEXP_REPLACE(Path, '" + re + n + "', '\\\\0" + path[path.length - 1] + "\\(\\)') AS NewPath FROM subtree_path WHERE Path REGEXP '" + re + "';");
		if (res == null)
			writer.println("ERROR on SELECT");
		else
			try {
				// for each relevant subtree
				while (res.next()) {
					// increment EndLvl if necessary
					int EndLvl = res.getInt("EndLvl");
					if (EndLvl < path.length - 1)
						EndLvl++;
					
					// insert new subtree
					ResultSet res2 = x.anyQuery("INSERT INTO subtree_path (StartLvl, EndLvl, Path) VALUES (0, " + EndLvl + ", '" + res.getString("NewPath") + "');");
					if (res2 == null)
						writer.println("ERROR on Insert: " + res.getString("NewPath"));
					else
						writer.println("Selected: " + res.getString("Path") + " - Inserted: " + res.getString("NewPath"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		// insert new subtree if path has only one element
		if (path.length == 1) {
			String newPath = path[0] + "()";
			ResultSet res2 = x.anyQuery("INSERT INTO subtree_path (StartLvl, EndLvl, Path) VALUES (0, 0, '" + newPath + "')");
			if (res2 == null)
				writer.println("ERROR on Create: " + newPath);
			else
				writer.println("Created: " + newPath);
		}
	}
}
