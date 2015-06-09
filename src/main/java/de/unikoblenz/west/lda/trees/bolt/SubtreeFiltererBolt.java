package de.unikoblenz.west.lda.trees.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * 
 * @author Martin Koerner <info@mkoerner.de>
 *
 */
public class SubtreeFiltererBolt extends BaseBasicBolt {

	  public void execute(Tuple tuple, BasicOutputCollector collector) {
	    System.out.println(tuple);
	  }

	  public void declareOutputFields(OutputFieldsDeclarer ofd) {
	  }

}
