import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Expiration extends Feature {

	enum Expiry {
		Date,
		Uses,
		Unknown,
		None
	}
	
	private Expiry expiry;

	public Expiration() {
		super("Expiration");
	}
	
	public Expiration(Expiry expiry) {
		super("Expiration");
		this.expiry = expiry;
	}
	
	public void setExpiry(Expiry expiry) {
		this.expiry = expiry;
	}

	public Expiry getExpiry() {
		return expiry;
	}
	
	public static Expiry determineExpiration(String expiry) {
		return Expiry.Date;
	}
	
	public static Expiry parseExpirationText(String offer) {
		return Expiry.Unknown;
	}
}
