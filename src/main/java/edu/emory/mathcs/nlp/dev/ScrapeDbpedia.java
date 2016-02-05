package edu.emory.mathcs.nlp.dev;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ScrapeDbpedia {
	private static BufferedReader br;


	public static void main(String[] args) throws FileNotFoundException {
        br = new BufferedReader(new FileReader("/Users/meerahahn/Downloads/dev.tsv"));
        BufferedWriter output = null;
        File file = new File("/Users/meerahahn/Desktop/devEval.txt");
        try {
			output = new BufferedWriter(new FileWriter(file));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String currentLine;
        try {
			while ((currentLine = br.readLine()) != null) {
				//currentLine = currentLine.replaceAll("\\d","");
				currentLine = currentLine.trim();
				currentLine = currentLine.substring(currentLine.length()-1);
		        output.write(currentLine + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
//	
//		HashMap<String, String> entityToTags = new HashMap<String, String>();
//		Set<String> possibleTags = new HashSet<String>();
//	    String  currentLine;
//	    Scanner s;
//	    String lastEntity = "";
//	    String currentEntity;
//	    String part1;
//	    String part3;
//	    String tag;
//	    		  
//	    	      try{
//	    	         br = new BufferedReader(new FileReader("/Users/meerahahn/Desktop/Development/NLP_Research/nerdata/instance_types_en.nt"));
//	    	         while ((currentLine = br.readLine()) != null) {
//	    	         //for(int i = 0; i < 600; i++) {
//	    	        	//currentLine = br.readLine();
//	    	            s = new Scanner(currentLine);
//	    	            part1 = s.next();
//	    	            s.next();
//	    	            part3 = s.next();
//	    	            if(part1.length() > 30) {
//	    	            	currentEntity = part1.substring(29, part1.length() - 1);
//		    	        	if(!lastEntity.equals("") && !lastEntity.equals(currentEntity)){
//		    	        		addTags(lastEntity, possibleTags, entityToTags);
//		    	        		possibleTags.clear();
//		    	        	}
//	    	            } else {
//	    	            	continue;
//	    	            }
//	    	            if (part3.contains("<http://dbpedia.org/ontology/") && part3.length() > 30) {
//	    	            	tag = part3.substring(29, part3.length() - 1);
//	    	            	possibleTags.add(tag);
//	    	            } else {
//	    	            	continue;
//	    	            }
//	    	            lastEntity = currentEntity;
//	    	         } 
//	    	         // Write to disk with FileOutputStream
//	    	         FileOutputStream f_out = new 
//	    	         	FileOutputStream("/Users/meerahahn/Desktop/Development/NLP_Research/nerdata/entityTagMap.data");
//
//	    	         // Write object with ObjectOutputStream
//	    	         ObjectOutputStream obj_out = new
//	    	         	ObjectOutputStream (f_out);
//
//	    	         // Write object out to disk
//	    	         obj_out.writeObject(entityToTags);
//	    	         obj_out.close();
//	    	         
//	    	      }catch(Exception e){
//	    	         e.printStackTrace();
//	    	      }
//	    	      
//	    	      //test();
//	    		    
//	}
//	public static void test() {
//		 // Read from disk using FileInputStream
//	      String path = "/Users/meerahahn/Desktop/Development/NLP_Research/nerdata/entityTagMap.data";
//	      Map<String, String> dbpedia = null;
//		    try {
//				FileInputStream f_in = new FileInputStream(path);
//	  	      	ObjectInputStream obj_in = new ObjectInputStream (f_in);
//	  	       dbpedia = (Map<String, String>)obj_in.readObject();
//	  	       obj_in.close();
//			} catch (IOException | ClassNotFoundException e1) {
//				e1.printStackTrace();
//			} 
//		    
//		    System.out.println(dbpedia.size());
//		    for (Map.Entry<String,String> entry : dbpedia.entrySet()) {
//		    	  System.out.print(entry.getKey() + " ");
//		    	  System.out.println(entry.getValue());
//		    }
//	}
//	
//	
//	public static void addTags(String entity, Set<String> possibleTags, Map<String, String> entityToTags) {
//		if(possibleTags.contains("Person")){
//			entityToTags.put(getString(entity.toLowerCase()), "PER");
//			return;
//		}
//		if(possibleTags.contains("Organisation")){
//			entityToTags.put(getString(entity.toLowerCase()), "ORG");
//			return;
//		}
//		if(possibleTags.contains("Place")){
//			entityToTags.put(entity.toLowerCase(), "LOC");
//			return;
//		}
//		return;
//	}
//	
//	public static String getString(String s) {
//		s = s.replaceAll("\\d","");
//		s = s.replace(".", "");
//		s = s.replace(",", "");
//		s = s.replace("%", "");
//		s = s.replace("", "");
//		s = s.trim();
//		return s;
//	}
//}
