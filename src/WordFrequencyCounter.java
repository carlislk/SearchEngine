import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

//Jared Napoli, 31666553

/**
 * Counts the total number of words and their frequencies in a text file.
 */
public final class WordFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private WordFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique word in the original list. The frequency of each word
	 * is equal to the number of times that word occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied words sorted
	 * alphabetically.
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["this", "sentence", "repeats", "the", "word", "sentence"]
	 * 
	 * The output list of frequencies should be 
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeWordFrequencies(List<String> words) {
		HashMap<String, Frequency> hm = new HashMap<String,Frequency>();//holds the frequencies for easy look up
		
		//add to the HashMap, if already there, increment frequency, if not, add to hm
		//the key is the actual word string
		for(String word : words)
		{
			if(hm.containsKey(word))
				hm.get(word).incrementFrequency();
			else
				hm.put(word, new Frequency(word,1));
		}
		
		//initialize an arraylist to hold all the Frequencies stored in hm
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>(hm.values());
		
		//sorts the array using a custom Comparator
		//sorts in decreasing order of frequency, tie breaker is done by alphabetical order
		Collections.sort(frequencies, Collections.reverseOrder(new Comparator<Frequency>() {
			public int compare(Frequency f1, Frequency f2) {
				if(f1.getFrequency() == f2.getFrequency())
					return String.CASE_INSENSITIVE_ORDER.compare(f2.getText(), f1.getText());
				else
					return Integer.compare(f1.getFrequency(),f2.getFrequency());
			}			
		}));

		return frequencies;
	}
	
	/**
	 * Runs the word frequency counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		//File file = new File(args[0]);
		//List<String> words = Utilities.tokenizeFile(file);
		//List<Frequency> frequencies = computeWordFrequencies(words);
		//Utilities.printFrequencies(frequencies);
	}
}
