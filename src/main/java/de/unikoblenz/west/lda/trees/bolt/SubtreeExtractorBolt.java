package de.unikoblenz.west.lda.trees.bolt;

import java.util.ArrayList;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import de.unikoblenz.west.lda.treeGeneration.Database;
import de.unikoblenz.west.lda.treeGeneration.RootNode;
import de.unikoblenz.west.lda.treeGeneration.SubtreeBuilder;
import de.unikoblenz.west.lda.treeGeneration.TreeTraversal;

/**
 * This class provides a storm bolt that consumes tree from
 * {@link SubtreeExtractorBolt} and returns a list of subtrees
 * 
 * @author Martin Koerner <info@mkoerner.de>, Olga Zagovora
 *         <zagovora@uni-koblenz.de>
 *
 */
public class SubtreeExtractorBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;

	OutputCollector collector;

	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple tuple) {
		Object rootNodeObject = tuple.getValue(0);
		if (rootNodeObject.getClass() != RootNode.class) {
			// TODO: better error handling
			System.out.println("tuple does not contain array");
			this.collector.ack(tuple);
			return;
		}

		RootNode rootNode = (RootNode) rootNodeObject;

		System.out.println("\nTree structure:");
		// TODO set parameters somewhere else
		// List<Subtree>subtrees=window.extractSubtrees(rootNode,100,10);
		// this.collector.emit(tuple, new Values(subtrees));
		// System.out.println("Size of list: "+subtrees.size());

		TreeTraversal traversal = new TreeTraversal(rootNode);
		Database db = new Database();
		SubtreeBuilder builder = new SubtreeBuilder(db);

		while (true) {
			ArrayList<Integer> path = traversal.getNextPath();
			if (path == null)
				break;
			builder.buildTrees(path);
		}

		this.collector.ack(tuple);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tree"));
	}

}
