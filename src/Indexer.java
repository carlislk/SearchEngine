import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Iterator;



public final class Indexer {
	public static final int FILE_COUNT = 44536;
	
	public static void main(String [] args){//static void main(String [] args) {
		HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	    HashMap<Integer, String> termid2term = new HashMap<Integer,String>();
	    HashMap<String, Integer> doc2docid = new HashMap<String, Integer>();
	    HashMap<Integer, ArrayList<Integer>> docid2termlist = new HashMap<Integer, ArrayList<Integer>>();
	    int termid = 1;
	    int docid = 0;
	    JSONParser parser = new JSONParser();
	    
	    //searches the directory that you are using for storing the pages
	    //change it to your own
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir") + "/data/Html/");
	    
	    try {
	    	// parses the json file
	    	Object obj = parser.parse(new FileReader(System.getProperty("user.dir") + "/data/html_files.json"));
	    	// creates a json object out of the json file
	    	JSONObject jsonObject = (JSONObject) obj;
			
	    	// the json file isn an array of json objects, its actually a json object of many json objects
	    	// these inner json objects are indexed by a number which will be used as the docid
	    	// the i in the for loop will be used as the index into these inner json objects, which have url and file fields
			for(Integer i = 0; i < FILE_COUNT;  i++)
			{
				// indexes on i to get the page from the json object
				JSONObject page = (JSONObject)jsonObject.get(i.toString());
				// the file field from the json is used as the key for our document names
				String key = (String)page.get("file");
				// used for jsoup to parse the html
				File input = new File(System.getProperty("user.dir") + "/data/Html/" + key);
				
				
				if(!doc2docid.containsKey(key)) // adds document to doc2docid map
		    		doc2docid.put(key, docid);
		    	docid2termlist.put(docid, new ArrayList<Integer>()); // allocate new arraylist for the doc's termlist
		    	
		    	// iterates over every word in the parsed html
		    	// adds words to term2termid and docid2termlist when appropriate
		    	for(String word : Utilities.tokenizeString(Jsoup.parse(input, "UTF-8").text())) {
		    		if(!term2termid.containsKey(word))
		    			term2termid.put(word, termid++);
		    		docid2termlist.get(docid).add(term2termid.get(word));
		    	}
		    	docid++;
			}
			
		    // this just reverses the term2termid to make the termid2term map
		    for(String key : term2termid.keySet()) {
		    	termid2term.put(term2termid.get(key), key);
		    }
		    
		    // print statements are commented out to save time
		    /*System.out.print("term2termid: {");
		    for(String key : term2termid.keySet())
		    	System.out.println(key + ": " + term2termid.get(key) + ", ");
		    System.out.println("}");*/
		    
		    /*
		    System.out.print("termid2term: {");
		    for(int key : termid2term.keySet())
		    	System.out.print(key + ": " + termid2term.get(key) + ", ");
		    System.out.println("}");*/
		    
		    /*
		    System.out.print("doc2docid: {");
		    for(String key : doc2docid.keySet())
		    	System.out.print(key + ": " + doc2docid.get(key) + ", ");
		    System.out.println("}");*/
		    
		    /*
		    System.out.println("docid2termlist:");
		    for(int id : docid2termlist.keySet()) {
		    	System.out.print("{" + id + " [");
		    	for(int newid : docid2termlist.get(id)) {
		    		System.out.print(newid + ", " );
		    	}
		    	System.out.println("]}");
			}*/
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
	    /* The old way of doing it, before the json
	     *  just saved this in case something is wrong with the json way
	    File dir = new File(System.getProperty("user.dir") + "/data/Html/");
	    File [] paths = dir.listFiles();
	    for(File path : paths) {
	    	if(!doc2docid.containsKey(path.toString())) 
	    		doc2docid.put(path.toString(), docid);
	    	docid2termlist.put(docid, new ArrayList<Integer>());
	    	for(String word : Utilities.tokenizeString(Jsoup.parse(path, "UTF-8").text())) {
	    		if(!term2termid.containsKey(word))
	    			term2termid.put(word, termid++);
	    		docid2termlist.get(docid).add(term2termid.get(word));
	    	}
	    	docid++;
	    }*/
	}
}