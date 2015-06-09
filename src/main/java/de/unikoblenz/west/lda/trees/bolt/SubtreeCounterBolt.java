package de.unikoblenz.west.lda.trees.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * 
 * @author Martin Koerner <info@mkoerner.de>
 *
 */
public class SubtreeCounterBolt extends BaseRichBolt {

	OutputCollector collector;

	public void prepare(Map conf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple tuple) {
		String tree = tuple.getString(0);
		String treeWithCount = tree + "\t1";
		this.collector.emit(tuple, new Values(treeWithCount));

		this.collector.ack(tuple);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("treeCounts"));
	}

}
