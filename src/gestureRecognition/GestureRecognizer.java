/**************************************************************
This file is part of Kinect Sensor Architecture Development Project.

   Kinect Sensor Architecture Development Project is free software:
you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Kinect Sensor Architecture Development Project is distributed in
the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Kinect Sensor Architecture Development Project.  If
not, see <http://www.gnu.org/licenses/>.
**************************************************************/
/**************************************************************
The work was done in joint collaboration with Cisco Systems Inc.
Copyright © 2012, Cisco Systems, Inc. and UCLA
*************************************************************/

package gestureRecognition;
/**************************************************************

   Kinect Sensor Architecture Development Project is free software:
you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Kinect Sensor Architecture Development Project is distributed in
the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Kinect Sensor Architecture Development Project.  If
not, see <http://www.gnu.org/licenses/>.
**************************************************************/
/**************************************************************
The work was done in joint collaboration with Cisco Systems Inc.
Copyright © 2012, Cisco Systems, Inc. and UCLA
*************************************************************/

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.OpenNI.UniOpenNIDevice;

import UnifyingAPI.Channel;
import UnifyingAPI.Sensor;
import UnifyingAPI.SensorSnapshot;
import UnifyingAPI.UniDevice;

/**
 * 
 */

/**
 * @author sunghoon
 *
 */
public class GestureRecognizer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;  //  @jve:decl-index=0:visual-constraint="548,10"
	private JButton jButton_start = null;
	private JButton jButton_end = null;
	private boolean continueReadData = false; //flag that allows to connect to the sensor unless user presses the END button
	private boolean startCapture = false; //flag that indicates to start capturing the sensor data
	private static int NUM_FEATURES = 16;
	private Sensor kinect;
	private LinkedList<SensorSnapshot> snapshots;
	private JButton jButton = null;
	private JLabel jLabel_skeleton_head = null;
	private JTextArea jTextArea_Head_X = null;
	private JTextArea jTextArea_Head_Y = null;
	private JLabel jLabel14 = null;
	private JLabel jLabel15 = null;
	private JLabel jLabel = null;
	private JTextField jTextField_RF_X = null;
	private JTextField jTextField_RF_Y = null;
	private JTextField jTextField_RF_Z = null;
	private JTextArea jTextArea_Head_Z = null;
	private JLabel jLabel1 = null;
	private float[][] trainingFeatures;
	private int[] trainingLabels;
	private int totalTrainingSize;
	private final int K = 10; //number of nearest neighbor that we consider
	private final int NUM_GESTURES = 4;
	private final boolean TRAINING = false;
	private JLabel jLabel2 = null;
	private JTextField jTextField_gesture = null;
	private JTextArea jTextArea_Comment = null;
	private JLabel jLabel3 = null;
	private JTextField jTextField_confidence = null;
	private GestureRecognitionApplet gesRecApplet = null;
	
	/**
	 * This method initializes jButton_start	
	 * 	jButton_start will indicate the start of the gesture performance event
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_start() {
		if (jButton_start == null) {
			jButton_start = new JButton();
			jButton_start.setBounds(new Rectangle(5, 5, 180, 50));
			jButton_start.setText("CONNECT SENSOR");
			//This is the function that is executed when the START button is pressed
			jButton_start.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//first it cleans up the global variable that 
					continueReadData = true;
					startCapture = false;
					
					//creating a thread that will continue receiving the data from the sensor
					//until the END button is pressed
					Runnable runA = new Runnable() {
					    public void run() {
					    	while(continueReadData)
							{
					    		//request data
					    		System.out.println("Receive Data\n"); // TODO Auto-generated Event stub actionPerformed()
					    		SensorSnapshot snapshot = kinect.getSensorSnapshot();
					    		System.out.println(snapshot.getChannelNames());
					    		
					    		Channel user1Channel = snapshot.getChannel("User1");
					    		
					    		gesRecApplet.Update(snapshot);
					    		
								//getting the skeleton data
						    	if (user1Channel != null)
						    	{
							    	for (int jointIndex = 0; jointIndex < 15; ++jointIndex)
							    	{
							    		try{
								    		float[] coordsAndConf = new float[4]; // coordinates (x, y, z) and confidence
								    		coordsAndConf[0] = user1Channel.getTuple(jointIndex).getElementFloat(0);
								    		coordsAndConf[1] = user1Channel.getTuple(jointIndex).getElementFloat(1);
								    		coordsAndConf[2] = user1Channel.getTuple(jointIndex).getElementFloat(2);
								    		coordsAndConf[3] = user1Channel.getTuple(jointIndex).getElementFloat(3);
								    		
								    		if (jointIndex == 0)
								    		{
									    		jTextArea_Head_X.setText(String.valueOf(coordsAndConf[0]));
									    		jTextArea_Head_Y.setText(String.valueOf(coordsAndConf[1]));	
									    		jTextArea_Head_Z.setText(String.valueOf(coordsAndConf[2]));	
								    		}
								    		else if(jointIndex == 14)
								    		{
								    			jTextField_RF_X.setText(String.valueOf(coordsAndConf[0]));
									    		jTextField_RF_Y.setText(String.valueOf(coordsAndConf[1]));
									    		jTextField_RF_Z.setText(String.valueOf(coordsAndConf[2]));	
								    		}
							    		}
							    		catch(Exception e){
							    			e.printStackTrace();
							    		}
							    	}
						    	}
					    		
					    		if (startCapture)
						    	{	
						    		snapshots.add(snapshot);
						    		//drawing the skeleton on the panel
					    		}
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
			jButton_end.setBounds(new Rectangle(5, 125, 180, 50));
			jButton_end.setText("END");
			//This is the function that is executed when the END button is pressed
			jButton_end.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					continueReadData = false;
					System.out.println("-------------------\n");
					System.out.println("Stop Receiving Data\n");
					float[][][] rawSensorDataMatrix = new float[snapshots.size()][15][4];
					//The index for "rawSensorDataMatrix" is [numDataSamples][jointIndx][x;y;z;confidence]
					
					//Filling in rawSensorDataMatrix
					try 
					{
						for (int listElement = 0; listElement < snapshots.size()-1; listElement++)
						{
							//getting the skeleton data from snapshots at the index "listElement" 
							Channel skeletonChannel = snapshots.get(listElement).getChannel("User1");
							if (skeletonChannel != null)
					    	{				    		
						    	for (int jointIndex = 0; jointIndex < 15; ++jointIndex)
						    	{
						    		try{
							    		rawSensorDataMatrix[listElement][jointIndex][0] = skeletonChannel.getTuple(jointIndex).getElementFloat(0);
							    		rawSensorDataMatrix[listElement][jointIndex][1] = skeletonChannel.getTuple(jointIndex).getElementFloat(1);
							    		rawSensorDataMatrix[listElement][jointIndex][2] = skeletonChannel.getTuple(jointIndex).getElementFloat(2);
							    		rawSensorDataMatrix[listElement][jointIndex][3] = skeletonChannel.getTuple(jointIndex).getElementFloat(3);
						    		}
						    		catch(Exception exc){
						    			exc.printStackTrace();
						    		}
						    	}
					    	}
						}
					}
					catch (ArrayIndexOutOfBoundsException aioobe)
					{
						System.out.println("Array Index Out of Bound Exception\n");
					}
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
					
					//Feature extraction
					// Feature #1: Std. of right hand X - coordinate
					// Feature #2: Std. of right hand Y - coordinate
					// Feature #3: Std. of middle torso X - coordinate
					// Feature #4: Std. of middle torso Y - coordinate
					
					//The following code is to obtain training data so that we can create the database. 
					if (TRAINING)
					{
						try 
						{
							File file = new File("kick_trial_10.txt");
							BufferedWriter output = new BufferedWriter(new FileWriter(file));
							//DataOutputStream output = new DataOutputStream(new FileOutputStream("handwave_trial_01.txt"));
							
							for (int listElement = 0; listElement < snapshots.size()-1; listElement++)
							{
								//getting the skeleton data from snapshots at the index "listElement" 									    		
						    	for (int jointIndex = 0; jointIndex < 15; jointIndex++)
						    	{
						    		for (int xyzc = 0; xyzc < 4; xyzc++)
						    		{
						    			//output.writeFloat(rawSensorDataMatrix[listElement][jointIndex][xyzc]);
						    			output.write(String.valueOf(rawSensorDataMatrix[listElement][jointIndex][xyzc]));
						    			output.write(";");
						    		}
						    		output.write(" ");
						    	}
						    	output.write("\n");
							}
							output.close();
							
							float[] featureMatrix = new float[NUM_FEATURES];					
							featureMatrix = featureExtraction(rawSensorDataMatrix);
							
							System.out.println("-------------------\n");
							System.out.println(String.valueOf(featureMatrix[6]) + " " + String.valueOf(featureMatrix[7]) + " " + 
									String.valueOf(featureMatrix[8]));
							System.out.println(String.valueOf(featureMatrix[14]) + " " + String.valueOf(featureMatrix[15]));
							System.out.println(String.valueOf(featureMatrix[0]) + " " + String.valueOf(featureMatrix[1]));
						}
						catch (IOException ioe)
						{
							//
						}
					}
					else //recognize the performed gesture
					{
						float[] featureMatrix = new float[NUM_FEATURES];					
						featureMatrix = featureExtraction(rawSensorDataMatrix);
						//extract features
						GestureResult result_gesture = runKNN(featureMatrix);
						System.out.println("Recognized Gesture = " + String.valueOf(result_gesture.label) + "\n");
						System.out.println("Confidence = " + String.valueOf(result_gesture.confidence) + "%\n");
						
						if (result_gesture.label == 0)
						{
							//Handwave detected
							getJTextArea_Comment().setText("Hello there!");
							getJTextField_gesture().setText("Handwave");
						}
						else if (result_gesture.label == 1)
							
						{
							//Body tilt detected
							getJTextArea_Comment().setText("Are you sick?!");
							getJTextField_gesture().setText("Bodytilt");
						}
						else if (result_gesture.label == 2)
						{
							//Puch detected
							getJTextArea_Comment().setText("Please don't punch me!!");
							getJTextField_gesture().setText("Punch");
						}
						else if (result_gesture.label == 3)
						{
							//Body tilt detected
							getJTextArea_Comment().setText("You are a bad person!!");
							getJTextField_gesture().setText("Kick");
						}
						else
						{
							//recognition error
							getJTextArea_Comment().setText("I don't know what you are doing!!");
							getJTextField_gesture().setText("Not Recognized");
						}
						getJTextField_confidence().setText(String.valueOf(result_gesture.confidence));
					}
					
					//clear the linked list
					snapshots.clear();
					//Now perform the K-NN classification
					//Knn.runKNN();
					
				}
			});
		}
		return jButton_end;
	}
	
	private static float[] featureExtraction (float[][][] rawSensorDataMatrix)
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
		
		int sensorMatrixSize = rawSensorDataMatrix.length;
		float[] featureMatrix = new float[NUM_FEATURES];
		
		//Computer feature #1
		float[] headX = new float[sensorMatrixSize];
		float[] headY = new float[sensorMatrixSize];
		float[] midTorsoX = new float[sensorMatrixSize];
		float[] midTorsoY = new float[sensorMatrixSize];
		float[] rightShoulderX = new float[sensorMatrixSize];
		float[] rightShoulderY = new float[sensorMatrixSize];
		float[] rightHandX = new float[sensorMatrixSize];
		float[] rightHandY = new float[sensorMatrixSize];
		float[] rightHandZ = new float[sensorMatrixSize];
		float[] leftShoulderX = new float[sensorMatrixSize];
		float[] leftShoulderY = new float[sensorMatrixSize];
		float[] leftHandX = new float[sensorMatrixSize];
		float[] leftHandY = new float[sensorMatrixSize];
		float[] leftHandZ = new float[sensorMatrixSize];
		float[] rightFootY = new float[sensorMatrixSize];
		float[] rightFootZ = new float[sensorMatrixSize];
		
		
		for (int dataSampleIdx = 0; dataSampleIdx < sensorMatrixSize; dataSampleIdx++){
			headX[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][0][0];
			headY[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][0][1];
			midTorsoX[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][8][0];
			midTorsoY[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][8][1];
			rightShoulderX[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][5][0];
			rightShoulderY[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][5][1];
			rightHandX[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][7][0];
			rightHandY[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][7][1];
			rightHandZ[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][7][2];
			leftShoulderX[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][2][0];
			leftShoulderY[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][2][1];
			leftHandX[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][4][0];
			leftHandY[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][4][1];
			leftHandZ[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][4][2];
			rightFootY[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][14][1];
			rightFootZ[dataSampleIdx] = rawSensorDataMatrix[dataSampleIdx][14][2];
			
		}
		
		featureMatrix[0] = computeStdDev(headX);
		featureMatrix[1] = computeStdDev(headX);
		featureMatrix[2] = computeStdDev(midTorsoX);
		featureMatrix[3] = computeStdDev(midTorsoY);
		
		featureMatrix[4] = computeStdDev(rightShoulderX);
		featureMatrix[5] = computeStdDev(rightShoulderY);
		featureMatrix[6] = computeStdDev(rightHandX);
		featureMatrix[7] = computeStdDev(rightHandY);
		featureMatrix[8] = computeStdDev(rightHandZ);
		
		featureMatrix[9] = computeStdDev(leftShoulderX);
		featureMatrix[10] = computeStdDev(leftShoulderY);
		featureMatrix[11] = computeStdDev(leftHandX);
		featureMatrix[12] = computeStdDev(leftHandY);
		featureMatrix[13] = computeStdDev(leftHandZ);
		
		featureMatrix[14] = computeStdDev(rightFootY);
		featureMatrix[15] = computeStdDev(rightFootZ);
		
		return featureMatrix;
	}
	
	private static float computeAverage ( float[] data ) 
	{ 
		final int n = data.length; 
		if ( n < 2 ) 
			return Float.NaN; 
		float sum = 0.0f; 
		for ( int i = 0; i < data.length; i++ ) { 
			sum += data[i];
		} 
		// Change to ( n - 1 ) to n if you have complete data instead of a sample. 
		return sum / n; 
	} 
	
	public static float computeStdDev ( float[] data) 
	{ 
		final int n = data.length; 
		if ( n < 2 ) 
			return Float.NaN; 
		
		float avg = computeAverage(data);
		float sum = 0; 
		for ( int i = 0; i < data.length; i++ ) { 
			sum += ( data[i] - avg ) * (data[i] - avg);
		} 
		// Change to ( n - 1 ) to n if you have complete data instead of a sample. 
		return (float)Math.sqrt( sum / n ); 
	} 
	

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(5, 65, 180, 50));
			jButton.setText("START");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//indicate to start capturing the sensor data.
					startCapture = true;
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jTextArea_Head_X	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea_Head_X() {
		if (jTextArea_Head_X == null) {
			jTextArea_Head_X = new JTextArea();
			jTextArea_Head_X.setBounds(new Rectangle(300, 30, 60, 15));
		}
		return jTextArea_Head_X;
	}

	/**
	 * This method initializes jTextArea_Head_Y	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea_Head_Y() {
		if (jTextArea_Head_Y == null) {
			jTextArea_Head_Y = new JTextArea();
			jTextArea_Head_Y.setBounds(new Rectangle(370, 30, 60, 15));
		}
		return jTextArea_Head_Y;
	}

	/**
	 * This method initializes jTextField_RF_X	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_RF_X() {
		if (jTextField_RF_X == null) {
			jTextField_RF_X = new JTextField();
			jTextField_RF_X.setBounds(new Rectangle(300, 63, 60, 20));
		}
		return jTextField_RF_X;
	}

	/**
	 * This method initializes jTextField_RF_Y	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_RF_Y() {
		if (jTextField_RF_Y == null) {
			jTextField_RF_Y = new JTextField();
			jTextField_RF_Y.setBounds(new Rectangle(370, 63, 60, 20));
		}
		return jTextField_RF_Y;
	}

	/**
	 * This method initializes jTextField_RF_Z	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_RF_Z() {
		if (jTextField_RF_Z == null) {
			jTextField_RF_Z = new JTextField();
			jTextField_RF_Z.setBounds(new Rectangle(440, 63, 60, 20));
		}
		return jTextField_RF_Z;
	}

	/**
	 * This method initializes jTextArea_Head_Z	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea_Head_Z() {
		if (jTextArea_Head_Z == null) {
			jTextArea_Head_Z = new JTextArea();
			jTextArea_Head_Z.setBounds(new Rectangle(440, 30, 60, 15));
		}
		return jTextArea_Head_Z;
	}

	/**
	 * This method initializes jTextField_gesture	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_gesture() {
		if (jTextField_gesture == null) {
			jTextField_gesture = new JTextField();
			jTextField_gesture.setBounds(new Rectangle(385, 106, 114, 19));
		}
		return jTextField_gesture;
	}

	/**
	 * This method initializes jTextArea_Comment	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea_Comment() {
		if (jTextArea_Comment == null) {
			jTextArea_Comment = new JTextArea();
			jTextArea_Comment.setBounds(new Rectangle(217, 166, 279, 68));
			jTextArea_Comment.setFont(new Font(getName(), Font.BOLD, 22));
		}
		return jTextArea_Comment;
	}

	/**
	 * This method initializes jTextField_confidence	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_confidence() {
		if (jTextField_confidence == null) {
			jTextField_confidence = new JTextField();
			jTextField_confidence.setBounds(new Rectangle(386, 136, 111, 19));
		}
		return jTextField_confidence;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		
		gesRecApplet = new GestureRecognitionApplet();
		
		//read training data
		readTrainingData();
	}
	
	/**
	 * This function reads the training data from the local database and construct the training set for KNN
	 * It outputs two variables (1) trainingFeatures and (2) trainingLabels
	 */
	public void readTrainingData()
	{
		System.out.println("-------------------\n");
		System.out.println("Loading Training Data...\n");
		
		//read features for Handwave gesture
		String dir_path = "handwave/";		
		float[][] featureMatrix_Handwave = readGestureFile(dir_path);
		
		//read features for Bodytilt gesture
		dir_path = "bodytilt/";		
		float[][] featureMatrix_Bodytilt = readGestureFile(dir_path);
		
		//read features for Bodytilt punch
		dir_path = "punch/";		
		float[][] featureMatrix_Punch = readGestureFile(dir_path);
		
		//read features for Bodytilt kick
		dir_path = "kick/";		
		float[][] featureMatrix_Kick = readGestureFile(dir_path);
		
		//now combine all the gestures
		totalTrainingSize = featureMatrix_Handwave.length + featureMatrix_Bodytilt.length + 
			featureMatrix_Punch.length + featureMatrix_Kick.length;
		
		//Now we know the size of the total feature matrix.
		trainingFeatures = new float[totalTrainingSize][NUM_FEATURES];
		trainingLabels = new int[totalTrainingSize];
		
		//Copying Handwave feature into the main feature matrix
		for (int idxSample = 0; idxSample < featureMatrix_Handwave.length; idxSample++)
		{
			for (int idxFeature = 0; idxFeature < NUM_FEATURES; idxFeature++)
				trainingFeatures[idxSample][idxFeature] = featureMatrix_Handwave[idxSample][idxFeature];
			trainingLabels[idxSample] = 0;
		}
		int idxOffSet = featureMatrix_Handwave.length;
		
		//Copying bodytilt feature into the main feature matrix
		for (int idxSample = idxOffSet; idxSample < idxOffSet + featureMatrix_Bodytilt.length; idxSample++)
		{
			for (int idxFeature = 0; idxFeature < NUM_FEATURES; idxFeature++)
				trainingFeatures[idxSample][idxFeature] = featureMatrix_Bodytilt[idxSample-idxOffSet][idxFeature];
			trainingLabels[idxSample] = 1;
		}
		idxOffSet = featureMatrix_Handwave.length + featureMatrix_Bodytilt.length;
		
		//Copying bodytilt feature into the main feature matrix
		for (int idxSample = idxOffSet; idxSample < idxOffSet + featureMatrix_Punch.length; idxSample++)
		{
			for (int idxFeature = 0; idxFeature < NUM_FEATURES; idxFeature++)
				trainingFeatures[idxSample][idxFeature] = featureMatrix_Punch[idxSample-idxOffSet][idxFeature];
			trainingLabels[idxSample] = 2;
		}
		idxOffSet = featureMatrix_Handwave.length + featureMatrix_Bodytilt.length + featureMatrix_Punch.length;
		
		//Copying bodytilt feature into the main feature matrix
		for (int idxSample = idxOffSet; idxSample < idxOffSet + featureMatrix_Kick.length; idxSample++)
		{
			for (int idxFeature = 0; idxFeature < NUM_FEATURES; idxFeature++)
				trainingFeatures[idxSample][idxFeature] = featureMatrix_Kick[idxSample-idxOffSet][idxFeature];
			trainingLabels[idxSample] = 3;
		}
		idxOffSet = featureMatrix_Handwave.length + featureMatrix_Bodytilt.length + featureMatrix_Punch.length  + featureMatrix_Kick.length;
		
		
		System.out.println("Loading Completed!\n");
		
		// this is for testing purpose and needs to be removed later.
		/*
		float[] testingFeatures = new float[NUM_FEATURES];
		for (int i =0; i < NUM_FEATURES; i++)
			testingFeatures[i] = featureMatrix_Handwave[2][i];
		
		GestureResult result_gesture = runKNN(testingFeatures);
		System.out.println("Recognized Gesture = " + String.valueOf(result_gesture.label) + "\n");
		System.out.println("Confidence = " + String.valueOf(result_gesture.confidence) + "%\n");
		*/
		
	}
	
	public GestureResult runKNN(float[] testingFeatures)
	{
		Distance[] distances = new Distance[totalTrainingSize];
		for (int idxTraining = 0; idxTraining < totalTrainingSize; idxTraining++) 
			distances[idxTraining] = new Distance();
		
		// find the knn
        for (int idxTraining = 0; idxTraining < totalTrainingSize; idxTraining++){
        	//compute the distance between the testing set and the training set
        	
        	float dist = computeDistance(testingFeatures, idxTraining);
        	
        	
            if (idxTraining < K)
            {
            	distances[idxTraining].d = dist;
                distances[idxTraining].label = trainingLabels[idxTraining];
            }
            else
            {// go through the knn list and replace the biggest one if possible
                float biggestd = distances[0].d;
                int biggestindex = 0;
                for (int idxDist = 1; idxDist < K; idxDist++)
                {
                    if (distances[idxDist].d > biggestd)
                    {
                    	biggestd = distances[idxDist].d;
                    	biggestindex = idxDist;
                    }
                }
                if (dist < biggestd)
               {
            		distances[biggestindex].d = dist;
            		distances[biggestindex].label = trainingLabels[idxTraining];;
               }
            }
        }
        //now compute the the frequency of each gesture within the K-NN
        
        //initialization
        int[] gestureFrequency = new int[NUM_GESTURES];
        for (int idxGesutre = 0; idxGesutre < NUM_GESTURES; idxGesutre++)
        	gestureFrequency[idxGesutre] = 0;
        
        //count gesture frequency
        for (int idxKNN = 0; idxKNN < K; idxKNN++)
        	gestureFrequency[distances[idxKNN].label]++; //increment the corresponding gesture
        
        int maxGestureFreq = 0;
        int gesture_recognized = -1;
        
        for (int idxGesutre = 0; idxGesutre < NUM_GESTURES; idxGesutre++)
        {
        	if (gestureFrequency[idxGesutre] >= maxGestureFreq)
        	{
        		maxGestureFreq = gestureFrequency[idxGesutre];
        		gesture_recognized = idxGesutre; //the gesture with maximum frequency
        	}
        }
        
        GestureResult gesture_result = new GestureResult();
        gesture_result.label = gesture_recognized;
        gesture_result.confidence = maxGestureFreq / K * 100;
        
        return gesture_result;
	}
	
	public float computeDistance(float[] testingFeatures, int idxTraining)
	{
		float dist = 0.0f;
		
		for (int idxFeature = 0; idxFeature < NUM_FEATURES; idxFeature++)
			dist += (testingFeatures[idxFeature] - trainingFeatures[idxTraining][idxFeature]) * (testingFeatures[idxFeature] - trainingFeatures[idxTraining][idxFeature]);
		
		return (float)Math.sqrt((double)dist);
	}
	
	public float[][] readGestureFile(String dir_path)
	{
		final File folder = new File(dir_path);
		//counting the number of training data in the designated folder
		int numTrainingSet = 0;
		for (final File fileEntry : folder.listFiles()) {
	        if (!fileEntry.isDirectory())
	        	numTrainingSet++;
		}
		
		//matrix that will contain features
		float[][] featureMatrix = new float[numTrainingSet][NUM_FEATURES];
		
		int idxTextFile = 0 ;
		for (final File fileEntry : folder.listFiles()) {
	        if (!fileEntry.isDirectory()) {
	        	String file_name = fileEntry.getName();
	        	 try{
	        		  // Open the file that is the first 
	        		  // command line parameter
	        		  FileInputStream fstream = new FileInputStream(dir_path + file_name);
	        		  // Get the object of DataInputStream
	        		  DataInputStream in = new DataInputStream(fstream);
	        		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        		  String strLine;
	        		  //Read File Line By Line
	        		  int numSamples = 0;
	        		  //counting the number of samples in a text file.
	        		  while ((strLine = br.readLine()) != null)   
	        		  {
	        			  numSamples++;
	        		  }        		  
	        		  //Close the input stream
	        		  in.close();	        		  
	        		  //raw training data matrix
	        		  float[][][] rawTrainingDataMatrix = new float[numSamples][15][4];
	        		  
	        		  //re-initializing the text file
	        		  fstream = new FileInputStream(dir_path + file_name);
	        		  in = new DataInputStream(fstream);
	        		  br = new BufferedReader(new InputStreamReader(in));
	        		  
	        		  int idxSamples = 0;
	        		  while ((strLine = br.readLine()) != null)   
	        		  {
	        			  String delims_out = " "; //outer delim
	        			  String[] tokens_out = strLine.split(delims_out);
	        			  for (int jointIndex = 0; jointIndex < tokens_out.length; jointIndex++)
	        			  {
	        				  String delims_in = ";"; //inner delim
	        				  String[] tokens_in = tokens_out[jointIndex].split(delims_in);
	        				  for (int xyzc = 0; xyzc < tokens_in.length; xyzc++)
	        					  rawTrainingDataMatrix[idxSamples][jointIndex][xyzc] = new Float(tokens_in[xyzc]);
	        			  }
	        			  idxSamples++; //this is the index for each txt file (training data)
	        		  }
	        		  //Close the input stream
	        		  in.close();
	        		  
	        		  //feature extracted from the current text file
	        		  float[] localFeatures = featureExtraction(rawTrainingDataMatrix);
	        		  
	        		  //copying into the feature matrix that will be returned
	        		  for (int i = 0; i < NUM_FEATURES; i++)
	        			  featureMatrix[idxTextFile][i] = localFeatures[i];
	        		  
	        		  idxTextFile++;
	        	 }
	        	 catch (Exception e){//Catch exception if any
	        		  System.err.println("Error: " + e.getMessage());
	        	 }	
	        }
	    }
		return featureMatrix;
	}

	/**
	 * This method initializes the main frame
	 * Sets the size and creates the view
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(528, 281);
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
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(217, 135, 145, 15));
			jLabel3.setText("Confidence Level");
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(216, 107, 146, 14));
			jLabel2.setText("Recognized Gesture");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(465, 12, 43, 15));
			jLabel1.setText("Z");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(210, 67, 76, 15));
			jLabel.setText("Right Foot");
			jLabel15 = new JLabel();
			jLabel15.setBounds(new Rectangle(396, 11, 19, 15));
			jLabel15.setText("Y");
			jLabel14 = new JLabel();
			jLabel14.setBounds(new Rectangle(323, 11, 43, 15));
			jLabel14.setText("X");
			jLabel_skeleton_head = new JLabel();
			jLabel_skeleton_head.setBounds(new Rectangle(226, 31, 46, 16));
			jLabel_skeleton_head.setText("Head");
			jContentPane = new JPanel();
			//jContentPane.setLayout(new BorderLayout());
			jContentPane.setLayout(null);			
			jContentPane.setSize(new Dimension(696, 622));
			
			jContentPane.add(getJButton_start(), null);
			jContentPane.add(getJButton_end(), null);
			
	        // add graphic component to center of this  
	        // JFrames (default) BorderLayout  
			jContentPane.add(getJButton(), null);
			jContentPane.add(jLabel_skeleton_head, null);
			jContentPane.add(getJTextArea_Head_X(), null);
			jContentPane.add(getJTextArea_Head_Y(), null);
			jContentPane.add(jLabel14, null);
			jContentPane.add(jLabel15, null);		
			jContentPane.add(jLabel, null);
			jContentPane.add(getJTextField_RF_X(), null);
			jContentPane.add(getJTextField_RF_Y(), null);
			jContentPane.add(getJTextField_RF_Z(), null);
			jContentPane.add(getJTextArea_Head_Z(), null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabel2, null);
			jContentPane.add(getJTextField_gesture(), null);
			jContentPane.add(getJTextArea_Comment(), null);
			jContentPane.add(jLabel3, null);
			jContentPane.add(getJTextField_confidence(), null);
			
		}
		return jContentPane;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

class Distance {
    float d;
    int label;
} 

class GestureResult {
	int label;
	double confidence;
}

