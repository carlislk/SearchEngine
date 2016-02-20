import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

//Jared Napoli 31666553

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 * @throws FileNotFoundException 
	 */
	private String[] stopwords = {};
	private HashSet<String> stopHash = new HashSet<String>();
	Scanner fileScanner = new Scanner("/Users/Jared/Programs/cs121/Assignment 2/stopwords.rtf");
	public static ArrayList<String> tokenizeFile(File input){
		ArrayList<String> tokens = new ArrayList<String>();
		Scanner fileScanner;
		try {
			fileScanner = new Scanner(input);
			String text = fileScanner.useDelimiter("\\A").next();
			fileScanner.close();
			
			//parsing...
			text = text.replaceAll("[^0-9a-zA-Z]", " ");//replace all puncutation with " "
			text = text.replaceAll("\\s+", " ");//replace multiple spacing/returns with " " 
			for(String token : text.toLowerCase().split(" "))//split into tokens on " " 
					tokens.add(token);//add token to the arraylist
			//return tokens;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Directory is not a directory");
			e.printStackTrace();
		}
		return tokens;
		/*//get text and load it into text String
		String text = fileScanner.useDelimiter("\\A").next();
		fileScanner.close();
		
		//parsing...
		text = text.replaceAll("[^0-9a-zA-Z]", " ");//replace all puncutation with " "
		text = text.replaceAll("\\s+", " ");//replace multiple spacing/returns with " " 
		for(String token : text.toLowerCase().split(" "))//split into tokens on " " 
				tokens.add(token);//add token to the arraylist
		return tokens;*/
	}
	
	public static ArrayList<String> tokenizeString(String input){
		ArrayList<String> tokens = new ArrayList<String>();
			
		//parsing...
		input = input.replaceAll("[^0-9a-zA-Z]", " ");//replace all puncutation with " "
		input = input.replaceAll("\\s+", " ");//replace multiple spacing/returns with " " 
		for(String token : input.toLowerCase().split(" "))//split into tokens on " " 					tokens.add(token);//add token to the arraylist
			tokens.add(token);//return tokens;
		return tokens;
	}
	
	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6
	 * Unique item count: 5
	 * 
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 * 
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
	 * 
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
		int total = 0;
		for(Frequency token : frequencies)//go through frequency list and get total word counts
			total += token.getFrequency();//add to total
				
		//output	
		System.out.println("Total item count: " + total);
		System.out.println("Unique item count: " + frequencies.size() + "\n");
		
		
		for(Frequency token : frequencies)
		{
			int length = token.getText().length();
			if(length > 30)//if the token is longer than 20 characters, add more spacing
				length += 5;
			else
				length = 30;//default spacing is 30
			System.out.printf("%-" + length + "s%d\n", token.getText(), token.getFrequency());
		}
	}
	
	public static void printTop500(List<Frequency> frequencies) {
		HashSet<String> stopHash = new HashSet<String>();
		Scanner fileScanner;
		try {
			fileScanner = new Scanner(new File("/Users/Jared/Programs/cs121/Assignment 2/stopwords.txt"));
			while(fileScanner.hasNextLine())
				stopHash.add(fileScanner.nextLine());
			fileScanner.close();
			
//			for(String thing : stopHash)
//				System.out.println(thing);
			
			int length = 0, i = 0;
			for(Frequency token : frequencies)
			{
				if(stopHash.contains(token.getText()))
					continue;
				if(i++ >= 100)
					break;
				if(stopHash.contains(token))
					length = token.getText().length();
				if(length > 30)//if the token is longer than 20 characters, add more spacing
					length += 5;
				else
					length = 30;//default spacing is 30
				System.out.printf("%-" + length + "s%d\n", token.getText(), token.getFrequency());
			}
			
			//int length = 0,i = 0;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
