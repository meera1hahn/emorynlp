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
package edu.emory.mathcs.nlp.emorynlp.pos;

import edu.emory.mathcs.nlp.emorynlp.component.node.NLPNode;
import edu.emory.mathcs.nlp.emorynlp.component.state.L2RState;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSState<N extends NLPNode> extends L2RState<N>
{
	private AmbiguityClassMap ambiguity_class_map;
	
	public POSState(N[] nodes, AmbiguityClassMap map)
	{
		super(nodes);
		setAmbiguityClass(map);
	}
	
	@Override
	protected String getLabel(N node)
	{
		return node.getPartOfSpeechTag();
	}
	
	@Override
	protected String setLabel(N node, String label)
	{
		String s = node.getPartOfSpeechTag();
		node.setPartOfSpeechTag(label);
		return s;
	}
	
	public String getAmbiguityClass(N node)
	{
		return ambiguity_class_map.get(node);
	}
	
	public void setAmbiguityClass(AmbiguityClassMap map)
	{
		ambiguity_class_map = map;		
	}
}
