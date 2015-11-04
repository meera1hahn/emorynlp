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
package edu.emory.mathcs.nlp.emorynlp.component.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public enum BILOU
{
	B,
	I,
	L,
	U,
	O;
	
	public static BILOU toBILOU(String tag)
	{
		return BILOU.valueOf(tag.substring(0,1));
	}

	public static String toBILOUTag(BILOU bilou, String tag)
	{
		return bilou+"-"+tag;
	}

	public static String toTag(String bilouTag)
	{
		return bilouTag.substring(2);
	}

	public static String changeChunkType(BILOU newBILOU, String tag)
	{
		return toBILOUTag(newBILOU, toTag(tag));
	}

	/**
	 * @param beginIndex inclusive
	 * @param endIndex exclusive
	 */
	public static <N>Int2ObjectMap<String> collectNamedEntityMap(N[] nodes, Function<N,String> f, int beginIndex, int endIndex)
	{
		Int2ObjectMap<String> map = new Int2ObjectOpenHashMap<>();
		int i, beginChunk = -1, size = nodes.length;
		String tag;
		List<String> tags = new ArrayList<String>();
		
		for (i=beginIndex; i<endIndex; i++)
		{
			tag = f.apply(nodes[i]);
			if (tag == null || tag.length() < 3) continue;
			
			switch (toBILOU(tag))
			{
			case U: 
				map.put(getKey(i,i,size), toTag(tag)); 
				beginChunk = -1; 
				break;
			case B: 
				beginChunk = i; 
				tags.add(toTag(tag)); 
				break;
			//case L: if (0 <= beginChunk&&beginChunk < i) map.put(getKey(beginChunk,i,size), toTag(tag)); beginChunk = -1; break;
			case L: 
				tags.add(toTag(tag));
				if (0 <= beginChunk&&beginChunk < i) {
					map.put(getKey(beginChunk,i,size),getBestTag(tags)); 
				}
				beginChunk = -1; 
				break;
			case O: 
				tags.add(toTag(tag)); 
				beginChunk = -1; 
				break;
			case I: 
				tags.add(toTag(tag));
				break;
			}
		}
		return map;
	}

	private static String getBestTag(List<String> tags) {
		//get most common tag
		return tags.get(tags.size() - 1);
	}

	private static int getKey(int beginIndex, int endIndex, int size)
	{
		return beginIndex * size + endIndex;
	}
}