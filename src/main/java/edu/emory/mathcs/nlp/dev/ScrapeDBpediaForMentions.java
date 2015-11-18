package edu.emory.mathcs.nlp.dev;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ScrapeDBpediaForMentions {
	private static BufferedReader br;


	public static void main(String[] args) {
	
		HashSet<String> names = new HashSet<String>();
		Set<String> female = new HashSet<String>();
		Set<String> male = new HashSet<String>();

	    String  currentLine;
	    Scanner s;
	    String lastEntity = "";
	    String currentEntity;
	    String part1;
	    String part2;
	    String tag;
	    		  
	    	      try{
	    	         br = new BufferedReader(new FileReader("/Users/meerahahn/Desktop/here.txt"));
	    	         while ((currentLine = br.readLine()) != null) {
	    	            s = new Scanner(currentLine);
	    	            male.add(s.next());
	    	            female.add(s.next());
	    	         }
	    	         System.out.println("MALE");
	    	         for(String m: male) {
	    	        	 System.out.println(m);
	    	         }
	    	         System.out.println("\nFEMALE");
	    	         for(String m: female) {
	    	        	 System.out.println(m);
	    	         }
	    	      }catch(Exception e){
	    	         e.printStackTrace();
	    	      }
	    	      
	    	      //test();
	    	     
	    		    
	}
	
	public static void addTags(String entity, Set<String> possibleTags, HashSet<String> names) {
		if(possibleTags.contains("Person")){
			for(String s: possibleTags) {
				names.add(getString(s.toLowerCase()));
			}
		}
	}
	
	public static String getString(String s) {
		s = s.replaceAll("\\d","");
		s = s.replace(".", "");
		s = s.replace(",", "");
		s = s.replace("%", "");
		s = s.replace("", "");
		return s;
	}
}
