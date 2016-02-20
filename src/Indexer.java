import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;


public final class Indexer {
	public static void main(String [] args) {
		
		//searches the directory that you are using for storing the pages
	    //change it to your own
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir") + "/data/Html/");
		
		
	    File dir = new File(System.getProperty("user.dir") + "/data/Html/");
	    File [] paths = dir.listFiles();
	    
	    /*for(File path : paths) 
	    {
	    	System.out.println(path);
	    }*/
	    
	    File input = new File(System.getProperty("user.dir") + "/data/Html/alderis.ics.uci.edudresystems.html");
	    try {
			Document doc = Jsoup.parse(input, "UTF-8");
			System.out.println(doc.text());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    	    
	    //System.exit(0);
	    HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	    HashMap<Integer, String> termid2term = new HashMap<Integer,String>();
	    HashMap<String, Integer> doc2docid = new HashMap<String, Integer>();
	    HashMap<Integer, ArrayList<Integer>> docid2termlist = new HashMap<Integer, ArrayList<Integer>>();

	    ArrayList<String>words = new ArrayList<String>();
	    //goes through all the files and tokenizes them into one big token list
	    try {
		    int termid = 1;
		    int docid = 1;
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
		    	//System.out.println("doc finished");
		    }
		    
		    for(String key : term2termid.keySet()) {
		    	termid2term.put(term2termid.get(key), key);
		    }
		    	//words.addAll(Utilities.tokenizeFile(path));
		    
		    // print statements are commented out
		    /*System.out.print("term2termid: {");
		    for(String key : term2termid.keySet())
		    	System.out.println(key + ": " + term2termid.get(key) + ", ");
		    System.out.println("}");
		    
		    System.out.print("termid2term: {");
		    for(int key : termid2term.keySet())
		    	System.out.print(key + ": " + termid2term.get(key) + ", ");
		    System.out.println("}");
		    
		    System.out.print("doc2docid: {");
		    for(String key : doc2docid.keySet())
		    	System.out.print(key + ": " + doc2docid.get(key) + ", ");
		    System.out.println("}");
		    
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
		}
	    //computes the word frequencies of all the the big list
	   // List<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(words);
	    //Utilities.printFrequencies(frequencies);
	}
}