package de.unikoblenz.west.lda.trees;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import de.unikoblenz.west.lda.trees.bolt.SubtreeCounterBolt;
import de.unikoblenz.west.lda.trees.bolt.SubtreeExtractorBolt;
import de.unikoblenz.west.lda.trees.bolt.SubtreeFiltererBolt;
import de.unikoblenz.west.lda.trees.bolt.TreeCreatorBolt;
import de.unikoblenz.west.lda.trees.spout.SchemExSpout;

/**
 * This class creates the topology for extracting frequent subtrees from (nquad)
 * rdf data
 * 
 * @author Martin Koerner <info@mkoerner.de>
 *
 */
public class TreeTopology { 
	
	/**
	 * This is a basic example of a Storm topology.
	 */


	  public static void main(String[] args) throws Exception {
		    TopologyBuilder builder = new TopologyBuilder();

		    builder.setSpout("word", new SchemExSpout(), 10);
		    builder.setBolt("exclaim1", new TreeCreatorBolt(), 3).shuffleGrouping("word");
		    builder.setBolt("exclaim2", new SubtreeExtractorBolt(), 2).shuffleGrouping("exclaim1");
		    builder.setBolt("exclaim3", new SubtreeCounterBolt(), 2).shuffleGrouping("exclaim2");
		    builder.setBolt("exclaim4", new SubtreeFiltererBolt(), 2).shuffleGrouping("exclaim3");
		    
		    Config conf = new Config();
		    conf.setDebug(true);

		    if (args != null && args.length > 0) {
		      conf.setNumWorkers(3);

		      StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		    }
		    else {

		      LocalCluster cluster = new LocalCluster();
		      cluster.submitTopology("test", conf, builder.createTopology());
		      Utils.sleep(10000);
		      cluster.killTopology("test");
		      cluster.shutdown();
		    }
		  }

}
