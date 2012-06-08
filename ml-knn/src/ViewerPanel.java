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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.HashMap;

import javax.swing.JPanel;


public class ViewerPanel extends JPanel{

    private BufferedImage image;
    private static final long serialVersionUID = 1L;
    private byte[] imgbytes;
    private float histogram[];
    HashMap<Integer, HashMap<Integer, float[]>> joints;

    private boolean drawBackground = true;
    private boolean drawPixels = true;
    private boolean drawSkeleton = true;    
    
    private final int MAX_WIDTH = 800;
    private final int MAX_HEIGHT = 800;
    
    private BufferedImage bimg;
    private int _width, _height;
    private double _sizeFactor = 1;

    public ViewerPanel() {           
          //image = ImageIO.read(new File("image name and path"));
    	histogram = new float[10000];
        _width = 800;
        _height = 800;
        _sizeFactor = 0.5 * _width/MAX_WIDTH;
        imgbytes = new byte[_width*_height*3];
        joints = new HashMap<Integer, HashMap<Integer, float[]>>();
    }
    
    public void updateSize(int width, int height){
    	_width = width;
    	_height = height;
        _sizeFactor = 0.5 * _width/MAX_WIDTH;
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
	
			g.drawLine((int)(_width/2 - _sizeFactor*pos1[0]), (int)(_height/2 - _sizeFactor*pos1[1]), (int)(_width/2 - _sizeFactor*pos2[0]), (int)(_height/2 - _sizeFactor*pos2[1]));
//			g.drawLine((int)(pos1[0]), (int)(pos1[1]), (int)(pos2[0]), (int)(pos2[1]));
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
    		//user1Skeleton.put(jointIndex, coordsAndConf);

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
            DataBufferByte dataBuffer = new DataBufferByte(imgbytes, _width*_height*3);

            WritableRaster raster = Raster.createInterleavedRaster(dataBuffer, _width, _height, _width * 3, 3, new int[]{0, 1, 2}, null); 

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
        return new Dimension(_width, _height);
    }
}
