package de.unikoblenz.west.lda.treeGeneration;

import java.util.Set;

@Deprecated
public class SubtreeString {

	private final String levelSeparator = "^";
	private final String itemSeparator = ",";
	private String subtree;
	private boolean wasExtended;

	public SubtreeString() {
		this.subtree = "";
		this.wasExtended = false;
	}

	public SubtreeString(String subtreee) {
		this.subtree = subtreee;
		this.wasExtended = false;

	}

	public SubtreeString(String subtree, boolean wasExtended) {
		this.subtree = subtree;
		this.wasExtended = wasExtended;
	}

	public boolean wasExtended() {
		return this.wasExtended;
	}

	public void setExtended() {
		this.wasExtended = true;
	}

	public void addBefore(int integer) {
		this.addBefore(String.valueOf(integer));
	}

	public void addBefore(String string) {
		if (!this.subtree.isEmpty()) {
			this.subtree = this.itemSeparator + this.subtree;
		}
		this.subtree = string + this.subtree;
		this.addLevelSeparatorAfter();
	}

	public void addAfter(int integer) {
		this.addAfter(String.valueOf(integer));
	}

	public void addAfter(String string) {
		if (!this.subtree.isEmpty()) {
			this.subtree = this.subtree + this.itemSeparator;
		}
		this.subtree = this.subtree + string;
		this.addLevelSeparatorAfter();
	}

	private void addLevelSeparatorAfter() {
		if (!this.subtree.isEmpty()) {
			this.subtree = this.subtree + this.itemSeparator;
		}

		this.subtree = this.subtree + this.levelSeparator;

	}

	public boolean hasRoot(Set<Integer> rootPredicateIds) {
		if (this.subtree.contains(this.itemSeparator)) {
			return rootPredicateIds.contains(Integer.parseInt(this.subtree
					.split(this.itemSeparator)[0]));
		} else {
			return false;
		}

	}

	@Override
	public SubtreeString clone() {
		return new SubtreeString(this.subtree, this.wasExtended);

	}

	@Override
	public String toString() {
		return this.subtree;
	}

	public void addTreeAfter(Subtree newTree) {
		if (!this.subtree.isEmpty()) {
			this.subtree += this.itemSeparator;
		}
		this.subtree += newTree.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Subtree)) {
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
