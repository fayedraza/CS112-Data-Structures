package lse;

import java.util.ArrayList;
import java.util.Arrays;



public class Checl {
	public static ArrayList<Integer> insertLastOccurrence(ArrayList<Integer> occs) {
		/** COMPLETE THIS METHOD **/
		/** COMPLETE THIS METHOD **/
		if(occs==null || occs.size()<=1 ) 
			return null;
		
		 ArrayList<Integer> middleValues  = new ArrayList<Integer>();
		
		 int e  = occs.get(occs.size()-1);
		 int low = 0;
		 int high = occs.size()-2;
	
		 
		 while(low<=high) {
			 
			int  mid = (low+high)/2;
			 middleValues.add(mid);
			
			 if(occs.get(mid) == e) {
				break;
			 }else if(occs.get(mid) >e) {
				 low = mid+1;
			
			 }else {
				 high = mid-1;
				 
			
			 }
			 
			 
		 }
		 
		 if(occs.get(occs.size()-2)>occs.get(occs.size()-1) ) {
    		 return middleValues;
    	 }
		 occs.remove(occs.size()-1);
		 

		 
		 int temp= occs.get(middleValues.get(middleValues.size()-1));
	     if(e>temp || e == temp ) {
	    	 occs.add(middleValues.get(middleValues.size()-1),e);
	     
	     }else {
	     
	    	 occs.add(middleValues.get(middleValues.size()-1)+1,e);
	     }
	    

			
		 
	
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return middleValues;
	}
 public static void main (String[]args) {
	ArrayList<Integer> xz  = new ArrayList<Integer>(Arrays.asList(90,70,80
));
	ArrayList<Integer> y = insertLastOccurrence(xz);
	for(int x=0; x<y.size(); x++) {
		System.out.print(y.get(x)+ " ");
	}
	System.out.println(isD(xz));
	for(int x=0; x<xz.size();x++) {
		System.out.print(xz.get(x)+" ");
	}
	
 }
 
 public static boolean isD(ArrayList<Integer> xz) {
	 for(int x=0; x<xz.size()-1; x++) {
			if(xz.get(x)<xz.get(x+1))
				return false;
		}
	 
	 
	 return true;
 }
}
