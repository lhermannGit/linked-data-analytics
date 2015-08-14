package de.unikoblenz.west.lda.input;

import java.util.ArrayList;

import de.unikoblenz.west.lda.input.LookupCache.Table;
import gnu.trove.map.hash.TIntIntHashMap;
import simplemysql.SimpleMySQL;

public class InputReduction {

	TIntIntHashMap preds_freq = new TIntIntHashMap();

	ArrayList<DisjointSet> sets = new ArrayList<DisjointSet>();

	LookupCache predicates;
	LookupCache objects;

	public InputReduction(int upperBound, int flushAmount) {
		SimpleMySQL database = SimpleMySQL.getInstance();
		database.connect("localhost", "root", "", "datamining");

		predicates = new LookupCache(upperBound, flushAmount, Table.predicates);
		objects = new LookupCache(upperBound, flushAmount, Table.objects);
	}

	private int existsWithinSets(int id) {
		int offset = 0;

		// TODO traverse in reverse order instead
		for (DisjointSet set : sets) {
			if (set.has(id))
				return offset;
			offset++;
		}
		return -1;
	}

	public void consume(String subj, String pred, String obj) {

		// Subject
		char[] c = subj.toCharArray();
		int subjID = objects.get(c);

		// Predicate
		c = pred.toCharArray();
		int predID = predicates.get(c);
		preds_freq.adjustOrPutValue(predID, 1, 1);
		// preds_reverse.putIfAbsent(predID, c);

		// Object
		c = obj.toCharArray();
		int objID = objects.get(c);

		int subjSet = existsWithinSets(subjID);
		int objSet = existsWithinSets(objID);

		if (subjSet == -1 && objSet == -1) {
			sets.add(new DisjointSet());
			sets.get(sets.size() - 1).add(subjID, predID, objID);
		} else if (subjSet >= 0 && subjSet == objSet) {

			sets.get(subjSet).add(subjID, predID, objID);
			// either subjBits or objBits has more than one Bit
		} else {
			DisjointSet set;

			if (subjSet >= 0 && objSet >= 0) {
				set = sets.get(subjSet);
				set.merge(sets.get(objSet));
				sets.remove(objSet);
			} else if (subjSet >= 0)
				set = sets.get(subjSet);
			else
				set = sets.get(objSet);
			set.add(subjID, predID, objID);
		}

	}

	// public TIntArrayList getReducedInput() {
	// int i = 0;
	// while (i < result.size()) {
	// result.set(i + 3, preds_freq.get(result.get(i + 1)));
	// i = i + 4;
	// }
	//
	// return result;
	// }

	public ArrayList<DisjointSet> getSets() {
		// TODO need to set frequencies first
		predicates.flush();
		objects.flush();
		return sets;
	}

	public void close() {
		predicates.close();
		objects.close();
		preds_freq.clear();
		// result.clear();
	}
}
