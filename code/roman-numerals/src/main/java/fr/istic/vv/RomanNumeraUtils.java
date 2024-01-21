package fr.istic.vv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class RomanNumeraUtils {
	
	static Map<String, Integer> numeralValues = new LinkedHashMap<>();
	static Map<String, Integer> validSubstractions = new LinkedHashMap<>();
	
	static {
		numeralValues.put("I", 1);
		numeralValues.put("V", 5);
		numeralValues.put("X", 10);
		numeralValues.put("L", 50);
		numeralValues.put("C", 100);
		numeralValues.put("D", 500);
		numeralValues.put("M", 1000);
		
		validSubstractions.put("IV", 4);
		validSubstractions.put("IX", 9);
		validSubstractions.put("XL", 40);
		validSubstractions.put("XC", 90);
		validSubstractions.put("CD", 400);
		validSubstractions.put("CM", 900);
	}
	
	// Function that return true if a numerical is repeated no more than 3 times, false otherwise
	// Function also returns false if a repetition of an invalid numeral is present
	private static boolean validRepetition(String numeral) {
		
		if (numeral.isEmpty())
			return true;
		
		List<String> forbiddenRepetition = Arrays.asList("D", "L", "V");
		
		int counter = 1;
		
		String previousChar = String.valueOf(numeral.charAt(0));
		
		for (int i = 1; i < numeral.length(); i++) {
			
			String currentChar = String.valueOf(numeral.charAt(i));
			
			if (currentChar.equals(previousChar)) {
				
				if (forbiddenRepetition.contains(currentChar)) { 
					return false;
				}
				else {
					counter++;
				}
			}
			else {
				counter = 1;
			}
			
			if (counter > 3) {
				return false;
			}
			previousChar = currentChar;
		}
		return true;
	}
	
	// Function that returns the integer value of a roman numeral
	private static  int intValue(String numeral) {
		if (numeral.length() == 1)	
			return numeralValues.get(numeral);
		else
			return validSubstractions.get(numeral);
	}
	
	// Function that check if only existing characters are present in a given numeral
	private static boolean validNumeralsOnly(String numeral) {

		List<String> listNumeral = Arrays.asList(numeral.split(""));
        LinkedHashSet<String> distinctSet = new LinkedHashSet<>(listNumeral);
		
		for (String num: distinctSet) 
			if (!numeralValues.containsKey(num)) 
				return false;
			
		return true;
	}
	
	// Function that checks if the order of numerals is valid (from bigger values to smaller ones)
	// Also checks for valid substractions
	private static boolean validOrder(String numeral) {
		
		// Padding necessary in order to avoid 'Index out of bounds'
		String paddedNumeral = ' ' + numeral + ' ';
		
		String previousChar = String.valueOf(paddedNumeral.charAt(1));
		
		for (int i = 1; i < paddedNumeral.length() - 1; i++) {
			
			String currentChar = String.valueOf(paddedNumeral.charAt(i));
			
			if (!previousChar.equals(currentChar)) 
				if (intValue(previousChar) < intValue(currentChar)) 
					if (!validSubstractions.keySet().contains(paddedNumeral.substring(i-1, i+1)) ||
							paddedNumeral.charAt(i - 1) == paddedNumeral.charAt(i - 2)) 
						return false;
				
			previousChar = currentChar;
		}
		return true;
	}
    
	public static boolean isValidRomanNumeral(String value) { 
		value = value.toUpperCase();
		
		return validNumeralsOnly(value) && validRepetition(value) && validOrder(value); 
	}

    public static int parseRomanNumeral(String numeral) { 
    	numeral = numeral.toUpperCase();
    	
    	if (isValidRomanNumeral(numeral)) {
    		int res = 0;
    		
    		for (String substraction: validSubstractions.keySet()) {
    			if (numeral.contains(substraction)) {
    				res += intValue(substraction);
    				numeral = numeral.replace(substraction, "");
    			}
    		}
    		
    		for (int i = 0; i < numeral.length(); i++) {
    			String currentChar = String.valueOf(numeral.charAt(i));
    			res += intValue(currentChar);
    		}
    		return res;
    	}
    	else 
    		return -1; 
    }

    public static String toRomanNumeral(int number) { 
    	if (number < 1 || number > 3999)
    		return "Number outside of the roman numeral range!";
    	
    	else {
    		String res = "";
    		
    		List<String> symbols = new ArrayList<>(numeralValues.keySet());
    		List<String> sub = new ArrayList<>(validSubstractions.keySet());
    		
    		Collections.reverse(symbols);
    		Collections.reverse(sub);
    		
    		int i = 0;
    		
    		String currentSymbol = symbols.get(i);
			String currentSub = sub.get(i);
    		
    		do {
    			
    			while (number >= intValue(currentSymbol)) {
    				res += currentSymbol;
    				number -= intValue(currentSymbol);
    			}
    			
				if (number >= intValue(currentSub)) {
    				res += currentSub;
    				number -= intValue(currentSub);
    			}
				
    			i++;
    			
    			if (i < symbols.size())
    				currentSymbol = symbols.get(i);
    			
    			if (i < sub.size()) 
    				currentSub = sub.get(i);
    			
    			
    		} while (number != 0 && i < symbols.size());
    		return res;
    	}
    }
}
