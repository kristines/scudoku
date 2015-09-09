package judoku.data;

public enum OneToNine {
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;

    public String toString() {
    	switch (ordinal()) {
	    	case 0: return "1";
	    	case 1: return "2";
	    	case 2: return "3";
	    	case 3: return "4";
	    	case 4: return "5";
	    	case 5: return "6";
	    	case 6: return "7";
	    	case 7: return "8";
	    	case 8: return "9";
    	}
    	throw new RuntimeException("should never happen");
    }

    public static OneToNine fromString(String s) {
    	switch (s) {
	    	case "1": return ONE;
	    	case "2": return TWO;
	    	case "3": return THREE;
	    	case "4": return FOUR;
	    	case "5": return FIVE;
	    	case "6": return SIX;
	    	case "7": return SEVEN;
	    	case "8": return EIGHT;
	    	case "9": return NINE;
	    	case ".": return null;
		}
		throw new RuntimeException("Found value different than 1,2,..., 9 - wrong setup");
    }
    
    public static OneToNine fromChar(char c) {
    	return fromString(""+c);
    }
}
