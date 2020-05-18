package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList <Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
   
    	String s = "";
    	expr = expr + " ";
    	
    for(int x =0; x<expr.length(); x++) {
    		
     char c = expr.charAt(x);
    		
     if(c == '[')	{
       if(arrays.contains(new Array(s))) {
    	   s="";
       }else {
    	   arrays.add(new Array(s));
    	   s= "";
       }
			continue;
     }
    			
     if((Character.isDigit(c) ||c == ' ' || c == '*'|| c == '/'|| c == '+'|| c == '-'|| c == '('|| c == ')' || c == ']')&& s.length() ==0  ) {
    	continue;
     }
    	
     if( (Character.isDigit(c) || c == ' ' || c == '*'|| c == '/'|| c == '+'|| c == '-'|| c == '('|| c == ')' || c == ']')&& s.length() !=0 ) {
       if(vars.contains(new Variable(s))) {
          s="";
       }else {
          vars.add(new Variable(s));
          s= "";
        }
      	continue;
     }
        s = s +c;
      }
   }
    
    //used for expressions containing addition and subtraction expressions
     private static String addSub(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	 expr = expr.replaceAll("\\s", "");
         expr= expr+" "; 
    	Stack<Float> finalValue = new Stack<Float>(); 
    	Stack<Character> opperations = new Stack<Character>();
    	String s= "";
    	expr =addZeros(expr);
    	for(int x=0; x<expr.length(); x++) {
    		char c = expr.charAt(x);
    		
    		if(Character.isLetter(c) || Character.isDigit(c) || c == '.') {
    			s=s+c;
    		}else if((c == '+' || c=='-')&&s.length()!=0) {
    		     
    			if(c== '-' && expr.charAt(x+1)=='-') {
    				finalValue.push(actualValue(s,vars,arrays));
    				s="";
    				if(finalValue.size() <=1) {
        				
        			}else {
        				finalValue.push(calculate(finalValue.pop(),finalValue.pop(),opperations.pop()));
        			}
        			opperations.push('+');
        			x++;
        			continue;
    			}
    			
    			if(c== '+' && expr.charAt(x+1)=='-') {
    				finalValue.push(actualValue(s,vars,arrays));
    				s="";
    				if(finalValue.size() <=1) {
        				
        			}else {
        				finalValue.push(calculate(finalValue.pop(),finalValue.pop(),opperations.pop()));
        			}
        			opperations.push('-');
        			x++;
        			continue;
    			}
    			finalValue.push(actualValue(s,vars,arrays));
    			s="";
    			if(finalValue.size() <=1) {
    				
    			}else {
    				finalValue.push(calculate(finalValue.pop(),finalValue.pop(),opperations.pop()));
    			}
    			opperations.push(c);
    		}else if(c==' '&&s.length()!=0) {
    			finalValue.push(actualValue(s,vars,arrays));
    			s="";
    			if(finalValue.size() <=1) {
    				
    			}else {
    				finalValue.push(calculate(finalValue.pop(),finalValue.pop(),opperations.pop()));
    			}
    		}
    	}
    	
    	return finalValue.pop().toString();
    	
    }
    
    //used operations that involves multiplication and division
    private static String multiDiv(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
   	 expr = expr.replaceAll("\\s", "");
        expr= expr+" "; 
   	Stack<Float> finalValue = new Stack<Float>(); 
   	Stack<Character> opperations = new Stack<Character>();
   	
   	String s= "";
   	boolean z = false;
 
   	for(int x=0; x<expr.length(); x++) {
   		char c = expr.charAt(x);
   		
   		if(Character.isLetter(c) || Character.isDigit(c) || c == '.'||c=='-') {
   			s=s+c;
   		}else if((c == '*' || c=='/')&&s.length()!=0) {
   		     
  
   			
   			finalValue.push(actualValue(s,vars,arrays));
   			s="";
   			if(finalValue.size() <=1) {
   				
   			}else {
   				finalValue.push(calculate(finalValue.pop(),finalValue.pop(),opperations.pop()));
   			}
   			opperations.push(c);
   		}else if(c==' '&&s.length()!=0) {
   			finalValue.push(actualValue(s,vars,arrays));
   			s="";
   			if(finalValue.size() <=1) {
   				
   			}else {
   				finalValue.push(calculate(finalValue.pop(),finalValue.pop(),opperations.pop()));
   			}
   		}
   	}
   	
   	
   	
   	
		return finalValue.pop().toString();
   	
   }
    
    private static String md(String s, ArrayList<Variable> vars, ArrayList<Array> arrays) {
     	 s = s.replaceAll("\\s", "");
         s= s+" "; 
    	boolean status = true;
    	String temporaryStorage = "";
    	Stack<Integer> posistion = new Stack<Integer>();
    	for(int x=0; x<s.length();x++) {
    		char c = s.charAt(x);
    		if(Character.isDigit(c)|| Character.isLetter(c)||c=='.') {
    			temporaryStorage = temporaryStorage + c;
    		}else if(c == ' ') {
    			if(!posistion.isEmpty()) {
    			int z= posistion.pop();
    		
    			s=s.substring(0,z)+multiDiv(s.substring(z,x),vars,arrays)+s.substring(x,s.length());
    			
    			}else {
    				s="";
    				s+= multiDiv(s,vars,arrays);
    			
    			}
    			break;
    		}else if((c=='+' || c=='-') && status ) {
    			if(x!=0 && (s.charAt(x-1)=='*'||s.charAt(x-1)=='/'||s.charAt(x-1)=='-'||s.charAt(x-1)=='+')) {
    				temporaryStorage = temporaryStorage + c;
    				continue;
    			}
    			
    			temporaryStorage = "";
    			
    		}else if((c=='+' || c=='-') && !status ) {
    			if(s.charAt(x-1)=='*'||s.charAt(x-1)=='/')
    				continue;
    			int z= posistion.pop();
    			s=s.substring(0,z)+multiDiv(s.substring(z,x),vars,arrays)+s.substring(x,s.length());
    			break;
    		}else if((c=='*' || c=='/') && status) {
    			posistion.push(x-temporaryStorage.length());
    			temporaryStorage="";
    			status = false;
    		}
    	}
    	
		return s;
    	
    }
    
    //check if the expression contains addition and subtraction
    private static Boolean isAddable(String s) {
    	for(int x=0; x<s.length();x++) {
    		if(s.charAt(x) == '+' || s.charAt(x) == '-')
    			return true;
    	}
    	return false;
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	  
    	/**following line just a placeholder for compilation**/
    	Stack<Integer> posistion = new Stack<Integer>();
       expr = expr+" ";
         
    	if(containParenthesis(expr)) {
    		for(int x=0; x<expr.length(); x++) {
    			if(expr.charAt(x) == '(')
    				posistion.push(x);
    			else if (expr.charAt(x) == ')') {
    				int val = posistion.pop();
    				
    				expr = expr.substring(0, val) + evaluate(expr.substring(val+1,x),vars,arrays)+expr.substring(x+1,expr.length());
    				return evaluate(expr,vars,arrays);
    				
    		}
    			
    	}
    		
    	}
    	
    	//gets rid of all brackets
    	while(containBracket(expr)) 
    		  expr = makeValuesforArray(expr, vars, arrays);
    	  
    	  //gets rid of all multiplication/division functions
    	  while(isMultiable(expr)) 
        	  expr = md(expr,vars,arrays);
    	  
    	  //gets rid of all addition/subtraction functions
          if(isAddable(expr))
        	  expr = addSub(expr,vars,arrays);
          
          expr = expr.replaceAll("\\s", "");
         
     return actualValue(expr,vars, arrays ) ;
    	
    }
    
    //check if a function contains multiplication and division functions
   private static boolean isMultiable(String s) {
    	for(int x=0; x<s.length();x++) {
    		if(s.charAt(x) == '*' || s.charAt(x) == '/')
    			return true;
    	}
    	return false;
    }
    
   //used for evaluating functions in array brackets
    private static String makeValuesforArray(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	Stack<Integer> places  = new Stack<Integer>();
    		String newS= "",prev= "";
   
   
    	for(int x=0; x<expr.length();x++) {
    		
    		char c = expr.charAt(x) ;
        if(c == '+' || c == '-'|| c == '*' || c == '/' ) {
        	if(newS.length() != 0) {
        	prev = newS;
        	newS="";
        	}
        
    	}else if(Character.isLetter(c)) {
    		newS=newS+expr.charAt(x);
    		}else if(c == '[') {
    			prev = newS;
            	newS="";
    			places.push(x);
    		}else if(c == ']') {
    			int z =places.pop();
    			String curr = expr.substring(z+1,x);
    			int aav = final_evaluate_array(curr,vars,arrays);
    			if(places.size() ==0)
    			expr = expr.substring(0,z-prev.length())+actualArrayValue(prev,aav,vars,arrays)+expr.substring(x+1,expr.length());
    			else 
    				expr = expr.substring(0,z-prev.length())+actualArrayValue(prev,aav,vars,arrays)+expr.substring(x+1,expr.length());
    			break;
    
    		}
    	}
		
		return expr;
    }
    
    
  private static int final_evaluate_array(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
	  while(isMultiable(expr)) 
		 
    	  expr = md(expr,vars,arrays);
	  
      if(isAddable(expr)) 
    	  expr = addSub(expr,vars,arrays);
      
      expr = expr.replaceAll("\\s", "");
     
	
	return (int) actualValue(expr,vars, arrays ) ;
		
	}

//similar to actualValue but used for arrays
private static int actualArrayValue(String expr,int x,ArrayList<Variable> vars, ArrayList<Array> arrays ) {
    	int posistion  = 0;
    	for(int j =0 ; j<arrays.size();j++) {
    		if(arrays.get(j).name.equals(expr)) {
    			posistion =j;
    			break;
    		}
    	}
    	
  				return arrays.get(posistion).values[x];
  			
    }

   //used for negative expressions
   private static String addZeros(String expr) { 
    	if(expr.startsWith("-")) {
    		expr = 0 + expr;
    	}
    	return expr;
    }
    
    //returns the float value from an int or a variable letter
    private static float actualValue(String expr,ArrayList<Variable> vars, ArrayList<Array> arrays ) {
    	
    	int posistion  = -1;
    	for(int j =0 ; j<vars.size();j++) {
    		if(vars.get(j).name.equals(expr)) {
    			posistion =j;
    			break;
    		}
    	}
    	
    	if (posistion != -1){
    		 return vars.get(posistion).value;
		}else{ 
	          return Float.parseFloat(expr);
		}
    }
    
  
   
   //check if expression contains parenthesis
	private static boolean containParenthesis(String s) {
    	for(int x=0; x<s.length(); x++) {
    		if(s.charAt(x) == '(')
    			return true;
    	}
    	
    	return false;
    }
    
	
	//check if expression contains brackets
    private static boolean containBracket(String s) {
    	for(int x=0; x<s.length(); x++) {
    		if(s.charAt(x) == '[')
    			return true;
    	}
    	
    	return false;
    }
   
    //calculate functions
    private static float calculate(float a, float b, char opperation) {
    	float result = 0;
		if(opperation == '+') {
			result = b +a;
		}else if(opperation == '-') {
			result = b-a;
			
		}else if(opperation == '*') {
			result = b *a;
		}else if(opperation == '/') {
			result = b/a;
		}
    	
    	return result;
    }
    
   
}
