package trie;

import java.util.ArrayList;

/**
* This class implements a Trie. 
 * 
 * @author Sesh Venugopal
*
*/
public class Trie {
        
        // prevent instantiation
        private Trie() { }
        
        /**
        * Builds a trie by inserting all words in the input array, one at a time,
        * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
        * The words in the input array are all lower case.
        * 
         * @param allWords Input array of words (lowercase) to be inserted.
        * @return Root of trie with all words inserted from the input array
        */
        public static TrieNode buildTrie(String[] allWords) {
                /** COMPLETE THIS METHOD **/
                TrieNode ptr = null;
                
                for(int x=0; x<allWords.length; x++) {
                       if(ptr == null) {
                           ptr = new TrieNode(null,new TrieNode(new Indexes(0,(short)0,(short) (allWords[0].length()-1)),null,null),null);
                           
                       }else {
                               traverseStraight("",ptr.firstChild,allWords,x,allWords[x],allWords[x]);
                       }
                               
                       
                }
                
                // FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
                // MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
                return ptr;
        }
        
        
        private static void traverseStraight(String s, TrieNode ptr,String[] allWords,int finalIndex,String alteredString, String original) {
                String sub = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,ptr.substr.endIndex+1);
                String temporary = "";
                boolean status = false;
                for(int x=0; x<alteredString.length(); x++) {
                       char c = alteredString.charAt(x);
                    temporary = temporary + c;
                    if(sub.startsWith(temporary)) {
                        status = true;
                        continue;
                    }else if(status) {
                        temporary = temporary.substring(0,temporary.length()-1);
                        break;
                    }else if (!status) {
                        temporary = "";
                        break;
                    }
        }
        s=s+temporary;
        if(temporary.length() == 0) {
                if(ptr.sibling == null ) {
                       ptr.sibling =  new TrieNode(new Indexes((short)finalIndex,(short)original.indexOf(alteredString,s.length()),(short)(original.indexOf(alteredString,s.length())+alteredString.length()-1)),null,null);
                }else {
                
                	traverseStraight(s,ptr.sibling,allWords,finalIndex,alteredString,original);
                }
        }else if(temporary.equals(sub)) {
        String at = alteredString.substring((alteredString.indexOf(sub)+sub.length()));
        traverseStraight(s,ptr.firstChild,allWords,finalIndex,at,original);
        }else{
        
                ptr.substr.startIndex = (short) (allWords[ptr.substr.wordIndex].indexOf(sub));
                ptr.substr.endIndex = (short)(allWords[ptr.substr.wordIndex].indexOf(sub)+temporary.length()-1);
                String b = allWords[ptr.substr.wordIndex].substring(ptr.substr.endIndex+1);
                String c = original.substring(ptr.substr.endIndex+1);
                if(ptr.firstChild == null)
                ptr.firstChild = new TrieNode(new Indexes((short)ptr.substr.wordIndex,(short)allWords[ptr.substr.wordIndex].indexOf(b),(short)(allWords[ptr.substr.wordIndex].indexOf(b)+b.length()-1)),null,null);
                else { 
                       b=sub.substring(temporary.length());
                       TrieNode a = new TrieNode(new Indexes((short)ptr.substr.wordIndex,(short)allWords[ptr.substr.wordIndex].indexOf(b,temporary.length()),(short)(allWords[ptr.substr.wordIndex].indexOf(b,temporary.length())+b.length()-1)),null,null);
                       a.firstChild = ptr.firstChild;
                       ptr.firstChild=a;
                }
                traverseStraight(s,ptr.firstChild,allWords,finalIndex,c,original);
        }
                
        } 
        /**
        * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
         * trie whose words start with this prefix. 
         * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
        * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
         * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
         * and for prefix "bell", completion would be the leaf node that holds "bell". 
         * (The last example shows that an input prefix can be an entire word.) 
         * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
        * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
        *
        * @param root Root of Trie that stores all words to search on for completion lists
        * @param allWords Array of words that have been inserted into the trie
        * @param prefix Prefix to be completed with words in trie
        * @return List of all leaf nodes in trie that hold words that start with the prefix, 
         *                    order of leaf nodes does not matter.
        *         If there is no word in the tree that has this prefix, null is returned.
        */
        public static ArrayList<TrieNode> completionList(TrieNode root,String[] allWords, String prefix) {
                /** COMPLETE THIS METHOD **/
                ArrayList<TrieNode> list  = new ArrayList<TrieNode>();
                // FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
                // MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
                
        if(root.substr == null)
       root = root.firstChild;    
                
                String sub = allWords[root.substr.wordIndex].substring(root.substr.startIndex,root.substr.endIndex+1);
               if(sub.length() > prefix.length() || sub.length() == prefix.length()) {
                 if(!sub.startsWith(prefix)) {
                	if(root.sibling == null) {
                		return null;
                	}else {
                 		return completionList(root.sibling,allWords,prefix);
                	}
                 }
                }
               
                String temp = "";
                boolean status = false;
                for(int x=0; x<prefix.length(); x++) {
                       char c = prefix.charAt(x);
                       temp = temp + c;
                       if(sub.startsWith(temp)) {
                               status = true;
                               continue;
                       }else if (status) {
                               temp = temp.substring(0, temp.length()-1);
                               break;
                       }else if(!status) {
                               temp = "";
                               break;
                       }
                }  
                
                if(temp.length() == 0) {
                       if(root.sibling != null)
                       return completionList(root.sibling,allWords,prefix);
                }else if(prefix.length() > sub.length() ) {
                       prefix = prefix.substring(temp.length());
                       if(root.firstChild != null)
                       return completionList(root.firstChild,allWords,prefix);
                }else {
                       if(root.firstChild == null)
                    list.add(root);
                       else
                       add(root.firstChild,allWords,list);
                       
                }
                if(list.isEmpty())
                       return null;
                
                return list;
        
        }
        
        private static boolean has(ArrayList<TrieNode> list,String s,String[] allWords) {
                for(int x=0; x<list.size(); x++) {
                        if(allWords[list.get(x).substr.wordIndex].equals(s))
                               return true;
                }
                
                
                
                return false;
                
        }
        private static void add (TrieNode root,String[] allWords, ArrayList<TrieNode> list){
                TrieNode ptr = root;
                if(ptr== null) 
                       return;
                
                       TrieNode a = ptr;
                       while(ptr!= null) {
                               if(!has(list,allWords[ptr.substr.wordIndex],allWords) ) {
                                       list.add(ptr);
                               }
                               add(ptr.firstChild,allWords,list);
                               ptr=ptr.sibling; 
                       }
                       
                       
                }
                
        
        
        public static void print(TrieNode root, String[] allWords) {
                System.out.println("\nTRIE\n");
                print(root, 1, allWords);
        }
        
        private static void print(TrieNode root, int indent, String[] words) {
                if (root == null) {
                       return;
                }
                for (int i=0; i < indent-1; i++) {
                       System.out.print("    ");
                }
                
                if (root.substr != null) {
                       String pre = words[root.substr.wordIndex]
                                                       .substring(0, root.substr.endIndex+1);
                       System.out.println("      " + pre);
                }
                
                for (int i=0; i < indent-1; i++) {
                       System.out.print("    ");
                }
                System.out.print(" ---");
                if (root.substr == null) {
                       System.out.println("root");
                } else {
                       System.out.println(root.substr);
                }
                
                for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
                       for (int i=0; i < indent-1; i++) {
                               System.out.print("    ");
                       }
                       System.out.println("     |");
                       print(ptr, indent+1, words);
                }
        }
}

