package de.unikoblenz.west.lda.treeGeneration;

public class TreePrinter {

	public static void printTree(Node node) {
		System.out.println(TreePrinter.treeToString(node));
	}

	public static String treeToString(Node node) {
		return treeToString(node, 0);
		
	}
	public static String treeToString(Node node,int depth) {
		String treeString="";
		//get depth
		for (ChildNode childNode : node.getChildren()) {
			for(int i=0;i<depth;i++){
				treeString+= "\t";
			}
			treeString+=node.getName() + "\t";
			treeString+=childNode.getPredicate() + "\t";
			treeString+=childNode.getName()+"\t";
			treeString+=childNode.getRdfCount()+"\n";



			//treeString+=TreePrinter.treeToString(childNode,depth+1);
		}
		return treeString;
	}

}
