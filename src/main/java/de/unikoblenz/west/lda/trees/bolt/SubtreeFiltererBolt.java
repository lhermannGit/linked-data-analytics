package de.unikoblenz.west.lda.trees.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * 
 * @author Martin Koerner <info@mkoerner.de>, Olga Zagovora <zagovora@uni-koblenz.de>	
 *
 */

//probably irrelevant, better use SubtreeCounterBolt for filtering the most relevant Patterns
public class SubtreeFiltererBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;

	public void execute(Tuple tuple, BasicOutputCollector collector) {
	    System.out.println(tuple);
	  }

	  public void declareOutputFields(OutputFieldsDeclarer ofd) {
	  }

	public void execute(Tuple arg0) {
		// TODO Auto-generated method stub
		
	}

	public void prepare(@SuppressWarnings("rawtypes") Map arg0, TopologyContext arg1, OutputCollector arg2) {
		// TODO Auto-generated method stub
		
	}

}
