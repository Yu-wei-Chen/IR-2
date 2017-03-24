package Indexing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import Classes.Path;

public class MyIndexReader {
	// We suggest you to write very efficient code here, otherwise, your memory
	// cannot hold our corpus...
	String path_index = "";
	String path_indexdocno = "";
	BufferedReader reader = null; // read indexdocno
	Map<Integer, String> index_docno = null; // the map store id & docno
	String line;
	BufferedReader reader_index = null; // read index
	int postinglist[][] = null;// posting list
	String line_index;

	public MyIndexReader(String type) throws IOException {
		// read the index files you generated in task 1
		// remember to close them when you finish using them
		// use appropriate structure to store your index

		// set path
		if (type.equals("trecweb")) {
			path_index = Path.IndexWebDir + "index";
			path_indexdocno = Path.IndexWebDir + "indexdocno";
		} else {
			path_index = Path.IndexTextDir + "index";
			path_indexdocno = Path.IndexTextDir + "indexdocno";
		}

		// read id & docno index
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(path_indexdocno)));
		index_docno = new HashMap<Integer, String>();

		while ((line = reader.readLine()) != null) {
			String iddoc[] = line.split(","); // split line by ","
			index_docno.put(Integer.parseInt(iddoc[0]), iddoc[1]); // put into
																	// map
		}
		reader.close(); // close reader

	}

	// get the non-negative integer dociId for the requested docNo
	// If the requested docno does not exist in the index, return -1
	public int GetDocid(String docno) { // no use this function
		for (Map.Entry<Integer, String> e : index_docno.entrySet()) {// get all
																		// the
																		// key
																		// and
																		// value
			Integer key = e.getKey();
			String value = e.getValue();
			if (value == docno) {
				return key;
			}
		}
		return -1;
	}

	// Retrieve the docno for the integer docid
	public String GetDocno(int docid) {
		String docno = index_docno.get(docid);// return document number
		return docno;
	}

	/**
	 * Get the posting list for the requested token.
	 * 
	 * The posting list records the documents' docids the token appears and
	 * corresponding frequencies of the term, such as:
	 * 
	 * [docid] [freq] 1 3 5 7 9 1 13 9
	 * 
	 * ...
	 * 
	 * In the returned 2-dimension array, the first dimension is for each
	 * document, and the second dimension records the docid and frequency.
	 * 
	 * For example: array[0][0] records the docid of the first document the
	 * token appears. array[0][1] records the frequency of the token in the
	 * documents with docid = array[0][0] ...
	 * 
	 * NOTE that the returned posting list array should be ranked by docid from
	 * the smallest to the largest.
	 * 
	 * @param token
	 * @return
	 */
	public int[][] GetPostingList(String token) throws IOException {
		return postinglist; // return posting list
	}

	// Return the number of documents that contains the token.
	public int GetDocFreq(String token) throws IOException {
		reader_index = new BufferedReader(new InputStreamReader(new FileInputStream(path_index)));
		int flag = 0; // store or not
		Map<Integer, Integer> posting = new HashMap<Integer, Integer>();
		while ((line_index = reader_index.readLine()) != null) {
			if (line_index.equals(token)) { // find token and set flag = 1
				flag = 1;
			} else if (line_index.indexOf(",") == -1) { // set flag = 0 when
														// line_index not
														// contain ","
				flag = 0;
			} else if (flag == 1) {
				String match_token[] = line_index.split(",");
				posting.put(Integer.parseInt(match_token[0]), Integer.parseInt(match_token[1]));
			}
		}
		int postinglist_size = posting.size();
		postinglist = new int[postinglist_size][2]; // build posting list
		Map<Integer, Integer> sort_map = new TreeMap<Integer, Integer>(posting); // sort by key
		if (postinglist_size != 0) {
			int j = 0;
			for (int key : sort_map.keySet()) { // build posting list array
				postinglist[j][0] = key;
				postinglist[j][1] = sort_map.get(key);
				j++;
			}
			return postinglist_size;
		} else {
			return 0;
		}

	}

	// Return the total number of times the token appears in the collection.
	public long GetCollectionFreq(String token) throws IOException {
		if (postinglist.length != 0) {
			int ctf = 0;
			for (int k = 0; k < postinglist.length; k++) {
				ctf = ctf + postinglist[k][1]; // sum of the frequency
			}
			return ctf;
		}
		return 0;
	}

	public void Close() throws IOException {
		reader_index.close();
	}

}