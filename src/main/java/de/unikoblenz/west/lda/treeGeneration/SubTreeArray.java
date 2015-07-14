package de.unikoblenz.west.lda.treeGeneration;

import java.util.Arrays;
import java.util.Set;

public class SubTreeArray {

	// private final String levelSeparator = "^";
	private final int levelSeparator = -1;
	// private final String itemSeparator = ","; //we do not need it
	// private String subtree;
	private int[] subtree;

	// public SubTreeArray(String subtreee) {
	public SubTreeArray(int[] subtreee) {
		this.subtree = subtreee;
	}

	public SubTreeArray() {
		// this.subtree = "";
		this.subtree = new int[0];
	}

	public void addBefore(int integer) {
		// this.addBefore(String.valueOf(integer));
		int[] subtreeNew = new int[this.subtree.length + 1];
		subtreeNew[0] = integer;
		System.arraycopy(this.subtree, 0, subtreeNew, 1, subtreeNew.length);
		System.out.println(Arrays.toString(subtreeNew)); // ?
		// /how to rewrite
		java.util.Arrays.copyOf(this.subtree, this.subtree.length + 1);
		this.subtree = subtreeNew;
		System.out.println(Arrays.toString(this.subtree));
		this.addLevelSeparatorAfter();
	}

	/*
	 * public void addBefore(int[] string) { this.subtree = string +
	 * this.subtree; this.addLevelSeparatorAfter(); }
	 */
	// /do we need it?
	/*
	 * public void addBefore(String string) { if (!this.subtree.isEmpty()) {
	 * this.subtree = this.itemSeparator + this.subtree; } this.subtree = string
	 * + this.subtree; this.addLevelSeparatorAfter(); }
	 */

	public void addAfter(int integer) {
		// this.addAfter(String.valueOf(integer));
		this.subtree = Arrays.copyOf(this.subtree, this.subtree.length + 1);
		this.subtree[this.subtree.length - 1] = integer;
		this.addLevelSeparatorAfter();
		System.out.println(Arrays.toString(this.subtree));

	}

	/*
	 * public void addAfter(int[] string) { this.subtree =
	 * Arrays.copyOf(this.subtree, this.subtree.length + string.length);
	 *
	 * this.subtree = this.subtree + string; this.addLevelSeparatorAfter(); }
	 *
	 * public void addAfter(String string) { if (!this.subtree.isEmpty()) {
	 * this.subtree = this.subtree + this.itemSeparator; } this.subtree =
	 * this.subtree + string; this.addLevelSeparatorAfter(); }
	 */

	private void addLevelSeparatorAfter() {
		// this.addAfter(this.levelSeparator);
		this.subtree = Arrays.copyOf(this.subtree, this.subtree.length + 1);
		this.subtree[this.subtree.length - 1] = this.levelSeparator;
	}

	public boolean hasRoot(Set<Integer> rootPredicateIds) {
		if (this.subtree.length != 0) {
			return rootPredicateIds.contains(this.subtree[0]);
		} else {
			return false;
		}
	}

	/*
	 * //old
	 *
	 * public boolean hasRoot(Set<Integer> rootPredicateIds) { if
	 * (this.subtree.contains(this.itemSeparator)) { return
	 * rootPredicateIds.contains(Integer.parseInt(this.subtree
	 * .split(this.itemSeparator)[0])); } else { return false; } }
	 */

	@Override
	public SubTreeArray clone() {
		return new SubTreeArray(this.subtree);

	}

	@Override
	public String toString() {
		return Arrays.toString(this.subtree);
	}

	public void addTreeAfter(SubTreeArray newTree) {
		// this.subtree = ArrayUtils.addAll(this.subtree, newTree.toString());
	}

	// /didnt change this part
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SubTreeArray)) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		return this.toString().equals(obj.toString());
	}

	@Override
	public int hashCode() {
		return this.subtree.hashCode();
	}
}
