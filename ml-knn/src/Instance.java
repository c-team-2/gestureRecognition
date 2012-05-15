import java.util.ArrayList;

public class Instance {
	private ArrayList<Feature> attributes;
	private boolean redeemed;
	private String sessionID;
	private String uuid;

	public void setAttributes(ArrayList<Feature> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<Feature> getAttributes() {
		return attributes;
	}

	public void setRedeemed(boolean redeemed) {
		this.redeemed = redeemed;
	}

	public boolean isRedeemed() {
		return redeemed;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}
}
