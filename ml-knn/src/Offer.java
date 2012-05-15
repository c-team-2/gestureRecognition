	import java.util.*;

public class Offer extends Feature {
	
	private class MutableInteger {
		int value = 1;
		public void inc() { value++; }
		public int get() { return value; }
	}

	enum OfferType {
		PercentageDiscount,
		DollarDiscount,
		PayForFree,
		Unknown
	}
	
	private OfferType offerType;
	
	public Offer() {
		super("Offer");
	}
	
	public Offer(OfferType offerType) {
		super("Offer");
		this.setOfferType(offerType);
	}
	
	public static OfferType parseOfferText(String offer) {
		HashMap<String, MutableInteger> words = new HashMap<String, MutableInteger>();
		
        StringTokenizer st = new StringTokenizer(offer, " ");
        String token;
        while(st.hasMoreTokens()) {
        	token = st.nextToken();
        	if(words.containsKey(token)) {
        		MutableInteger value = words.get(token);
        		value.inc();
        		words.put(token, value);
        	}
        	else {
				MutableInteger value = (new Offer()).new MutableInteger();
        		words.put(token, value);
        	}
        }
        
       ArrayList<String> sortedWords = new ArrayList<String>();
       int highest = 0; 
       
       for(int i = 0; i < words.size(); i++) {
           for(String word : words.keySet()) {
        	   int value = words.get(word).value;
        	   if(value > highest) {
        		   highest = value;
                   words.remove(word);
                   sortedWords.add(word);
                   break;
        	   }
           }
       }
       
       if(sortedWords.contains("free")) {
    	   return OfferType.PayForFree;
       }
       if(sortedWords.contains("percent") || sortedWords.contains("percentage") || sortedWords.contains("%")) {
    	   return OfferType.PercentageDiscount;
       }
       if(sortedWords.contains("dollar") || sortedWords.contains("dollars") || sortedWords.contains("$")) {
    	   return OfferType.DollarDiscount;
       }
       
		return OfferType.Unknown;
	}

	public void setOfferType(OfferType offerType) {
		this.offerType = offerType;
	}

	public OfferType getOfferType() {
		return offerType;
	}
}
