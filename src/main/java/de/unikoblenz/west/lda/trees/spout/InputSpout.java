package de.unikoblenz.west.lda.trees.spout;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class InputSpout extends BaseRichSpout {
	  SpoutOutputCollector collector;
	  Random rand;

	  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
	    this.collector = collector;
	    rand = new Random();
	  }

	  public void nextTuple() {
	    Utils.sleep(100);
	   /* String[] sentences = new String[]{ "the cow jumped over the moon", "an apple a day keeps the doctor away",
	        "four score and seven years ago", "snow white and the seven dwarfs", "i am at two with nature" };
	    String sentence = sentences[_rand.nextInt(sentences.length)];*/
	    ArrayList<String> triplesList = new ArrayList<String>();
	    //Insert Triples here
	    triplesList.add("123");
	    triplesList.add("456");
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
