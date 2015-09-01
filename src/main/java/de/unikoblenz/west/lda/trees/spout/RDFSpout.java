package de.unikoblenz.west.lda.trees.spout;

import java.util.ArrayList;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;


/**
 * 
 * @author Olga Zagovora <zagovora@uni-koblenz.de>	
 *
 */


public class RDFSpout extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	
	SpoutOutputCollector collector;

	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void nextTuple() {
		Utils.sleep(500);
//		ArrayList<String> triplesList = new ArrayList<String>();
//		// Insert Triples here
//		triplesList
//				.add("<http://example.com/tim> <http://example.com/likes> <http://example.com/car>");
//		triplesList
//				.add("<http://example.com/tim> <http://example.com/likes> <http://example.com/cake>");
//		triplesList
//				.add("<http://example.com/cake> <http://example.com/is> <http://example.com/sweet>");
//		this.collector.emit(new Values(triplesList));
		int[] testArray = new int[] {1,0,3,1,	11,0,5,1,	9,0,2,1,
									 3,0,9,1,	1,0,6,1,	11,0,13,1,
									 13,0,14,1,	13,0,15,1,	6,0,2,1,
									 14,0,2,1,	2,0,11,1,	13,0,7,1};
		this.collector.emit(new Values(testArray));

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
