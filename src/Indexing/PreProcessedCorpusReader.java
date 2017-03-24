package Indexing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import Classes.Path;

public class PreProcessedCorpusReader {

	
    BufferedReader reader = null;
    String line;
    String line_key;
    
	public PreProcessedCorpusReader(String type) throws IOException {
		// This constructor opens the pre-processed corpus file, Path.ResultHM1 + type
		// You can use your own version, or download from http://crystal.exp.sis.pitt.edu:8080/iris/resource.jsp
		// Close the file when you do not use it any more
		
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(Path.ResultHM1+type)));
		    
	}
	

	public Map<String, String> NextDocument() throws IOException {
		// read a line for docNo, put into the map with <"DOCNO", docNo>
		// read another line for the content , put into the map with <"CONTENT", content>


        if ((line = reader.readLine()) != null){
        	
        	Map<String, String> map = new HashMap<String, String>();
        	line_key = line; // docno as key
        	line = reader.readLine(); // turn to next line as content
        	map.put(line_key, line); // store to map 
        	return map;
        	
        }else{
        	
        	reader.close(); // close file
        	return null;
        }
        
    	
	}

}
