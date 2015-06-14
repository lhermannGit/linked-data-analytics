package de.unikoblenz.west.lda.trees.spout;

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



public class RDFSpout extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	private static final int maxPackageSize = 50;
	
	SpoutOutputCollector collector;
    InputStream input;
    String fileLocation;
    NxParser nxp;
    
    public RDFSpout(InputStream input){
    	this.input = input;
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
    		
    	}
    	
		
    	return null;
    	
    }
    
    
	
	

	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void nextTuple() {
		Utils.sleep(100);
		ArrayList<String> triplesList = new ArrayList<String>();
		// Insert Triples here
		triplesList
				.add("<http://example.com/tim> <http://example.com/likes> <http://example.com/car>");
		triplesList
				.add("<http://example.com/tim> <http://example.com/likes> <http://example.com/cake>");
		triplesList
				.add("<http://example.com/cake> <http://example.com/is> <http://example.com/sweet>");
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
