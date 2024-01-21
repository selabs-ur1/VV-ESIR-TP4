package fr.istic.vv;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.jqwik.api.*;

// Class unfinished due to a lack of time
public class RomanNumeralTest {
	
	private static Map<Character, Integer> numeralValues = new HashMap<>();

    static {
    	numeralValues.put('M', 1000);
    	numeralValues.put('D', 500);
    	numeralValues.put('C', 100);
    	numeralValues.put('L', 50);
    	numeralValues.put('X', 10);
    	numeralValues.put('V', 5);
    	numeralValues.put('I', 1);
    }
    
    private static Set<Character> allowedSymbols = numeralValues.keySet();
    
    private static Set<Character> repeatableSymbols = Set.of('M', 'C', 'X', 'I');
    
    private static Set<Character> singleInstanceOnly = Set.of('D', 'L', 'V');
    
    
    @Provide
    Arbitrary<String> romanNumeral() {
    	char[] symbols = new char[7];
    	int indx = 0;
    	for (Character symbol: allowedSymbols)
    		symbols[indx++] = symbol;
    		
        return Arbitraries.strings()
        		.withChars(symbols)
        		.ofMinLength(1)
        		.ofMaxLength(9)
        		.filter(numeral -> {
        			for (char symbol: repeatableSymbols) {
        				for (char singleSymbol: singleInstanceOnly) {
        					String repeatedSequence = String.valueOf(symbol + symbol + symbol);
            				if (numeral.indexOf(repeatedSequence) != numeral.lastIndexOf(repeatedSequence) ||
            						numeral.indexOf(singleSymbol) != numeral.lastIndexOf(singleSymbol))
            					return false;
        				}
        			}
        			return true;
        		});
    }
    
    @Property
    boolean validSymbolsOnly(@ForAll("romanNumeral") String numeral) {
    	
    	for (Character symbol : numeral.toCharArray()) {
            if (!allowedSymbols.contains(symbol)) {
                return false;
            }
        }
        return true;
    }
    
    @Property
    boolean repeatedSymbols(@ForAll("romanNumeral") String numeral) {
    	for (char symbol: repeatableSymbols) {
    		
    		String repeatedSequece = String.valueOf(symbol + symbol + symbol);
    		
    		if (numeral.contains(repeatedSequece)) {
    			// If the first and last index of the repeated sequence aren't equal, then the sequence might be present multiple times in the strin
    			// Example: IIII (2 instances of 'III' substring)
    			return numeral.indexOf(repeatedSequece) == numeral.lastIndexOf(repeatedSequece);
    		}
    	}
    	return true;
    }
    
    @Property
    boolean singleInstaceSymbols(@ForAll("romanNumeral") String numeral) {
    	for (Character symbol: singleInstanceOnly) 
    		if (numeral.indexOf(symbol) != numeral.lastIndexOf(symbol))
    			return false;
    	
    	return true;
    }
}