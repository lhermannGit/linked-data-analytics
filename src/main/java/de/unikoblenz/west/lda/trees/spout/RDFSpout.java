package de.unikoblenz.west.lda.trees.spout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.*;

import de.unikoblenz.west.lda.input.*;



public class RDFSpout extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	private static final int maxPackageSize = 50;
	
	SpoutOutputCollector collector;
    InputStream input;
    String fileLocation;
    NxParser nxp;
    
    public RDFSpout(String fileLocation) throws IOException{
    	ZipInput zip = new ZipInput(fileLocation);
    	input = zip.getInputStream(fileLocation);
    	nxp = new NxParser(input);
    }
    
    public RDFSpout(){
    	
    }
    
    public ArrayList<String> pack(){
    	
    	ArrayList<String> pack = new ArrayList<String>();
    	
    	String subject;
    	String predicate;
    	String object;
    	
    	Node[] line;
    	
    	while(pack.size()< maxPackageSize && nxp.hasNext()){
    		line = nxp.next();
    		subject = line[0].toString();
    		predicate = line[1].toString();
    		object = line[2].toString();
    		
    		pack.add(subject + " " + predicate + " " + object ); 
    		
    		
    	}
    	
		
    	return pack;
    	
    }
    
    
	
	

	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void nextTuple() {
		Utils.sleep(100);
		ArrayList<String> triplesList = pack();
		this.collector.emit(new Values(triplesList));
	}

	@Override
	public void ack(Object id) {
	}

	@Override
	public void fail(Object id) {
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("triples"));
	}

}
