package edu.emory.mathcs.nlp.dev;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScrapeWordVectors {
	private static BufferedReader br;


	public static void main(String[] args) {
	
		HashMap<String, List<Double>> entityToTags = new HashMap<String, List<Double>>();
		Set<String> possibleTags = new HashSet<String>();
	    String  currentLine;
   	 	String[] vector;
	    String entityName = "";
	    List<Double> entityVector = new ArrayList<Double>(); 
	    		  
	    	      try{
	    	    	 int current = 1;
	    	         br = new BufferedReader(new FileReader("/Users/meerahahn/Desktop/Development/NLP_Research/nerdata/vectors_for_ner.txt"));
	    	         while ((currentLine = br.readLine()) != null) {
	    	        	if(current % 2 != 0) {
	    	        		entityName = currentLine.trim();
	    	        	} else {
	    	        		vector = currentLine.split(",");
	    	        		for(String str: vector) {
	    	        			entityVector.add(Double.valueOf(str));
	    	        		}
	    	        		entityToTags.put(entityName, entityVector);
	    	        		System.out.println("put");
	    	        	}
	    	        	current++;
	    	        	entityVector.clear();
	    	         }
	    	         // Write to disk with FileOutputStream
	    	         FileOutputStream f_out = new FileOutputStream("/Users/meerahahn/Desktop/Development/NLP_Research/nerdata/entityVectorMap.data");
	 	    	         // Write object with ObjectOutputStream
	 	    	         ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
	 	    	         // Write object out to disk
	 	    	         obj_out.writeObject(entityToTags);
	 	    	         obj_out.close();
	    	      }catch(Exception e){
	    	         e.printStackTrace();
	    	      }    
	}
}

