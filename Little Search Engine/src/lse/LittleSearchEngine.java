package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 *  
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		if(!new File(docFile).exists()) {
			throw new FileNotFoundException();
		}
		
		HashMap<String,Occurrence> map = new HashMap<String,Occurrence>();
		Scanner sc = new Scanner(new File(docFile));
		
		while (sc.hasNext()) {
			String word = sc.next().trim();
			String result = getKeyword(word);
			if(result == null) {
				continue;
			}else if(result.length() != 0) {
				if(map.containsKey(result)) {
					Occurrence e = map.get(result);
					e.frequency=e.frequency+1;
				}else {
				map.put(result, new Occurrence(docFile,1));	
				}
			
			}
			 
		}
		
		sc.close();
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return map;
	}
	

	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for(String e : kws.keySet()) {
			Occurrence x = kws.get(e);
			ArrayList<Occurrence> temp = keywordsIndex.get(e);
			if(temp == null) {
			ArrayList<Occurrence> temp2 = new ArrayList<Occurrence> ();
			temp2.add(x);
			keywordsIndex.put(e, temp2);
		
			}else {
				
				temp.add(x);
			    insertLastOccurrence(temp);
				
				
			}
		}
			
	}
	
	private boolean containsInvalid(String s) {
		for(int x=0; x<s.length(); x++) {
			char c = s.charAt(x);
			if(c != '.' && c!=',' && c!='?' && c!=':' &&c!=';' && c!='!' && !Character.isLetter(c)) {
				return true;
			}
		}
		
		return false;
	}
	
	/** 
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		if(containsInvalid(word) || word == null || word.length() < 0)
			return null;
		
		boolean status = false;
		String temp = "";
	    for(int x=word.length()-1; x>-1; x--) {
	    	char a  = word.charAt(x);
	    	if(Character.isLetter(a)) {
	    		status = true;
	    	}
	    	
	    	if(status && !Character.isLetter(a)) {
	    		return null;
	    	}
	    	
	    	if(Character.isLetter(a)) {
	    		temp = a+ temp ;
	    	}
	    }
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
	    if(noiseWords.contains(temp.toLowerCase()) || temp.length() == 0 || temp == null) {
	    	
	    	return null;
	    }
		return temp.toLowerCase();
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		/** COMPLETE THIS METHOD **/
		if(occs==null || occs.size()<=1 ) 
			return null;
		
		 ArrayList<Integer> middleValues  = new ArrayList<Integer>();
		
		 Occurrence e  = occs.get(occs.size()-1);
		 int low = 0;
		 int high = occs.size()-2;
	     int mid = -1;
		 
		 while(low<=high) {
			 
		     mid = (low+high)/2;
			 middleValues.add(mid);
			
			 if(occs.get(mid).frequency == e.frequency) {
				break;
			 }else if(occs.get(mid).frequency >e.frequency) {
				 low = mid+1;
			
			 }else {
				 high = mid-1;
			 }
		 }
		
		 if(occs.get(occs.size()-2).frequency>occs.get(occs.size()-1).frequency ) {
             return middleValues;
         }
         occs.remove(occs.size()-1);
         

         
         int temp= occs.get(middleValues.get(middleValues.size()-1)).frequency;
         if(e.frequency>temp || e.frequency == temp ) {
             occs.add(middleValues.get(middleValues.size()-1),e);
         
         }else {
         
             occs.add(middleValues.get(middleValues.size()-1)+1,e);
         }
	    
	     // following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return middleValues;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		
		
		kw1=kw1.toLowerCase();
		
		
		kw2=kw2.toLowerCase();
		
		
		ArrayList<String> result  = new ArrayList<String>();
		if(!keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2) || (kw1 == null && kw2 == null))
			return result;
		
		
		if(keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)) {
			ArrayList<Occurrence> t  =keywordsIndex.get(kw1);
			for(int x=0; x<5 && x<t.size(); x++) {
				
				result.add(t.get(x).document);
			}
		}
			
		if(!keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)) {
			ArrayList<Occurrence> t  =keywordsIndex.get(kw2);
			for(int x=0; x<5 && x<t.size(); x++) {
				
				result.add(t.get(x).document);
			}
		}
		
		if(keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)) {
			
			ArrayList<Occurrence> temp = new ArrayList<Occurrence> ();
			temp.addAll(keywordsIndex.get(kw1));
			temp.addAll(keywordsIndex.get(kw2));
			
			for(int x=0; x<5 && !temp.isEmpty(); x++) {
				
			   int current = 0, previous =-1;
			   
			   for(current =0; current < temp.size() && temp.get(current) != null; current++) {
				   
				   if(previous == -1) {
					   
					   
                      if(!result.contains(temp.get(current).document)) {
						    previous = current;
					   }
					   
				   }else if(temp.get(current).frequency > temp.get(previous).frequency ) {
					   
					   
		                if(!result.contains(temp.get(current).document)) {
						   previous = current;
					   }
					   
				   }else if(temp.get(current).frequency == temp.get(previous).frequency ) {
					   
					   if(keywordsIndex.get(kw1).contains(temp.get(current))) {
						   
						   
						   
						   
						   
						  if(!result.contains(temp.get(current).document)) {
							  previous = current;
						  }
					   }
					   
				   }
			   }
			   
			   if(previous != -1) {
				   
				   String s = temp.remove(previous).document;    
				   
				   
				   result.add(s);
				   
			   }
				
			       
				
			}
			
		}
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return result;
	
	}

}
