package de.unikoblenz.west.lda.learning;

import java.io.BufferedWriter;
import java.io.FileWriter;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class Clustering {
	
	public String data = null;
	public String output = null;
	
	public Clustering(String data){
		this.data = data;
		output = data.substring(0,data.length()-5) + ".txt";
	}
	
	public void createCluster() throws Exception {
		DataSource ds = new DataSource(data);
		Instances ins = ds.getDataSet();
		SimpleKMeans skm = new SimpleKMeans();
		
		skm.setNumClusters(20);
		skm.buildClusterer(ins);
		
		BufferedWriter br = new BufferedWriter(new FileWriter(output));
		br.write(skm.toString());
		
		br.close();
			
	}

}
