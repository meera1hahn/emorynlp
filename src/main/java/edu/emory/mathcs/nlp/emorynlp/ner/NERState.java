/**
 * Copyright 2015, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.nlp.emorynlp.ner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Map.Entry;

import edu.emory.mathcs.nlp.emorynlp.component.eval.Eval;
import edu.emory.mathcs.nlp.emorynlp.component.eval.F1Eval;
import edu.emory.mathcs.nlp.emorynlp.component.node.NLPNode;
import edu.emory.mathcs.nlp.emorynlp.component.state.L2RState;
import edu.emory.mathcs.nlp.emorynlp.component.util.BILOU;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NERState<N extends NLPNode> extends L2RState<N>
{
	private String[] entityTags;
	private List<List<Double>> entityVectors;
	public NERState(N[] nodes)
	{
		super(nodes);
		entityTags = new String[nodes.length];
		entityVectors = new ArrayList<List<Double>>(nodes.length);
		getDBPediaTag(nodes);
		getWordVector(nodes);
	}
	
	private void getDBPediaTag(N[] nodes) {
		N node;
		int innerCount = 1;
		for(int i = 0; i < nodes.length; i++) {
			node = nodes[i];
			if (getLabel(node) != null) {
				String label = getLabel(node).substring(0, 1);
				switch(label) {
					case "U":
						entityTags[i] = NERConfig.dbpedia.get(node.getSimplifiedWordForm().toLowerCase());
						break;
					case "B":
						String full = node.getSimplifiedWordForm();
						if (innerCount + i == nodes.length) {
							entityTags[i] = NERConfig.dbpedia.get(node.getSimplifiedWordForm().toLowerCase());
							break;
						}
						node = nodes[innerCount + i];
						while(getLabel(node).substring(0, 1).equals("I")){
							full += "_" + node.getSimplifiedWordForm().toLowerCase();
							if (innerCount + i == nodes.length - 1) {
								break;
							}
							innerCount++;
							node = nodes[innerCount + i];
						}
						if(getLabel(node).substring(0, 1).equals("L")) {
							full += "_" + node.getSimplifiedWordForm().toLowerCase();
						}
						int j;
						for(j = i; j <= (i + innerCount); j++) {
							entityTags[j] = NERConfig.dbpedia.get(full.toLowerCase());
						}
						i = j;
						innerCount = 1;
						break;
				}
			}
		}
	}
	
	private void getWordVector(N[] nodes) {
		N node;
		for(int i = 0; i < nodes.length; i++) {
			node = nodes[i];
			if (getLabel(node) != null) {
				entityVectors.add(i, NERConfig.wordVectors.get(node.getSimplifiedWordForm().toLowerCase()));
			}
		}
	}
	public List<Double> getEntityVector(N node){
		return entityVectors.get(node.getID());
	}
	
	@Override
	protected String getLabel(N node)
	{
		return node.getNamedEntityTag();
	}
	
	@Override
	protected String setLabel(N node, String label)
	{
		String s = node.getNamedEntityTag();
		if(node.getID() > 0 && label != null)
		{
			BILOU tag = BILOU.toBILOU(label);
			BILOU prev = getBILOU(node.getID() -1);
			if(prev == BILOU.I && tag == BILOU.O)
				label = changeBILOU(BILOU.L, label);
		}
		node.setNamedEntityTag(label);
		Queue<String> history = new LinkedList<String>();
		if (NERConfig.wordHistory.get(node.getSimplifiedWordForm()) != null) {
			history = NERConfig.wordHistory.get(node.getSimplifiedWordForm());
			if (history.size() >= 2) {
				history.poll();
			}
		}
		history.add(label);
		NERConfig.wordHistory.put(node.getSimplifiedWordForm(), history);

		return s;
	}
	
	public void fixBILOU()
	{
		BILOU prev = null, next = null;
		String label = "", pLabel = "", nLabel = "";
		String chunk = null;
		for(int i = 0; i < nodes.length; i++)
		{
			label = nodes[i].getNamedEntityTag();
			//set up local tags
			BILOU tag = getBILOU(i);
			if(tag == null)
				continue;
			if(i > 0){
				prev = getBILOU(i-1);
				pLabel = nodes[i-1].getNamedEntityTag();
			}
			if(i < nodes.length -1)
			{
				next = getBILOU(i+1);
				nLabel = nodes[i+1].getNamedEntityTag();
			}
			
			//Things that shouldn't happen
				//[BI][UBO]
				//[UOL][IL]
			
			//Finding out what might be going wrong
			if(tag == BILOU.B || tag == BILOU.O || tag == BILOU.U)
			{
				if(chunk != null)//problem
				{
					System.out.println(chunk + " INT_BY " + label);
				}
				if(tag == BILOU.B)
					chunk = label;
				else
					chunk = null;
			}
			else if(tag == BILOU.I || tag == BILOU.L)
			{
				if(chunk == null)//problem
				{
					System.out.println(label + " FOLLOWED " + pLabel);
					if(tag == BILOU.I)
						chunk = label;
					else
						chunk = null;
				}
				else if(tag == BILOU.I)
					chunk += "_" + label;
				else
					chunk = null;
			}
			
			//CASES
			
			//BB
				//Possible approaches:
					//Make UB
//			label = changeBILOU(BILOU.U, label);
				
				
			//clean up
			prev = null; next = null;
//			nodes[i].setNamedEntityTag(label);
		}
    }
	
	public String changeBILOU(BILOU tag, String label)
	{
		return BILOU.toBILOUTag(tag, label.substring(1));
	}
	
	public BILOU getBILOU(int i)
	{
		String label = nodes[i].getNamedEntityTag();
		if(label != null && label != NLPNode.ROOT_TAG)
			return BILOU.toBILOU(label);
		else
			return null;
	}
	
	public String getAmbiguityClass(N node){
		return entityTags[node.getID()];
	}
	
	public String getWordHistory(N node){
		String first = "";
		String second = "";
		Queue<String> history = null;
		if (NERConfig.wordHistory.get(node.getSimplifiedWordForm()) != null) 
			history = NERConfig.wordHistory.get(node.getSimplifiedWordForm());
			if (history.size() >= 3) {
				first = history.poll();
			}
			if (history.size() >= 2) {
				second = history.peek();
			}
		return first + " "  + second;
	}

	@Override
	public void evaluate(Eval eval)
	{
		fixBILOU();
		Int2ObjectMap<String> gMap = BILOU.collectNamedEntityMap(oracle, String::toString, 1, nodes.length);
		Int2ObjectMap<String> sMap = BILOU.collectNamedEntityMap(nodes , this::getLabel  , 1, nodes.length);
		((F1Eval)eval).add(countCorrect(sMap, gMap), sMap.size(), gMap.size());
	}
	
	private int countCorrect(Int2ObjectMap<String> map1, Int2ObjectMap<String> map2)
	{
		int count = 0;
		String s2;
		
		for (Entry<Integer,String> p1 : map1.entrySet())
		{
			s2 = map2.get(p1.getKey());
			if (s2 != null && s2.equals(p1.getValue())) count++; 
		}
		
		return count;
	}
}
