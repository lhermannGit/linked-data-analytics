package de.unikoblenz.west.lda.input;

import java.sql.ResultSet;
import java.sql.SQLException;

import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.custom_hash.TObjectIntCustomHashMap;
import simplemysql.SimpleMySQL;

public class LookupCache {

	public enum Table {
		objects, predicates
	}

	protected TObjectIntCustomHashMap<char[]> objects;
	protected int object_id = Integer.MIN_VALUE + 1;

	protected int flushAmount;
	protected boolean databaseEmpty = true;
	protected SimpleMySQL database;
	protected String table;

	public LookupCache(int upperBound, int flushAmount, Table table) {
		objects = new TObjectIntCustomHashMap<char[]>(CharArrHashingStrategy.create(), upperBound);
		this.flushAmount = flushAmount;
		this.database = SimpleMySQL.getInstance();

		switch (table) {
		case objects:
			this.table = "objects";
			break;
		case predicates:
			this.table = "predicates";
			break;
		}
	}

	public int get(char[] tupleElem) {
		Integer result = objects.get(tupleElem);
		if (result == objects.getNoEntryValue()) {
			if (databaseEmpty) {
				objects.put(tupleElem, object_id);
				if (objects.size() > objects.capacity() - 5)
					flush();
				return object_id++;
			} else {
				ResultSet rset = null;
				rset = (ResultSet) database
						.Query("SELECT key FROM " + table + " WHERE key ='" + tupleElem.hashCode() + "'");
				try {
					while (rset.next()) {
						return rset.getInt("value");
					}
					objects.put(tupleElem, object_id);
					if (objects.size() > objects.capacity() - 5)
						flush();
					return object_id++;
				} catch (SQLException e) {
					System.out.println("Problem retrieving value from table!");
					e.printStackTrace();
				}
			}
		} else
			return result;
		return Integer.MIN_VALUE;
	}

	public void flush() {
		TObjectIntIterator<char[]> iterator = objects.iterator();
		int i = 0;
		String query = "INSERT INTO " + table + " VALUES ";

		if (!iterator.hasNext())
			return;

		while (iterator.hasNext() && i < flushAmount) {
			if (i > 0)
				query = query + ",";
			iterator.advance();
			query = query + "('" + String.copyValueOf(iterator.key()) + "', " + iterator.value() + ")";
			iterator.remove();
			i++;
		}

		query = query + ";";
		System.out.println(query);
		if (i > 0)
			database.Query(query);

		databaseEmpty = false;
	}

	public void close() {
		objects.clear();
	}
}
