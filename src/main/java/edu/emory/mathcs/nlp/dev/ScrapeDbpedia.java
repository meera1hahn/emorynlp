package edu.emory.mathcs.nlp.dev;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ScrapeDbpedia {
	private static BufferedReader br;


	public static void main(String[] args) {
	
		HashMap<String, String> entityToTags = new HashMap<String, String>();
		Set<String> possibleTags = new HashSet<String>();
	    String  currentLine;
	    Scanner s;
	    String lastEntity = "";
	    String currentEntity;
	    String part1;
	    String part3;
	    String tag;
	    		  
	    	      try{
	    	         br = new BufferedReader(new FileReader("/Users/meerahahn/Desktop/Development/NLP_Research/nerdata/instance_types_en.nt"));
	    	         while ((currentLine = br.readLine()) != null) {
	    	         //for(int i = 0; i < 200; i++) {
	    	        	//currentLine = br.readLine();
	    	            s = new Scanner(currentLine);
	    	            part1 = s.next();
	    	            s.next();
	    	            part3 = s.next();
	    	            if(part1.length() > 30) {
	    	            	currentEntity = part1.substring(29, part1.length() - 1);
		    	        	if(!lastEntity.equals("") && !lastEntity.equals(currentEntity)){
		    	        		addTags(lastEntity, possibleTags, entityToTags);
		    	        		//if(entityToTags.get(lastEntity) != null) {
		    	        			//System.out.println(lastEntity + " " + entityToTags.get(lastEntity).toString());
		    	        		//}
		    	        		possibleTags.clear();
		    	        	}
	    	            } else {
	    	            	continue;
	    	            }
	    	            if (part3.contains("<http://dbpedia.org/ontology/") && part3.length() > 30) {
	    	            	tag = part3.substring(29, part3.length() - 1);
	    	            	possibleTags.add(tag);
	    	            } else {
	    	            	continue;
	    	            }
	    	            lastEntity = currentEntity;
	    	         } 
	    	         // Write to disk with FileOutputStream
	    	         FileOutputStream f_out = new 
	    	         	FileOutputStream("/Users/meerahahn/Desktop/Development/NLP_Research/nerdata/entityTagMap.xz");

	    	         // Write object with ObjectOutputStream
	    	         ObjectOutputStream obj_out = new
	    	         	ObjectOutputStream (f_out);

	    	         // Write object out to disk
	    	         obj_out.writeObject(entityToTags);
	    	         obj_out.close();
	    	         
	    	      }catch(Exception e){
	    	         e.printStackTrace();
	    	      }
	}
	
	
	public static void addTags(String entity, Set<String> possibleTags, Map<String, String> entityToTags) {
		if(possibleTags.contains("Person")){
			entityToTags.put(entity.toLowerCase(), "PER");
			return;
		}
		if(possibleTags.contains("Organisation")){
			entityToTags.put(entity.toLowerCase(), "ORG");
			return;
		}
		if(possibleTags.contains("Place")){
			entityToTags.put(entity.toLowerCase(), "LOC");
			return;
		}
		return;
	}
}
