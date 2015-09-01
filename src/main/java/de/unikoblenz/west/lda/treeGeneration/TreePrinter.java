package de.unikoblenz.west.lda.treeGeneration;

public class TreePrinter {

	public static void printTree(Node node) {
		for (ChildNode childNode : node.getChildren()) {
			System.out.print(node.getName() + "\t");
			System.out.print(childNode.getPredicate() + "\t");
			System.out.println(childNode.getName());
			TreePrinter.printTree(childNode);
		}
	}

}
