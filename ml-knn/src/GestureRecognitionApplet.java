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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JApplet;
import javax.swing.JFrame;


public class GestureRecognitionApplet extends JApplet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8819074088088576998L;
	
	private ViewerPanel _imgPanel;
	private UpdateThread _updater;
	
	
	protected boolean continueReadData = true;

	/**
	 * Constructor for GestureRecognitionApplet. Initializes the layout with the ImagePanel
	 */
	public GestureRecognitionApplet(){
		
		Container content = getContentPane();
		
		_imgPanel = new ViewerPanel();
		_updater = new UpdateThread(_imgPanel);
		
		content.add(_imgPanel, BorderLayout.CENTER);
		content.setBackground(Color.black);
		
		final JFrame frame = new JFrame("Gesture recognition");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.pack();
		frame.setVisible(true);
		
		
		_imgPanel.addHierarchyBoundsListener(new HierarchyBoundsListener(){
			 
            @Override
            public void ancestorMoved(HierarchyEvent e) {
            }
            @Override
            public void ancestorResized(HierarchyEvent e) {
                System.out.println(e.getComponent().getSize());
                _imgPanel.updateSize(e.getChanged().getWidth(), e.getChanged().getHeight());
                 
            }           
        });
		_updater.start();
	}
	
	public void Update(SensorSnapshot snapshot){
		_updater.addSnapshot(snapshot);
	}
	

}

class UpdateThread extends Thread{
	
	protected ArrayBlockingQueue<SensorSnapshot> _snapshotQueue;
	protected volatile boolean _running = false;
	protected ViewerPanel _vwPanel;
	
	public UpdateThread(ViewerPanel vwPanel){
		_snapshotQueue = new ArrayBlockingQueue<SensorSnapshot>(10);
		_vwPanel = vwPanel;
	}
	
	public boolean addSnapshot(SensorSnapshot snapshot){
		try{
			return _snapshotQueue.add(snapshot);
		} catch(IllegalStateException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void stopUpdates(){
		_running = false;
	}
	
	public void run(){
		_running = true;
		SensorSnapshot snapshot;
		
		while(_running){
			try{
				snapshot = _snapshotQueue.take();
				_vwPanel.updateJoints(snapshot);
				_vwPanel.repaint();
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			
		}
		 
		
	}
}
