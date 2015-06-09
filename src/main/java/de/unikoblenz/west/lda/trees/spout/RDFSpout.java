package de.unikoblenz.west.lda.trees.spout;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class RDFSpout extends BaseRichSpout {
	SpoutOutputCollector collector;
	Random rand;

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
		rand = new Random();
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
