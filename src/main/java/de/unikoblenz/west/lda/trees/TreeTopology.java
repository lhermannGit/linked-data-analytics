package de.unikoblenz.west.lda.trees;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import de.unikoblenz.west.lda.trees.bolt.SubtreeExtractorBolt;
import de.unikoblenz.west.lda.trees.bolt.TreeCreatorBolt;
import de.unikoblenz.west.lda.trees.spout.RDFSpout;

/**
 * This class creates the topology for extracting frequent subtrees from (nquad)
 * rdf data
 * 
 * @author Martin Koerner <info@mkoerner.de>, Olga Zagovora <zagovora@uni-koblenz.de>	
 *
 */
public class TreeTopology { 


	  public static void main(String[] args) throws Exception {
		    TopologyBuilder builder = new TopologyBuilder();
		    
		    //Amount of Threads per Bolt should be changed
		    builder.setSpout("RDFSpout", new RDFSpout(), 10);
		    builder.setBolt("TreeCreatorBolt", new TreeCreatorBolt(), 3).shuffleGrouping("RDFSpout");
		    builder.setBolt("SubtreeExtractorBolt", new SubtreeExtractorBolt(), 2).shuffleGrouping("TreeCreatorBolt");
		    
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
