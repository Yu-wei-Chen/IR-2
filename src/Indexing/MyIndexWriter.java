package Indexing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import Classes.Path;

public class MyIndexWriter {
	// We suggest you to write very efficient code here, otherwise, your memory cannot hold our corpus...
	
	String TextOrWeb = ""; //  path of text file or web file
	
    BufferedWriter IdWriter = null; 
    BufferedWriter IndexWriter = null; 
    
    Map<Integer, String> DocIdNO = new HashMap<Integer, String>(); //  non-negative integer docId to each document
    Map<String, HashMap<Integer, Integer>> index = new HashMap<String, HashMap<Integer, Integer>>(); // index structure
	
    int DocNum = 1; // count processed document quantity
    int tempNum = 1; // count spread index file quantity
    
    BufferedReader reader;// read temporary index file
    
	public MyIndexWriter(String type) throws IOException {
		// This constructor should initiate the FileWriter to output your index files
		// remember to close files if you finish writing the index
		
		if(type.equals("trecweb")){ // text file or web file
			File indexwebDir = new File(Path.IndexWebDir);
			indexwebDir.mkdir(); // create new folder
			TextOrWeb = Path.IndexWebDir; // set path for web or text
		}
		else{
			File indextextDir = new File(Path.IndexTextDir);
			indextextDir.mkdir();
			TextOrWeb = Path.IndexTextDir;	
		}
		
		// set writer path
		IdWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TextOrWeb + "indexdocno")));
        IndexWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TextOrWeb + "index")));
		
	}
	
	public void IndexADocument(String docno, String content) throws IOException {
		// you are strongly suggested to build the index by installments
		// you need to assign the new non-negative integer docId to each document, which will be used in MyIndexReader
		// reference by https://goo.gl/aqmVoc
		
		DocIdNO.put(DocNum, docno); // assign the new non-negative integer docId to each document
		
		 String[] words = content.split(" ");  // split content
		 
         for (String word : words) {  
             if (!index.containsKey(word)) {  // not exist
            	 HashMap<Integer, Integer> subIndex = new HashMap<Integer, Integer>();  
                 subIndex.put(DocNum, 1);  // store document number and frequency
                 index.put(word, subIndex);  // store term with document number and frequency
             
             }else{  
            	 HashMap<Integer, Integer> subIndex = index.get(word);  //exist then get value
            	 if (subIndex.containsKey(DocNum)) {  
                     int count = subIndex.get(DocNum);  
                     subIndex.put(DocNum, count+1);  
                 } else {  
                     subIndex.put(DocNum, 1);  
                 }  
             }
         }
         DocNum++; // count document
         
         if (DocNum % 40000 == 0){ // every 40000 documents store to one file in disk
        	 
        	for (int key:DocIdNO.keySet()){ // write the new integer document Id for each document
        		IdWriter.write(key +","+ DocIdNO.get(key) +"\n");
        	}
        	
     		BufferedWriter tmp_write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TextOrWeb +"tem_index"+tempNum)));
     		
     		for (String key1:index.keySet()){  
     			tmp_write.write(key1 +"\n"); // write term into index file
     			
     			HashMap<Integer, Integer> subIndex = index.get(key1); // get docnum with quantity
     			for (int key2:subIndex.keySet()){
     				tmp_write.write(key2 +","+ subIndex.get(key2) +"\n");
     			}
        	}
     		tempNum++; // spread index quantity
     		DocIdNO.clear(); // cleaned for the next block of documents
     		index.clear();	// cleaned for the next block of documents
     		tmp_write.close();
         }
         
	}
	
	public void Close() throws IOException {
		// close the index writer, and you should output all the buffered content (if any).
		// if you write your index into several files, you need to fuse them here.
		
		// write the rest index to disk
		for (int key:DocIdNO.keySet()){ 
    		IdWriter.write(key +","+ DocIdNO.get(key) +"\n");
    	}
    	
 		BufferedWriter tmp_write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TextOrWeb +"tem_index"+tempNum)));
 		
 		for (String key1:index.keySet()){  
 			tmp_write.write(key1 +"\n"); // write term into index file
 			
 			HashMap<Integer, Integer> subIndex = index.get(key1); // get documents number with frequency
 			for (int key2:subIndex.keySet()){
 				tmp_write.write(key2 +","+ subIndex.get(key2) +"\n"); // write documents number and frequency to index file
 			}
    	}
 		DocIdNO.clear(); // cleaned for the next block of documents
 		index.clear();	// cleaned for the next block of documents
 		tmp_write.close();
		
		// fuse the temporary index file to one file
 		for (int a=1; a<=tempNum;a++){
 			// get the temporary index file
 			reader = new BufferedReader(new InputStreamReader(new FileInputStream(TextOrWeb +"tem_index"+a)));
 			
 			String line;
 			while ((line = reader.readLine()) != null){
 				IndexWriter.write(line+"\n");
 			}
 			
 			reader.close(); // save
 			
			File file = new File(TextOrWeb +"tem_index"+a); // get the temporary index file
			file.delete(); // delete the fused temporary index file
 		}
		
 		// close writer

 		IdWriter.close();
 		IndexWriter.close();
	}
	
}
