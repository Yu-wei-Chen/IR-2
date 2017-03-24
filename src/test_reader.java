import static org.junit.Assert.*;

import java.awt.Window.Type;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import Classes.Path;

public class test_reader {
	String type = "trecweb";
	String token = "acow";
	int ctf=0;
	String docno;
	int df=0;
	
	String path_index = "";
	String path_indexdocno = "";
    BufferedReader reader = null; // read indexdocno
    Map<Integer, String> index_docno = null; // the map store id & docno
	String line;
    BufferedReader reader_index = null; // read index
	int postinglist[][] = null;// posting list
	String line_index;
	
	
	@Test
	public void test() throws IOException {
		
		// make the path to web or text
		if(type.equals("trecweb")){
			path_index = Path.IndexWebDir + "index";
			path_indexdocno = Path.IndexWebDir + "indexdocno";
		}else{
			path_index = Path.IndexTextDir + "index";
			path_indexdocno = Path.IndexTextDir + "indexdocno";
		}
		
		// read id & docno index
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(path_indexdocno)));
		index_docno = new HashMap<Integer, String>();
		
		while ((line = reader.readLine()) != null){
			String iddoc[] = line.split(","); // split line by ","
			//System.out.println(iddoc[0]+iddoc[1]);
			index_docno.put(Integer.parseInt(iddoc[0]), iddoc[1]); // put into map
		}
		reader.close(); // close reader
		
		//------------------------------------------------------------------   main

		reader_index = new BufferedReader(new InputStreamReader(new FileInputStream(path_index)));
		int flag = 0; // store or not
		Map<Integer, Integer> posting = new HashMap<Integer, Integer>();
		while((line_index = reader_index.readLine()) != null){
			if(line_index.equals(token)){ // find token and set flag = 1
				flag = 1;
			}else if(line_index.indexOf(",") == -1){ // set flag = 0 when line_index not contain ","
				flag = 0;
			}else if(flag == 1){
				String match_token[] = line_index.split(",");
				posting.put(Integer.parseInt(match_token[0]), Integer.parseInt(match_token[1]));
				//System.out.println(match_token[0]+"-"+match_token[1]);
			}
		}
		int postinglist_size = posting.size();
		postinglist = new int[postinglist_size][2]; // build posting list
		Map<Integer, Integer> sort_map = new TreeMap<Integer, Integer>(posting); // sort by key 
		if (postinglist_size != 0){
			int j = 0;
			for (int key : sort_map.keySet() ) {
				postinglist[j][0] = key;
				postinglist[j][1] = sort_map.get(key);
				j++;
			}
			df=j;
			System.out.println("return postinglist"+postinglist_size);
			/*for (int i=0;i<postinglist_size;i++){
				System.out.println(postinglist[i][0]+"---"+postinglist[i][1]);
			}*/
		}else{
			System.out.println("return 0");	
		}
		// -------------------------------------------------------GetDocFreq
		
		if (postinglist.length != 0){
			//System.out.println("Yes"+postinglist.length);	
			ctf=0; //int 
			for(int k = 0;k < postinglist.length;k++){
				ctf = ctf + postinglist[k][1];
			}
			//return ctf;
			System.out.println(ctf);	
		}
		// -------------------------------------------------------GetCollectionFreq
		System.out.println(" >> the token \""+token+"\" appeared in "+postinglist_size+" documents and "+ctf+" times in total");
		
		if (postinglist.length != 0){
			System.out.println("postinglist");	
		}else{
			System.out.println("no postinglist");
		}
		// -------------------------------------------------------GetPostingList
		if(df>0){
			for(int ix=0;ix<postinglist.length;ix++){
				int docid = postinglist[ix][0];
				int freq = postinglist[ix][1];
				//----------------------------------GetDocno(docid);
				
				
				docno = index_docno.get(docid);
				
				//return docnoDict.get(docid);
				
				
				
				//----------------------------------GetDocno(docid);
				System.out.printf("    %20s    %6d    %6d\n", docno, docid, freq);
				
				
				//-------------------------------------GetDocid
				for (Map.Entry<Integer, String> e : index_docno.entrySet()) {
					Integer key = e.getKey();
				    String value = e.getValue();
				    if (value == docno) {
						System.out.println(key);
					}
				}
				
				
			}
		}
		
		
		
		
		
		
	}

}
