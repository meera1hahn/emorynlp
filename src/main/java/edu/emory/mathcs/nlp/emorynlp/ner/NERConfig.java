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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.nlp.emorynlp.component.config.NLPConfig;
import edu.emory.mathcs.nlp.emorynlp.component.node.NLPNode;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NERConfig extends NLPConfig<NLPNode>
{
	public static Map<String, String> dbpedia;
	public static Map<String, List<Double>> wordVectors;
	public static Map<String, String> wordHistory;

	
	public NERConfig() {}
	
	public NERConfig(InputStream in)
	{
		super(in);
		initDbpedia();
		initVectors();
		initHistory();
	}
	
	public void initDbpedia()
	{
		String path = "dat/entityTagMap.data";
	    try {
			FileInputStream f_in = new FileInputStream(path);
  	      	ObjectInputStream obj_in = new ObjectInputStream (f_in);
  	       dbpedia = (Map<String, String>)obj_in.readObject();
  	       obj_in.close();
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	public void initVectors()
	{
		String path = "/home/azureuser/data/entityVectorMap.data";
	    try {
			FileInputStream f_in = new FileInputStream(path);
  	      	ObjectInputStream obj_in = new ObjectInputStream (f_in);
  	        wordVectors = (Map<String, List<Double>>)obj_in.readObject();
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	public void initHistory()
	{
		wordHistory = new HashMap<String, String>();
	}
}
