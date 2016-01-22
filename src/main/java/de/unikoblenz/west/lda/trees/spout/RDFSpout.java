package de.unikoblenz.west.lda.trees.spout;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import de.unikoblenz.west.lda.input.DisjointSet;
import de.unikoblenz.west.lda.input.Reader;
import de.unikoblenz.west.lda.util.FilePathFormatter;


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
//		int[] testArray = new int[] {1,0,3,1,	11,0,5,1,	9,0,2,1,
//									 3,0,9,1,	1,0,6,1,	11,0,13,1,
//									 13,0,14,1,	13,0,15,1,	6,0,2,1,
//									 14,0,2,1,	2,0,11,1,	13,0,7,1};
//		this.collector.emit(new Values(testArray));


		//TODO: proper reading of directories
		String homeDir = System.getProperty("user.home");
		Reader reader=new Reader(new File(homeDir+FilePathFormatter.setSeparators("/data/btc2014/crawls/06/data.nq-0.gz")));
		//Reader reader=new Reader(new File("/home/martin/research-lab/data/btc2014/crawls/06/test"));
		try {
			List<DisjointSet>sets=reader.read();
			DisjointSet disjointSet=sets.get(0);
			System.out.println(disjointSet.getSize());
			this.collector.emit(new Values(disjointSet.getSet().toArray()));
			//System.out.println(disjointSet.getSet());
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
