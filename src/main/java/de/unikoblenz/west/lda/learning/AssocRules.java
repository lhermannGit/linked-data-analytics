package de.unikoblenz.west.lda.learning;

import java.io.BufferedWriter;
import java.io.FileWriter;

import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;




public class AssocRules {
	
	public String data = null;
	public String output = null;
	
	public AssocRules(String data){
		this.data = data;
		output = data.substring(0,data.length()-5) + ".txt";
	}
	
	public void createAssociationRule() throws Exception{
		DataSource ds = new DataSource(data);
		Instances ins = ds.getDataSet();
		Apriori ap = new Apriori();
		ap.buildAssociations(ins);
		
		FileWriter fw = new FileWriter(output);
		BufferedWriter br = new BufferedWriter(fw);
		br.write(ap.toString());
		
		br.close();
		if(fw != null){
			fw.close();
		}
	}
	

}
