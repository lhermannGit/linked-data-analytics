package de.unikoblenz.west.lda.trees.bolt;

import java.util.ArrayList;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import de.unikoblenz.west.lda.trees.spout.RDFSpout;

/**
 * This class provides a storm bolt that consumes the output of {@link RDFSpout}
 * and provides trees
 * 
 * @author Martin Koerner <info@mkoerner.de>, Olga Zagovora <zagovora@uni-koblenz.de>	
 *
 */
public class TreeCreatorBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	
	OutputCollector collector;

	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple tuple) {
		@SuppressWarnings("unchecked")
		ArrayList<String> triples = (ArrayList<String>) tuple.getValue(0);

		this.collector.emit(tuple,
				new Values(triples.get(0) + "\t" + triples.get(1) + "\t"
						+ triples.get(2)));

		this.collector.ack(tuple);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tree"));
	}

}
