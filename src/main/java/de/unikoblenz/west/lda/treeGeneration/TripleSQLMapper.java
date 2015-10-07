package de.unikoblenz.west.lda.treeGeneration;

import java.util.HashMap;
import java.util.Map.Entry;

public class TripleSQLMapper {
	public HashMap<String, Integer> tripleKeyMap;
	public SubtreeToDB subtreeToDB;
	
	public TripleSQLMapper(){
		this.tripleKeyMap=new HashMap<String,Integer>();
		this.subtreeToDB=new SubtreeToDB();
	}

	public static void main(String[] args) {
		TripleSQLMapper tripleSQLMapper=new TripleSQLMapper();

		int[] testArray = new int[] {1,2,3,1, 	1,4,5,1, 	9,3,2,1,
							 3,1,9,1,	1,9,6,1, 	11,12,13,1,
							 13,1,14,1, 13,2,15,1,	6,3,2,1,
							 14,16,2,1,	2,3,11,1,	13,3,7,1};
		tripleSQLMapper.storeTriples(testArray);
		for(Entry<String, Integer> entry:tripleSQLMapper.tripleKeyMap.entrySet()){
			System.out.println(entry.getKey()+": "+entry.getValue());
		}

	}

	public void storeTriples(int[] inputArray){
		if(inputArray.length%4!=0){
			throw new IllegalArgumentException("array can't be divided by 4");	
		}
		for(int i=0;i<inputArray.length;i++){
			int tripleID=this.subtreeToDB.storeTripleToDB(inputArray[i], inputArray[i+1], inputArray[i+2]);
			this.tripleKeyMap.put(inputArray[i]+"\t"+inputArray[i+1]+"\t"+ inputArray[i+2], tripleID);
			i+=4;
		}
	}
}
