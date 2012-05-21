package classifier.kinectData;

import java.util.Date;

public class SkeletonSnapshot {
	public Date timestamp;
	
	public JointPosition headPosition;
	public JointPosition neckPosition;
	public JointPosition leftShoulderPosition;
	public JointPosition leftElbowPosition;
	public JointPosition leftHandPosition;
	public JointPosition rightHandPosition;
	public JointPosition rightElbowPosition;
	public JointPosition rightShoulderPosition;
	public JointPosition torsoPosition;
	public JointPosition leftHipPosition;
	public JointPosition leftKneePosition;
	public JointPosition leftFootPosition;
	public JointPosition rightHipPosition;
	public JointPosition rightKneePosition;
	public JointPosition rithFootPosition;
	
	public SkeletonSnapshot(){
		this.timestamp = new Date();
	}

}
