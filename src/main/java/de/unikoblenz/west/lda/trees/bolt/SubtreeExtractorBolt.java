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
 * This class provides a storm bolt that consumes tree from
 * {@link SubtreeExtractorBolt} and returns a list of subtrees
 * 
 * @author Martin Koerner <info@mkoerner.de>
 *
 */
public class SubtreeExtractorBolt extends BaseRichBolt {

	OutputCollector _collector;
	   
    
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      _collector = collector;
    }

    public void execute(Tuple tuple) {
      _collector.emit(tuple, new Values(tuple.getString(0) + "!!!SubtreeExtractorBolt"));
      _collector.ack(tuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("word"));
    }

}