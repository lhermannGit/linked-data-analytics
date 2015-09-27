package de.unikoblenz.west.lda.treeGeneration;

public class TreePrinter {

	public static void printTree(Node node) {
		for (ChildNode childNode : node.getChildren()) {
			System.out.print(node.getName() + "\t");
			System.out.print(childNode.getPredicate() + "\t");
			System.out.print(childNode.getName()+"\t");
			System.out.println(childNode.getRdfCount()+"\t");


			TreePrinter.printTree(childNode);
		}
	}

	public static String treeToString(Node node) {
		String treeString="";
		for (ChildNode childNode : node.getChildren()) {
			treeString+=node.getName() + "\t";
			treeString+=childNode.getPredicate() + "\t";
			treeString+=childNode.getName()+"\t";
			treeString+=childNode.getRdfCount()+"\n";


			treeString+=TreePrinter.treeToString(childNode);
		}
		return treeString;
	}

}
