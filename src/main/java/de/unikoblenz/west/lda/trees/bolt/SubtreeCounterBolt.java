package de.unikoblenz.west.lda.trees.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.streaminer.stream.frequency.FrequencyException;
import org.streaminer.stream.frequency.LossyCounting;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * 
 * @author Martin Koerner <info@mkoerner.de>, Olga Zagovora <zagovora@uni-koblenz.de>	
 *
 */
public class SubtreeCounterBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	
	OutputCollector collector;
	private LossyCounting<int[]> lossyCounting;
	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;

		//TODO: set errorRate in a smart way
		this.lossyCounting = new LossyCounting<int[]>(
				0.001);
	}

	public void execute(Tuple tuple) {
		Object subtreeObject=tuple.getValue(0);
		if(subtreeObject.getClass()!=java.util.ArrayList.class){
			//TODO: better error handling
			System.out.println("tuple does not contain list of arrays");
			System.out.println("class: "+subtreeObject.getClass().getName());
			this.collector.ack(tuple);
			return;
		}
		@SuppressWarnings("unchecked")
		List<int[]> subtrees=(List<int[]>)subtreeObject;
		
        for(int[]subtree:subtrees){
        	try {
				lossyCounting.add(subtree);
			} catch (FrequencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	
        System.out.println("Lossy counting size: "+lossyCounting.size());
        System.out.println("actual size: "+lossyCounting.keySet().size());
		//this.collector.emit(tuple, new Values(treeWithCount));

		this.collector.ack(tuple);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("treeCounts"));
	}

}
