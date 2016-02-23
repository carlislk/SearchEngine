import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;



public final class Indexer {
	public static final int FILE_COUNT = 44536;
	
	public static void main(String [] args) {
		long startTime = System.currentTimeMillis();
		HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	    HashMap<Integer, String> termid2term = new HashMap<Integer,String>();
	    HashMap<String, Integer> doc2docid = new HashMap<String, Integer>();
	    HashMap<Integer, Integer> docid2wordcount = new HashMap<Integer,Integer>();
	    HashMap<Integer, HashSet<Integer>> termid2doclist = new HashMap<Integer, HashSet<Integer>>();
	    HashMap<Integer, ArrayList<Integer>> docid2termlist = new HashMap<Integer, ArrayList<Integer>>();
	    // Term Frequency
	    HashMap<Integer, HashMap<Integer,Integer>> termid2tflist = new HashMap<Integer, HashMap<Integer,Integer>>();
	    HashMap<Integer, HashMap<Integer, Float>> termid2tfidf = new HashMap<Integer, HashMap<Integer,Float>>();
	    // Inverse Document Frequency
	    	// write
	    // Term Frequency Inverse Document Frequency
	    	// write
	    
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
		    	
		    	int wordcount = 0;
		    	// iterates over every word in the parsed html
		    	// adds words to term2termid and docid2termlist when appropriate
		    	for(String word : Utilities.tokenizeString(Jsoup.parse(input, "UTF-8").text())) {
		    		if(!term2termid.containsKey(word)) { // new word
		    			term2termid.put(word, termid++);
		    			int termid_index = term2termid.get(word);
		    			termid2doclist.put(termid_index, new HashSet<Integer>());
		    			termid2doclist.get(termid_index).add(docid);
		    			termid2tflist.put(termid_index, new HashMap<Integer,Integer>());
		    			termid2tflist.get(termid_index).put(new Integer(docid), new Integer(1));
		    		}
		    		else if(!termid2tflist.get(term2termid.get(word)).containsKey(docid)) {
		    			termid2tflist.get(term2termid.get(word)).put(new Integer(docid), new Integer(1));
		    			termid2doclist.get(term2termid.get(word)).add(docid);
		    		}
		    		else { 
		    			int termid_index = term2termid.get(word);
		    			termid2doclist.get(termid_index).add(docid);
		    			termid2tflist.get(termid_index).put(docid, new Integer(termid2tflist.get(termid_index).get(docid) + 1));
		    		}
		    		docid2termlist.get(docid).add(term2termid.get(word));
		    		wordcount++;
		    	}
		    	docid2wordcount.put(docid, wordcount);
		    	docid++;
		    	wordcount = 0;
			}
			
		    // this just reverses the term2termid to make the termid2term map
		    for(String key : term2termid.keySet()) {
		    	termid2term.put(term2termid.get(key), key);
		    }
		    
		    float tf = 0;
		    float idf = 0;
		    float tfidf = 0;
		    for(Integer term : term2termid.values()) {
		    	termid2tfidf.put(term, new HashMap<Integer, Float>());
		    	for(Integer doc : termid2doclist.get(term))
		    	{
		    		tf = (float) termid2tflist.get(term).get(doc) / (float) docid2wordcount.get(doc);
		    		idf = (float) Math.log10((float) FILE_COUNT / (float) termid2doclist.get(term).size());
		    		//tfidf = (float) (1 + Math.log10(tf) * Math.log10(idf));
		    		tfidf = tf * idf;
		    		termid2tfidf.get(term).put(new Integer(doc), new Float(tfidf));
		    		//System.out.println(tfidf);
		    	}
		    }
		    
		    long stopTime = System.currentTimeMillis();
		    long elapsedTime = stopTime - startTime;
		    System.out.println(elapsedTime);
		    
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
		    
		    /*System.out.println("docid2wordcount");
		    for(int id : docid2wordcount.keySet())
		    	System.out.println("{" + id + ": " + docid2wordcount.get(id) + "}");*/
		    
		    PrintWriter d2tlwriter = new PrintWriter("doc_termList.txt", "UTF-8");
		    d2tlwriter.println("docid2termlist index");
		    for(int id : docid2termlist.keySet()) 
		    	d2tlwriter.println("{" + id + docid2termlist.get(id).toString() + "}");
		    d2tlwriter.close();
		    
		    PrintWriter t2dlwriter = new PrintWriter("term_docList.txt", "UTF-8");
		    t2dlwriter.println("termid2doclist index");
		    for(int id : termid2doclist.keySet())
		    	t2dlwriter.println("{" + id + ": " + termid2doclist.get(id).toString() + "}"); 
		    t2dlwriter.close();
		    
		    PrintWriter t2tfwriter = new PrintWriter("term_docid-tf.txt", "UTF-8");
		    t2tfwriter.println("termid2tflist index");
		    for(int id : termid2tflist.keySet())
		    	t2tfwriter.println("{" + id + ": " + termid2tflist.get(id).toString() + "}");
		    t2tfwriter.close();
		    
		    PrintWriter t2tfidfwriter = new PrintWriter("term_docid-tfidf.txt", "UTF-8");
		    t2tfidfwriter.println("termid2tfidf index");
		    for(int id : termid2tfidf.keySet())
		    	t2tfidfwriter.println("{" + id + ": " + termid2tfidf.get(id).toString() + "}");
		    t2tfidfwriter.close();
	    } catch (NullPointerException e)
	    {
	    	e.printStackTrace();
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