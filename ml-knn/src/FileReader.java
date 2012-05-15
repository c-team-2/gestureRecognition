import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FileReader {
	private String dataFilePath;
	
	public FileReader(String dataFilePath) {
		this.dataFilePath = dataFilePath;
	}
	
    public ArrayList<Instance> buildInstances() {
		BufferedReader reader = null;
		DataInputStream dis = null;
		ArrayList<Instance> instances = new ArrayList<Instance>();

        try { 
           File f = new File(dataFilePath);
           FileInputStream fis = new FileInputStream(f); 
           reader = new BufferedReader(new InputStreamReader(fis));;
           
           // read the first Instance of the file
           String line;
           int numa=1;
           Instance instance = null;
           ArrayList<Feature> attributes;
           while ((line = reader.readLine()) != null) {
              StringTokenizer st = new StringTokenizer(line, ",");
              attributes = new ArrayList<Feature>();
              instance = new Instance();
              
              numa++;
              if((Knn.NUM_ATTRS - 1) != st.countTokens()) {
            	  System.out.println("LINE: " + numa--);
            	  throw new Exception("Unknown number of attributes!");
              }
              
              String sessionId = st.nextToken(); // the session Id for the client.  we only want to consider
              									 // classification for the same person (uuid) in the same session
              String uuid = st.nextToken(); // the actual person redeeming the coupon
              String os = st.nextToken(); // the OS that is running on the device
              String device = st.nextToken(); // the device the person is using
              String offer = st.nextToken();
              String expiry = st.nextToken();
              String category = st.nextToken();
              String distance = st.nextToken();
              
              String wsAction = st.nextToken(); // what happened in the session, redeem or hit

              attributes.add(new Category(Category.determineCategory(category)));
			  attributes.add(new Distance(Distance.determineDistance(distance)));
			  attributes.add(new Expiration(Expiration.determineExpiration(expiry)));
			  attributes.add(new Handset(Handset.determineDevice(device), Handset.determineOS(os)));
			  attributes.add(new Offer(Offer.parseOfferText(offer)));
			  attributes.add(new WSAction(WSAction.determineAction(wsAction)));
			  
			  if(((WSAction)attributes.get(Knn.WSACTION_INDEX)).getAction() == WSAction.Action.Redeem) {
				  instance.setRedeemed(true);
			  }
			  else {
				  instance.setRedeemed(false);
			  }
			  instance.setUuid(uuid);
			  instance.setSessionID(sessionId);
			  
			  instance.setAttributes(attributes);
			  instances.add(instance);
           }

        } 
        catch (IOException e) { 
           System.out.println("Uh oh, got an IOException error: " + e.getMessage()); 
        } 
        catch (Exception e) {
            System.out.println("Uh oh, got an Exception error: " + e.getMessage()); 
        }
        finally { 
           if (dis != null) {
              try {
                 dis.close();
              } catch (IOException ioe) {
                 System.out.println("IOException error trying to close the file: " + ioe.getMessage()); 
              }
           }
        }
        
		return instances;
	}

	public void setDataFilePath(String dataFilePath) {
		this.dataFilePath = dataFilePath;
	}

	public String getDataFilePath() {
		return dataFilePath;
	}
}