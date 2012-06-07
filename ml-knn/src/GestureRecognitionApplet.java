import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class GestureRecognitionApplet extends JApplet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8819074088088576998L;
	private JButton startButton = new JButton("Start");
	private JButton stopButton = new JButton("End");
	protected UserTracker userTracker = null; //new UserTracker();
	private JTextArea textArea;
	private boolean shouldRun = true;
	
	private static final int HEIGHT = 900;
	private static final int WIDTH = 800;
	
	protected boolean continueReadData = true;

	public GestureRecognitionApplet(){
		JPanel controls = new JPanel();	
		GridLayout gridLayout = new GridLayout(1,2);
		gridLayout.setHgap(10);
		gridLayout.setVgap(10);
		
		Container content = getContentPane();
		
		//Associate actions with the buttons
		ActionListener startListener = new ActionListener() {
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
				    		//SensorSnapshot snapshot = kinect.getSensorSnapshot();
				    		//System.out.println(snapshot.getChannelNames());
				    		//Channel dummy = snapshot.getChannel("User1");
				    		//snapshots.add(snapshot);
				    		//drawing the skeleton on the panel
						}
				    }
				};
				
				//Start the thread
		        final Thread threadA = new Thread(runA);
		        threadA.start();
			}
		};
		
		
		ActionListener stopListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				continueReadData = false;
				
				System.out.println("Stopping data reception");
				
			}
		};
		
		//Add the buttons to the container
		startButton.addActionListener(startListener);
		stopButton.addActionListener(stopListener);
		
		controls.setLayout(gridLayout);
		controls.add(startButton);
		controls.add(stopButton);
		
		userTracker = new UserTracker(WIDTH, HEIGHT);
		
		textArea = new JTextArea(7, 18);
		textArea.setEditable(false);
		
		
		textArea.append("Initialization...");
		JScrollPane scrollPane = new JScrollPane(textArea);
		content.add(scrollPane, BorderLayout.NORTH);

		content.add(controls, BorderLayout.SOUTH);
		content.add(userTracker, BorderLayout.CENTER);
		content.setBackground(Color.black);
	}
	
	
	void run()
    {
		int count = 0;
        while(shouldRun) {
        	if(count < 20){
        		textArea.append("Update depth + repaint\n");
        		textArea.setCaretPosition(textArea.getDocument().getLength());

        		count++;
        	}
        	
            userTracker.updateDepth();
//        	userTracker.updateJoints();
            userTracker.repaint();
        }
    }
	
	public static void main(String[] args){
		final JFrame frame = new JFrame("Gesture recognition");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension dim = new Dimension(WIDTH + 20, HEIGHT + 20);
		frame.setSize(dim);
		frame.setPreferredSize(dim);
		
		final GestureRecognitionApplet applet = new GestureRecognitionApplet();
		frame.getContentPane().add(applet);
		frame.pack();
		frame.setVisible(true);
		
		applet.run();
	}

}
