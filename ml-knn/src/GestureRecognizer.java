import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.color.ColorSpace;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.LinkedList;
import java.awt.GridBagLayout;

/**
 * 
 */

/**
 * @author sunghoon
 *
 */
public class GestureRecognizer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButton_start = null;
	private JButton jButton_end = null;
	private boolean continueReadData = false;
	private Sensor kinect;
	private LinkedList<SensorSnapshot> snapshots;

	/**
	 * This method initializes jButton_start	
	 * 	jButton_start will indicate the start of the gesture performance event
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_start() {
		if (jButton_start == null) {
			jButton_start = new JButton();
			jButton_start.setBounds(new Rectangle(5, 5, 120, 30));
			jButton_start.setText("START");
			//This is the function that is executed when the START button is pressed
			jButton_start.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//first it cleans up the global variable that 
					continueReadData = true;
					
					//creating a thread that will continue receiving the data from the sensor
					//until the END button is pressed
					Runnable runA = new Runnable() {
					    public void run() {
					    	while(continueReadData)
							{
								//request data
					    		System.out.println("Receive Data\n"); // TODO Auto-generated Event stub actionPerformed()
					    		SensorSnapshot snapshot = kinect.getSensorSnapshot();
					    		System.out.println(snapshot.getChannelName());
					    		Channel dummy = snapshot.getChannel("User1");
					    		snapshots.add(snapshot);
					    		//drawing the skeleton on the panel
							}
					    }
					};
					
					//Start the thread
			        final Thread threadA = new Thread(runA);
			        threadA.start();
				}
			});
		}
		return jButton_start;
	}

	/**
	 * This method initializes jButton_end	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_end() {
		if (jButton_end == null) {
			jButton_end = new JButton();
			jButton_end.setBounds(new Rectangle(5, 40, 120, 30));
			jButton_end.setText("END");
			//This is the function that is executed when the END button is pressed
			jButton_end.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					continueReadData = false;
					System.out.println("Stop Receiving Data\n"); // TODO Auto-generated Event stub actionPerformed()
					
					/*
					Channel user1Channel = snapshot.getChannel("User1");
					//getting the skeleton data
			    	if (user1Channel != null)
			    	{
			    		HashMap<Integer, float[]> user1Skeleton = new HashMap<Integer, float[]>();	
				    	for (int jointIndex = 0; jointIndex < 15; ++jointIndex)
				    	{
				    		float[] coordsAndConf = new float[4]; // coordinates (x, y, z) and confidence
				    		coordsAndConf[0] = user1Channel.getTuple(jointIndex).getElementFloat(0);
				    		coordsAndConf[1] = user1Channel.getTuple(jointIndex).getElementFloat(1);
				    		coordsAndConf[2] = user1Channel.getTuple(jointIndex).getElementFloat(2);
				    		coordsAndConf[3] = user1Channel.getTuple(jointIndex).getElementFloat(3);
				    		
				    		user1Skeleton.put(jointIndex, coordsAndConf);
				    	}
				    	joints.put(1, user1Skeleton);
			    	}
					 */
					
					//clear the linked list
					snapshots.clear();
					//Now perform the K-NN classification
					Knn.runKNN();
				}
			});
		}
		return jButton_end;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GestureRecognizer thisClass = new GestureRecognizer();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public GestureRecognizer() {
		super();
		initialize();

		//Initializing OpenNI
		UniDevice device = new UniOpenNIDevice();
		kinect = new Sensor(device);
		snapshots = new LinkedList<SensorSnapshot>();
	}

	/**
	 * This method initializes the main frame
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(381, 251);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");	
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			//jContentPane.setLayout(new BorderLayout());
			jContentPane.setLayout(null);			
			
			jContentPane.add(getJButton_start(), null);
			jContentPane.add(getJButton_end(), null);
			
			ImagePanel skeletonImagePanel = new ImagePanel();  
	        // add graphic component to center of this  
	        // JFrames (default) BorderLayout  
			jContentPane.add(skeletonImagePanel, null);  
			
			
		}
		return jContentPane;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

class ImagePanel extends JPanel{

    private BufferedImage image;
    private static final long serialVersionUID = 1L;
    private byte[] imgbytes;
    private float histogram[];
    HashMap<Integer, HashMap<Integer, float[]>> joints;

    private boolean drawBackground = true;
    private boolean drawPixels = true;
    private boolean drawSkeleton = true;    
    
    private BufferedImage bimg;
    int width, height;
    double sizeFactor = 1;

    public ImagePanel() {           
          //image = ImageIO.read(new File("image name and path"));
    	histogram = new float[10000];
        width = 800;
        height = 800;
        sizeFactor = 0.5;
        imgbytes = new byte[width*height*3];
        joints = new HashMap<Integer, HashMap<Integer, float[]>>();
    }

    @Override
    public void paintComponent(Graphics g) {
    	paint(g);
        g.drawImage(bimg, 0, 0, null); // see javadoc for more info on the parameters
    }
    
    private void calcHist(Channel depth)
    {
        // reset
        for (int i = 0; i < histogram.length; ++i)
            histogram[i] = 0;

        int points = 0;
        int depthIterator = 0;
        while(depthIterator < depth.getNumberOfTuples())
        {
            short depthVal = depth.getTuple(depthIterator++).getElementShort(0);
            if (depthVal != 0)
            {
                histogram[depthVal]++;
                points++;
            }
        }
        
        for (int i = 1; i < histogram.length; i++)
        {
            histogram[i] += histogram[i-1];
        }

        if (points > 0)
        {
            for (int i = 1; i < histogram.length; i++)
            {
                histogram[i] = 1.0f - (histogram[i] / (float)points);
            }
        }
    }
    
    public void updateJoints(SensorSnapshot snapshot)
    {
    	Channel user1Channel = snapshot.getChannel("User1");
    	if (user1Channel != null)
    	{
    		HashMap<Integer, float[]> user1Skeleton = new HashMap<Integer, float[]>();	
	    	for (int jointIndex = 0; jointIndex < 15; ++jointIndex)
	    	{
	    		float[] coordsAndConf = new float[4]; // coordinates (x, y, z) and confidence
	    		coordsAndConf[0] = user1Channel.getTuple(jointIndex).getElementFloat(0);
	    		coordsAndConf[1] = user1Channel.getTuple(jointIndex).getElementFloat(1);
	    		coordsAndConf[2] = user1Channel.getTuple(jointIndex).getElementFloat(2);
	    		coordsAndConf[3] = user1Channel.getTuple(jointIndex).getElementFloat(3);
	    		
	    		user1Skeleton.put(jointIndex, coordsAndConf);
	    	}
	    	joints.put(1, user1Skeleton);
    	}
    }

    Color colors[] = {Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.PINK, Color.YELLOW, Color.WHITE};

    void drawLine(Graphics g, HashMap<Integer, float[]> dict, int i, int j)
    {
		float[] pos1 = dict.get(i);
		float[] pos2 = dict.get(j);

		if (pos1 != null && pos2 != null) 
		{
			if (pos1[3] == 0 || pos2[3] == 0)
				return;
	
			g.drawLine((int)(sizeFactor*pos1[0]), (int)(height - sizeFactor*pos1[1]), (int)(sizeFactor*pos2[0]), (int)(height - sizeFactor*pos2[1]));
		}
    }
    
    public void drawSkeleton(Graphics g, int user) //throws StatusException
    {
    	HashMap<Integer, float[]> dict = joints.get(new Integer(user));

    	if (dict != null)
    	{
    		/*
    		 0 = head
    		 1 = neck
    		 2 = left shoulder 
    		 3 = left elbow
    		 4 = left hand
    		 5 = right shoulder
    		 6 = right elbow
    		 7 = right hand
    		 8 = middle torso
    		 9 = left pelvis
    		 10 = left knee
    		 11 = left foot
    		 12 = right pelvis
    		 13 = right knee
    		 14 = right foot
    		 */
	    	drawLine(g, dict, 0, 1); //head
	
	    	drawLine(g, dict, 2, 8); //left shoulder - middle torso
	    	drawLine(g, dict, 5, 8);
	
	    	drawLine(g, dict, 1, 2);
	    	drawLine(g, dict, 2, 3);
	    	drawLine(g, dict, 3, 4);
	
	    	drawLine(g, dict, 2, 5);
	    	drawLine(g, dict, 5, 6);
	    	drawLine(g, dict, 6, 7);
	
	    	drawLine(g, dict, 9, 8);
	    	drawLine(g, dict, 12, 8);
	    	drawLine(g, dict, 9, 12);
	
	    	drawLine(g, dict, 9, 10);
	    	drawLine(g, dict, 10, 11);
	
	    	drawLine(g, dict, 12, 13);
	    	drawLine(g, dict, 13, 14);
    	}

    }
    
    public void paint(Graphics g)
    {
    	if (drawPixels)
    	{
            DataBufferByte dataBuffer = new DataBufferByte(imgbytes, width*height*3);

            WritableRaster raster = Raster.createInterleavedRaster(dataBuffer, width, height, width * 3, 3, new int[]{0, 1, 2}, null); 

            ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8}, false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);

            bimg = new BufferedImage(colorModel, raster, false, null);

    		g.drawImage(bimg, 0, 0, null);
    	}
    	
		int[] users = {1};
		for (int i = 0; i < users.length; ++i)
		{
	    	Color c = colors[users[i]%colors.length];
	    	c = new Color(255-c.getRed(), 255-c.getGreen(), 255-c.getBlue());

	    	g.setColor(c);
	    	if (drawSkeleton)
			{
				drawSkeleton(g, users[i]);
			}
		}
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

}
