package de.unikoblenz.west.lda.trees.bolt;

import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import de.unikoblenz.west.lda.treeGeneration.RootNode;
import de.unikoblenz.west.lda.treeGeneration.Window;
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
		Object treeObject=tuple.getValue(0);

		if(treeObject.getClass()!=int[].class){
			//TODO: better error handling
			System.out.println("tuple does not contain array");
			this.collector.ack(tuple);
			return;
		}

		int[]treeArray=(int[])treeObject;

		Window window=new Window();
		List<RootNode>rootNodes=window.buildTree(treeArray);
		for (RootNode rootNode : rootNodes) {
			this.collector.emit(tuple, new Values(rootNode));
		}
		this.collector.ack(tuple);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tree"));
	}

}
