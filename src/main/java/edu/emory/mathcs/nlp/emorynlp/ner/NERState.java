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

import java.util.HashMap;
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
	public NERState(N[] nodes)
	{
		super(nodes);
		String[] entityTags = new String[nodes.length];
		getDBPediaTag(nodes, entityTags);
	}
	
	private void getDBPediaTag(N[] nodes, String[] entityTags) {
		N node;
		int innerCount = 1;
		for(int i = 0; i < nodes.length; i++) {
			node = nodes[i];
			if (getLabel(node) != null) {
				String label = getLabel(node).substring(0, 1);
				switch(label) {
					case "U":
						//entityTags[i] = ambiguityMap.get(node.getSimplifiedWordForm());
						//System.out.println(node.getSimplifiedWordForm().toLowerCase());
						break;
					case "B":
						String full = node.getSimplifiedWordForm();
						if (innerCount + i == nodes.length) {
							//entityTags[i] = ambiguityMap.get(node.getSimplifiedWordForm());
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
						//System.out.println(full);
						int j;
						for(j = i; j <= (i + innerCount); j++) {
							//entityTags[j] = ambiguityMap.get(full);
						}
						i = j;
						innerCount = 1;
						break;
				}
			}
		}
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
		node.setNamedEntityTag(label);
		return s;
	}
	
	public String getAmbiguityClass(N node){
		if (getLabel(node) != null) {
			String label = getLabel(node).substring(0, 1);
			switch(label) {
				case "O":
					return null;
				case "U":
					return null;
				case "B":
					return null;
				case "I":
					return null;
				case "L":
					return null;
				default: return null;
			}
		}
		return null;
	}

	@Override
	public void evaluate(Eval eval)
	{
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
